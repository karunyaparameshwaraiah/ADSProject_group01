// ADS I Class Project
// Pipelined RISC-V Core - EX Stage
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 01/09/2026 by Tobias Jauch (@tojauch)

/*
Instruction Execute (EX) Stage: ALU operations and exception detection

Instantiated Modules:
    ALU: Integrate your module from Assignment02 for arithmetic/logical operations

ALU Interface:
    alu.io.operandA: first operand input
    alu.io.operandB: second operand input
    alu.io.operation: operation code controlling ALU function
    alu.io.aluResult: computation result output

Internal Signals:
    Map uopc codes to ALUOp values

Functionality:
    Map instruction uop to ALU operation code
    Pass operands to ALU
    Output results to pipeline

Outputs:
    aluResult: computation result from ALU
    exception: pass exception flag
*/

package core_tile

import chisel3._
import chisel3.util._
import Assignment02.{ALU, ALUOp}
import uopc._

// -----------------------------------------
// Execute Stage
// -----------------------------------------

class EX extends Module {
  val io = IO(new Bundle {
    // Inputs from ID Barrier
    val uop = Input(uopc())
    val operandA = Input(UInt(32.W))
    val operandB = Input(UInt(32.W))
    val rd = Input(UInt(5.W))
    val XcptInvalid = Input(Bool())
    val imm = Input(UInt(32.W)) // Immediate value from ID stage
    val pc = Input(UInt(32.W)) // Program Counter from ID stage

    // Register Write control signal from ID
    val regWrite = Input(Bool())
    
    //Forwarding inputs
    val forwardA = Input(UInt(2.W)) // Control signal for operandA forwarding
    val forwardB = Input(UInt(2.W)) // Control signal for operandB forwarding

    // Forwarding data inputs
    val dataFromWB = Input(UInt(32.W)) // Data forwarded from WB stage
    val dataFromMEM = Input(UInt(32.W)) // Data forwarded from MEM stage

    val predictTaken = Input(Bool()) // Branch prediction result from BTB
    
    // Outputs to EX Barrier
    val aluResult = Output(UInt(32.W))
    val outRD = Output(UInt(5.W))
    val outXcptInvalid = Output(Bool())

    // These now act as "Flush Pipeline" and "Recovery Address"
    val takeBranch = Output(Bool()) // True if we should jump
    val targetAddr = Output(UInt(32.W)) // Where to jump if takeBranch is true

    // Pass RegWrite to the EX Barrier
    val outRegWrite = Output(Bool())

    // Outputs to update the BTB
    val btbUpdate = Output(Bool())
    val btbUpdatePC = Output(UInt(32.W))
    val btbUpdateTarget = Output(UInt(32.W))
    val btbMispredict = Output(Bool())

    // Hardware Performance Counters
    val totalBranches = Output(UInt(32.W))
    val totalMispredicts = Output(UInt(32.W))
  })

  // Instantiate ALU from Assignment02
  val alu = Module(new ALU())

  // //MUX for forwarding logic
  // val operandA = MuxCase(io.forwardA, io.operandA, Seq(
  //   "b00".U -> io.operandA, // No forwarding use operand from ID
  //   "b01".U -> io.dataFromWB, // Forward from WB stage
  //   "b10".U -> io.dataFromMEM // Forward from MEM stage
  // ))

  // val operandB = MuxCase(io.forwardB, io.operandB, Seq(
  //   "b00".U -> io.operandB, // No forwarding use operand from ID
  //   "b01".U -> io.dataFromWB, // Forward from WB stage
  //   "b10".U -> io.dataFromMEM // Forward from MEM stage
  // ))


  // Select Operand A
  val opA_mux = MuxCase(io.operandA, Seq(
    (io.forwardA === "b10".U) -> io.dataFromMEM, // Priority: MEM stage is newer
    (io.forwardA === "b01".U) -> io.dataFromWB
  ))

  // FIX 2: Identify instructions that actually use an rs2 register!
  val uses_rs2 = (io.uop === uopADD) || (io.uop === uopSUB) || (io.uop === uopSLL) || 
                 (io.uop === uopSLT) || (io.uop === uopSLTU) || (io.uop === uopXOR) || 
                 (io.uop === uopSRL) || (io.uop === uopSRA) || (io.uop === uopOR) || 
                 (io.uop === uopAND) || 
                 (io.uop === uopBEQ) || (io.uop === uopBNE) || (io.uop === uopBLT) || 
                 (io.uop === uopBGE) || (io.uop === uopBLTU) || (io.uop === uopBGEU)

  // Select Operand B: ONLY forward if the instruction actually uses rs2.
  // Otherwise, default to io.operandB (which safely holds your immediate!)
  val opB_mux = MuxCase(io.operandB, Seq(
    (uses_rs2 && io.forwardB === "b10".U) -> io.dataFromMEM,
    (uses_rs2 && io.forwardB === "b01".U) -> io.dataFromWB
  ))

  // Map uopc micro-operation codes to ALU operation codes
  val aluOp = Wire(ALUOp())
  
  aluOp := MuxLookup(io.uop.asUInt, ALUOp.ADD, Seq(
    // R-Type operations
    uopADD.asUInt   -> ALUOp.ADD,
    uopSUB.asUInt   -> ALUOp.SUB,
    uopSLL.asUInt   -> ALUOp.SLL,
    uopSLT.asUInt   -> ALUOp.SLT,
    uopSLTU.asUInt  -> ALUOp.SLTU,
    uopXOR.asUInt   -> ALUOp.XOR,
    uopSRL.asUInt   -> ALUOp.SRL,
    uopSRA.asUInt   -> ALUOp.SRA,
    uopOR.asUInt    -> ALUOp.OR,
    uopAND.asUInt   -> ALUOp.AND,
    
    // I-Type operations (map to same ALU ops as R-type)
    uopADDI.asUInt  -> ALUOp.ADD,
    uopSLLI.asUInt  -> ALUOp.SLL,
    uopSLTI.asUInt  -> ALUOp.SLT,
    uopSLTIU.asUInt -> ALUOp.SLTU,
    uopXORI.asUInt  -> ALUOp.XOR,
    uopSRLI.asUInt  -> ALUOp.SRL,
    uopSRAI.asUInt  -> ALUOp.SRA,
    uopORI.asUInt   -> ALUOp.OR,
    uopANDI.asUInt  -> ALUOp.AND,
    
    // NOP - use PASSB to pass through operandB
    uopNOP.asUInt   -> ALUOp.PASSB
  ))

  // Connect ALU inputs
  //alu.io.operandA := io.operandA
  //alu.io.operandB := io.operandB
  alu.io.operandA := opA_mux
  alu.io.operandB := opB_mux
  alu.io.operation := aluOp


  //val targetBase = Mux(io.uop === uopJALR, opA_mux, io.pc) // JALR uses rs1 as base, JAL uses PC
  //io.targetAddr := targetBase + io.imm

  // Branch resolution and BTB logic
  val isBranch = (io.uop === uopBEQ) || (io.uop === uopBNE) || (io.uop === uopBLT) || 
                 (io.uop === uopBGE) || (io.uop === uopBLTU) || (io.uop === uopBGEU)
  val isJump = (io.uop === uopJAL) || (io.uop === uopJALR)

  /* // Branch decision logic (for simplicity, we only handle BEQ here as an example)
  io.takeBranch := MuxLookup(io.uop.asUInt, false.B, Seq(
    uopJAL.asUInt -> true.B, // Always take JAL
    uopJALR.asUInt -> true.B, // Always take JALR
    uopBEQ.asUInt -> (opA_mux === opB_mux), // Take branch if rs1 == rs2
    uopBNE.asUInt -> (opA_mux =/= opB_mux), // Take branch if rs1 != rs2
    uopBLT.asUInt -> (opA_mux.asSInt < opB_mux.asSInt), // Take branch if rs1 < rs2 (signed)
    uopBGE.asUInt -> (opA_mux.asSInt >= opB_mux.asSInt), // Take branch if rs1 >= rs2 (signed)
    uopBLTU.asUInt -> (opA_mux < opB_mux), // Take branch if rs1 < rs2 (unsigned)
    uopBGEU.asUInt -> (opA_mux >= opB_mux) // Take branch if rs1 >= rs2 (unsigned)
  )) */

  // Updated branch decision logic to incorporate BTB prediction
  val actualBranchTaken = MuxLookup(io.uop.asUInt, false.B, Seq(
    uopBEQ.asUInt -> (opA_mux === opB_mux), 
    uopBNE.asUInt -> (opA_mux =/= opB_mux), 
    uopBLT.asUInt -> (opA_mux.asSInt < opB_mux.asSInt), 
    uopBGE.asUInt -> (opA_mux.asSInt >= opB_mux.asSInt), 
    uopBLTU.asUInt -> (opA_mux < opB_mux), 
    uopBGEU.asUInt -> (opA_mux >= opB_mux) 
  ))

  //val linkAddr = io.pc + 4.U // Address of the next instruction (for JAL/JALR link)
  //val isJump = (io.uop === uopJAL) || (io.uop === uopJALR)

  val actualTaken = isJump || (isBranch && actualBranchTaken) // True if the instruction is a jump or a branch that is actually taken
  val branchTarget = Mux(io.uop === uopJALR, opA_mux, io.pc) + io.imm // Calculate target address based on instruction type

  // 1. Did we mispredict?
  val mispredicted = isBranch && (actualBranchTaken =/= io.predictTaken)

  // 2. Do we need to flush? 
  // We flush if we mispredicted a branch, OR if it is a Jump (Jumps are unconditionally flushed per spec)
  io.takeBranch := mispredicted || isJump

  // 3. If we predicted TAKEN but it actually wasn't, recover to PC + 4
  // Otherwise, recover to the branchTarget
  io.targetAddr := Mux(actualTaken, branchTarget, io.pc + 4.U)

  // 4. Update the BTB
  io.btbUpdate := isBranch
  io.btbUpdatePC := io.pc
  io.btbUpdateTarget := branchTarget
  io.btbMispredict := mispredicted

  // --- OUTPUT LOGIC ---
  val linkAddr = io.pc + 4.U 
  when(io.rd === 0.U) {
    io.aluResult := 0.U
  } .elsewhen(isJump) {
    io.aluResult := linkAddr 
  } .otherwise {
    io.aluResult := alu.io.aluResult  
  }

  io.outRD := io.rd
  io.outXcptInvalid := io.XcptInvalid
  io.outRegWrite := io.regWrite

  /* //Updated output logic to handle jumps and branches
  when(io.rd === 0.U) {
    io.aluResult := 0.U
  } .elsewhen(isJump) {
    io.aluResult := linkAddr // For JAL/JALR, write the return address to rd
  } .otherwise {
    io.aluResult := alu.io.aluResult  // For other instructions, write the ALU result to rd
  }

  io.outRD := io.rd
  io.outXcptInvalid := io.XcptInvalid
  io.outRegWrite := io.regWrite // Pass the regWrite signal through */

  // PERFORMANCE COUNTERS
  val branchCounter = RegInit(0.U(32.W))
  val mispredictCounter = RegInit(0.U(32.W))

  // Only increment if rd != 0 to prevent counting flushed/bubble instructions
  when(isBranch && io.uop =/= uopNOP) {
    branchCounter := branchCounter + 1.U
    when(mispredicted) {
      mispredictCounter := mispredictCounter + 1.U
    }
  }

  io.totalBranches := branchCounter
  io.totalMispredicts := mispredictCounter

}
