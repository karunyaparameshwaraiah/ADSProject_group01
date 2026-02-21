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
    
    //Forwarding inputs
    val forwardA = Input(UInt(2.W)) // Control signal for operandA forwarding
    val forwardB = Input(UInt(2.W)) // Control signal for operandB forwarding

    // Forwarding data inputs
    val dataFromWB = Input(UInt(32.W)) // Data forwarded from WB stage
    val dataFromMEM = Input(UInt(32.W)) // Data forwarded from MEM stage
    
    // Outputs to EX Barrier
    val aluResult = Output(UInt(32.W))
    val outRD = Output(UInt(5.W))
    val outXcptInvalid = Output(Bool())
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

  // Select Operand B
  val opB_mux = MuxCase(io.operandB, Seq(
    (io.forwardB === "b10".U) -> io.dataFromMEM,
    (io.forwardB === "b01".U) -> io.dataFromWB
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

  // Outputs
  

 when(io.rd === 0.U) {
    io.aluResult := 0.U
  } .otherwise {
    io.aluResult := alu.io.aluResult
  }
  io.outRD := io.rd

  io.outXcptInvalid := io.XcptInvalid
}
