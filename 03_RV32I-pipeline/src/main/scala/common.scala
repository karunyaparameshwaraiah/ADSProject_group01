// ADS I Class Project
// Pipelined RISC-V Core - Common Definitions
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 01/09/2026 by Tobias Jauch (@tojauch)

/*
Global Definitions and Data Types

Enumerations:
    uopc: ChiselEnum defining micro-operation codes for all supported RV32I instructions:
        R-type instructions 
        I-type instructions
        NOP (no operation, default case)

This enum is used throughout the pipeline:
    Decode stage assigns uop based on instruction fields
    Execute stage maps uop to ALU operations
*/

package core_tile

import chisel3._
import chisel3.experimental.ChiselEnum

// -----------------------------------------
// Global Definitions and Data Types
// -----------------------------------------

object uopc extends ChiselEnum {
  // R-type instructions
  val uopADD, uopSUB, uopSLL, uopSLT, uopSLTU = Value
  val uopXOR, uopSRL, uopSRA, uopOR, uopAND = Value
  
  // I-type instructions
  val uopADDI, uopSLLI, uopSLTI, uopSLTIU = Value
  val uopXORI, uopSRLI, uopSRAI, uopORI, uopANDI = Value

  //Branch and Jump instructions
  val uopBEQ, uopBNE, uopBLT, uopBLTU, uopBGE, uopBGEU = Value
  val uopJAL, uopJALR = Value
  
  // Special
  val uopNOP = Value
}

//ToDo: Add your implementation according to the specification above here 