// ADS I Class Project
// Pipelined RISC-V Core - ID Stage
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 01/09/2026 by Tobias Jauch (@tojauch)

/*
Instruction Decode (ID) Stage: decoding and operand fetch

Extracted Fields from 32-bit Instruction (see RISC-V specification for reference):
    opcode: instruction format identifier
    funct3: selects variant within instruction format
    funct7: further specifies operation type (R-type only)
    rd: destination register address
    rs1: first source register address
    rs2: second source register address
    imm: 12-bit immediate value (I-type, sign-extended)

Register File Interfaces:
    regFileReq_A, regFileResp_A: read port for rs1 operand
    regFileReq_B, regFileResp_B: read port for rs2 operand

Internal Signals:
    Combinational decoders for instructions

Functionality:
    Decode opcode to determine instruction and identify operation (ADD, SUB, XOR, ...)
    Output: uop (operation code), rd, operandA (from rs1), operandB (rs2 or immediate)

Outputs:
    uop: micro-operation code (identifies instruction type)
    rd: destination register index
    operandA: first operand
    operandB: second operand 
    XcptInvalid: exception flag for invalid instructions
*/

package core_tile

import chisel3._
import chisel3.util._
import uopc._

// -----------------------------------------
// Decode Stage
// -----------------------------------------

class ID extends Module {
  val io = IO(new Bundle {
    // Input from IF Barrier
    val instr = Input(UInt(32.W))
    
    // Register file read port A (for rs1)
    val regFileReq_A = Output(UInt(5.W))
    val regFileResp_A = Input(UInt(32.W))
    
    // Register file read port B (for rs2)
    val regFileReq_B = Output(UInt(5.W))
    val regFileResp_B = Input(UInt(32.W))
    
    // Outputs to ID Barrier
    val uop = Output(uopc())                 // Micro-operation code (ChiselEnum)
    val rd = Output(UInt(5.W))               // Destination register
    val operandA = Output(UInt(32.W))        // First operand (from rs1)
    val operandB = Output(UInt(32.W))        // Second operand (rs2 or immediate)
    val XcptInvalid = Output(Bool())         // Invalid instruction exception
  })

  // Extract instruction fields
  val opcode = io.instr(6, 0)
  val rd = io.instr(11, 7)
  val funct3 = io.instr(14, 12)
  val rs1 = io.instr(19, 15)
  val rs2 = io.instr(24, 20)
  val funct7 = io.instr(31, 25)
  
  // Generate immediate value (I-type, sign-extended)
  val immI = Cat(Fill(20, io.instr(31)), io.instr(31, 20))
  
  // For shift instructions, only lower 5 bits are used
  val immShift = Cat(Fill(27, 0.U), io.instr(24, 20))
  
  // Detect instruction types
  val isRType = opcode === "b0110011".U  // 0x33
  val isIType = opcode === "b0010011".U  // 0x13
  
  // Check for shift immediate instructions
  val isShiftImm = isIType && (funct3 === "b001".U || funct3 === "b101".U)
  
  // Select appropriate immediate
  val immediate = Mux(isShiftImm, immShift, immI)
  
  // Determine if instruction is valid
  val isValid = isRType || isIType
  
  // Request register reads
  io.regFileReq_A := rs1
  io.regFileReq_B := rs2
  
  // Micro-operation decoding using ChiselEnum
  val uop = Wire(uopc())
  
  when(isRType) {
    // R-Type instructions
    uop := MuxLookup(funct3, uopNOP, Seq(
      "b000".U -> Mux(funct7(5), uopSUB, uopADD),  // ADD or SUB
      "b001".U -> uopSLL,    // SLL
      "b010".U -> uopSLT,    // SLT
      "b011".U -> uopSLTU,   // SLTU
      "b100".U -> uopXOR,    // XOR
      "b101".U -> Mux(funct7(5), uopSRA, uopSRL),  // SRL or SRA
      "b110".U -> uopOR,     // OR
      "b111".U -> uopAND     // AND
    ))
  }.elsewhen(isIType) {
    // I-Type instructions
    uop := MuxLookup(funct3, uopNOP, Seq(
      "b000".U -> uopADDI,   // ADDI
      "b001".U -> uopSLLI,   // SLLI
      "b010".U -> uopSLTI,   // SLTI
      "b011".U -> uopSLTIU,  // SLTIU
      "b100".U -> uopXORI,   // XORI
      "b101".U -> Mux(funct7(5), uopSRAI, uopSRLI),  // SRLI or SRAI
      "b110".U -> uopORI,    // ORI
      "b111".U -> uopANDI    // ANDI
    ))
  }.otherwise {
    uop := uopNOP  // Invalid instruction
  }
  
  // Outputs
  io.uop := uop
  io.rd := rd
  io.operandA := io.regFileResp_A  // Value from rs1
  io.operandB := Mux(isIType, immediate, io.regFileResp_B)  // Immediate or rs2 value
  io.XcptInvalid := !isValid
}