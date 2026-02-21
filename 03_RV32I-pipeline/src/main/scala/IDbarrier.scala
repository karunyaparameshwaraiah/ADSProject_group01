// ADS I Class Project
// Pipelined RISC-V Core - ID Barrier
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 01/09/2026 by Tobias Jauch (@tojauch)

/*
ID-Barrier: pipeline register between Decode and Execute stages

Internal Registers:
    uop: micro-operation code (from uopc enum)
    rd: destination register index, initialized to 0
    operandA: first source operand, initialized to 0
    operandB: second operand/immediate, initialized to 0

Inputs:
    inUOP: micro-operation code from ID stage
    inRD: destination register from ID stage
    inOperandA: first operand from ID stage
    inOperandB: second operand/immediate from ID stage
    inXcptInvalid: exception flag from ID stage

Outputs:
    outUOP: micro-operation code to EX stage
    outRD: destination register to EX stage
    outOperandA: first operand to EX stage
    outOperandB: second operand to EX stage
    outXcptInvalid: exception flag to EX stage
Functionality:
    Save all input signals to a register and output them in the following clock cycle
*/

// IDbarrier.scala
// ID-Barrier: pipeline register between Decode and Execute stages

package core_tile

import chisel3._
import uopc._

class IDbarrier extends Module {
  val io = IO(new Bundle {
    // Inputs from ID stage
    val inUOP = Input(uopc())           // ChiselEnum type
    val inRD = Input(UInt(5.W))
    val inOperandA = Input(UInt(32.W))
    val inOperandB = Input(UInt(32.W))
    val inXcptInvalid = Input(Bool())
    val inRS1 = Input(UInt(5.W)) // Source Reg 1 Address
    val inRS2 = Input(UInt(5.W)) // Source Reg 2 Address
    
    // Outputs to EX stage
    val outUOP = Output(uopc())         // ChiselEnum type
    val outRD = Output(UInt(5.W))
    val outOperandA = Output(UInt(32.W))
    val outOperandB = Output(UInt(32.W))
    val outXcptInvalid = Output(Bool())
    val outRS1 = Output(UInt(5.W)) // Source Reg 1 Address to EX
    val outRS2 = Output(UInt(5.W)) // Source Reg 2 Address to EX
  })

  // Pipeline registers
  val uop = RegInit(uopNOP)             // Initialize to NOP (ChiselEnum)
  val rd = RegInit(0.U(5.W))
  val operandA = RegInit(0.U(32.W))
  val operandB = RegInit(0.U(32.W))
  val xcptInvalid = RegInit(false.B)
  val rs1 = RegInit(0.U(5.W))   // Register for Source Reg 1 Address
  val rs2 = RegInit(0.U(5.W))   // Register for Source Reg 2 Address

  // Update registers with inputs each cycle
  uop := io.inUOP
  rd := io.inRD
  operandA := io.inOperandA
  operandB := io.inOperandB
  xcptInvalid := io.inXcptInvalid
  rs1 := io.inRS1 // Capture Source Reg 1 Address
  rs2 := io.inRS2 // Capture Source Reg 2 Address

  // Output registered values
  io.outUOP := uop
  io.outRD := rd
  io.outOperandA := operandA
  io.outOperandB := operandB
  io.outXcptInvalid := xcptInvalid
  io.outRS1 := rs1 // Output Source Reg 1 Address to EX
  io.outRS2 := rs2 // Output Source Reg 2 Address to EX
}