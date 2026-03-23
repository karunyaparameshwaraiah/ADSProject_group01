// ADS I Class Project
// Pipelined RISC-V Core
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 01/15/2023 by Tobias Jauch (@tojauch)

/*
The goal of this task is to implement a 5-stage pipeline that features a subset of RV32I (all R-type and I-type instructions). 

    Instruction Memory:
        The CPU has an instruction memory (IMem) with 4096 words, each of 32 bits.
        The content of IMem is loaded from a binary file specified during the instantiation of the MultiCycleRV32Icore module.

    CPU Registers:
        The CPU has a program counter (PC) and a register file (regFile) with 32 registers, each holding a 32-bit value.
        Register x0 is hard-wired to zero.

    Microarchitectural Registers / Wires:
        Various signals are defined as either registers or wires depending on whether they need to be used in the same cycle or in a later cycle.

    Processor Stages:
        The FSM of the processor has five stages: fetch, decode, execute, memory, and writeback.
        All stages are active at the same time and process different instructions simultaneously.

        Fetch Stage:
            The instruction is fetched from the instruction memory based on the current value of the program counter (PC).

        Decode Stage:
            Instruction fields such as opcode, rd, funct3, and rs1 are extracted.
            For R-type instructions, additional fields like funct7 and rs2 are extracted.
            Control signals (isADD, isSUB, etc.) are set based on the opcode and funct3 values.
            Operands (operandA and operandB) are determined based on the instruction type.

        Execute Stage:
            Arithmetic and logic operations are performed based on the control signals and operands.
            The result is stored in the aluResult register.

        Memory Stage:
            No memory operations are implemented in this basic CPU.

        Writeback Stage:
            The result of the operation (writeBackData) is written back to the destination register (rd) in the register file.

    Check Result:
        The final result (writeBackData) is output to the io.check_res signal.
        The exception signal is also passed to the wrapper module. It indicates whether an invalid instruction has been encountered.
        In the fetch stage, a default value of 0 is assigned to io.check_res.
*/

package core_tile

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile
import Assignment02.{ALU, ALUOp}
import uopc._

class PipelinedRV32Icore(BinaryFile: String) extends Module {
  val io = IO(new Bundle {
    // Output signals for verification
    val check_res = Output(UInt(32.W))
    val exception = Output(Bool())
  })

  // ============================================================================
  // Module Instantiation
  // ============================================================================
  
  // Pipeline stages
  val ifStage = Module(new IF(BinaryFile))
  val idStage = Module(new ID())
  val exStage = Module(new EX())
  val memStage = Module(new MEM())
  val wbStage = Module(new WB())
  val forwardingUnit = Module(new ForwardingUnit()) // Instantiate Forwarding Unit
  
  // Pipeline barriers
  val ifBarrier = Module(new IFbarrier())
  val idBarrier = Module(new IDbarrier())
  val exBarrier = Module(new EXbarrier())
  val memBarrier = Module(new MEMbarrier())
  val wbBarrier = Module(new WBbarrier())
  
  // Register file
  val registerFile = Module(new regFile())

  // ============================================================================
  // Stage 1: Instruction Fetch (IF)
  // ============================================================================
  
  // IF stage outputs instruction directly
  // (Instruction memory is internal to IF stage)

  //Feedback from EX stage for branch/jump handling
  ifStage.io.takeBranch := exStage.io.takeBranch
  ifStage.io.targetAddr := exStage.io.targetAddr

  // ============================================================================
  // IF/ID Barrier
  // ============================================================================
  
  ifBarrier.io.instr_in := ifStage.io.instr
  ifBarrier.io.pc_in := ifStage.io.outPC // Pass the current PC to IF Barrier
  ifBarrier.io.flush := exStage.io.takeBranch // Flush IF/ID barrier on branch
  
  // ============================================================================
  // Stage 2: Instruction Decode (ID)
  // ============================================================================
  
  idStage.io.instr := ifBarrier.io.instr_out
  
  // Connect register file read ports to ID stage
  registerFile.io.req_1.addr := idStage.io.regFileReq_A
  idStage.io.regFileResp_A := registerFile.io.resp_1.data
  
  registerFile.io.req_2.addr := idStage.io.regFileReq_B
  idStage.io.regFileResp_B := registerFile.io.resp_2.data
  
  // ============================================================================
  // ID/EX Barrier
  // ============================================================================
  
  idBarrier.io.inUOP := idStage.io.uop
  idBarrier.io.inRD := idStage.io.rd
  idBarrier.io.inOperandA := idStage.io.operandA
  idBarrier.io.inOperandB := idStage.io.operandB
  idBarrier.io.inXcptInvalid := idStage.io.XcptInvalid
  idBarrier.io.inRS1 := idStage.io.regFileReq_A // Pass Source Reg 1 Address to ID Barrier
  idBarrier.io.inRS2 := idStage.io.regFileReq_B // Pass Source Reg 2 Address to ID Barrier
  idBarrier.io.inImm := idStage.io.imm // Pass Immediate value to ID Barrier
  idBarrier.io.inPC := ifBarrier.io.pc_out // Pass the PC from IF Barrier to ID Barrier
  idBarrier.io.flush := exStage.io.takeBranch // Flush ID/EX barrier on branch

  // Pass RegWrite flag from ID Stage to ID/EX Barrier
  idBarrier.io.inRegWrite := idStage.io.regWrite


  //===========================================================================
  // Forwarding Unit Connections
  //===========================================================================
  
  //Inputs from the EX stage (current instruction in EX stage)
  forwardingUnit.io.rs1_ex := idBarrier.io.outRS1
  forwardingUnit.io.rs2_ex := idBarrier.io.outRS2
  //Inputs from MEM stage (1 instruction ahead)
  forwardingUnit.io.rd_mem := exBarrier.io.outRD
  //forwardingUnit.io.regWrite_mem := true.B //Assume all passing instruction write
  // FIXED: Use actual RegWrite signal instead of true.B
  forwardingUnit.io.regWrite_mem := exBarrier.io.outRegWrite

  //Inputs from WB stage (2 instructions ahead)
  forwardingUnit.io.rd_wb := memBarrier.io.outRD
  //forwardingUnit.io.regWrite_wb := true.B //Assume all passing instruction write
  // FIXED: Use actual RegWrite signal instead of true.B
  forwardingUnit.io.regWrite_wb := memBarrier.io.outRegWrite
  
  // ============================================================================
  // Stage 3: Execute (EX)
  // ============================================================================
  
  exStage.io.uop := idBarrier.io.outUOP
  exStage.io.operandA := idBarrier.io.outOperandA
  exStage.io.operandB := idBarrier.io.outOperandB
  exStage.io.rd := idBarrier.io.outRD
  exStage.io.XcptInvalid := idBarrier.io.outXcptInvalid
  exStage.io.pc := idBarrier.io.outPC // Pass the PC to EX stage
  exStage.io.imm := idBarrier.io.outImm // Pass the immediate value to EX stage

  // Pass RegWrite flag into EX stage
  exStage.io.regWrite := idBarrier.io.outRegWrite

  // Connect forwarding control signals to EX stage
  exStage.io.forwardA := forwardingUnit.io.forwardA
  exStage.io.forwardB := forwardingUnit.io.forwardB

  //Forwarding data inputs (from MEM and WB stages) to EX stage
  exStage.io.dataFromWB := memBarrier.io.outAluResult // Forward ALU result from MEM stage
  exStage.io.dataFromMEM := exBarrier.io.outAluResult // Forward ALU result from EX stage (for MEM stage)
  
  // ============================================================================
  // EX/MEM Barrier
  // ============================================================================
  
  exBarrier.io.inAluResult := exStage.io.aluResult
  exBarrier.io.inRD := exStage.io.outRD
  exBarrier.io.inXcptInvalid := exStage.io.outXcptInvalid

  // Pass RegWrite flag from EX stage to EX/MEM Barrier
  exBarrier.io.inRegWrite := exStage.io.outRegWrite
  
  // ============================================================================
  // Stage 4: Memory (MEM)
  // ============================================================================
  
  // MEM stage is empty (no memory operations)
  // Data passes through via MEM barrier
  
  // ============================================================================
  // MEM/WB Barrier
  // ============================================================================
  
  memBarrier.io.inAluResult := exBarrier.io.outAluResult
  memBarrier.io.inRD := exBarrier.io.outRD
  memBarrier.io.inException := exBarrier.io.outXcptInvalid

  // Pass RegWrite flag from EX/MEM Barrier to MEM/WB Barrier
  memBarrier.io.inRegWrite := exBarrier.io.outRegWrite
  
  // ============================================================================
  // Stage 5: Writeback (WB)
  // ============================================================================
  
  wbStage.io.aluResult := memBarrier.io.outAluResult
  wbStage.io.rd := memBarrier.io.outRD

  // Pass RegWrite flag into the WB stage so it controls the register file
  wbStage.io.regWrite := memBarrier.io.outRegWrite
  
  // Connect WB stage to register file write port
  registerFile.io.req_3 <> wbStage.io.regFileReq
  
  // ============================================================================
  // WB Barrier (Final Output Register)
  // ============================================================================
  
  wbBarrier.io.inCheckRes := wbStage.io.check_res
  wbBarrier.io.inXcptInvalid := memBarrier.io.outException

  
  // ============================================================================
  // Output Signals
  // ============================================================================
  
  io.check_res := wbBarrier.io.outCheckRes
  io.exception := wbBarrier.io.outXcptInvalid 
}
