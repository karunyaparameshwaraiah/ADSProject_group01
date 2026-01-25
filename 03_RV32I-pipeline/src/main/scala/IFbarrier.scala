// ADS I Class Project
// Pipelined RISC-V Core - IF Barrier
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 01/09/2026 by Tobias Jauch (@tojauch)

/*
IF-Barrier: pipeline register between Fetch and Decode stages

Internal Registers:
    instrReg: holds instruction between pipeline stages, initialized to 0

Inputs:
    inInstr: fetched instruction from IF stage

Outputs:
    outInstr: instruction to ID stage

Functionality:
    Save all input signals to a register and output them in the following clock cycle
*/

package core_tile

import chisel3._

// -----------------------------------------
// IF-Barrier
// -----------------------------------------

class IFbarrier extends Module {
  val io = IO(new Bundle {
    // Input from IF stage
    val instr_in = Input(UInt(32.W))
    
    // Output to ID stage
    val instr_out = Output(UInt(32.W))
  })

    // Internal register to hold instruction
    val instrReg = RegInit(0.U(32.W))

    // On each clock cycle, update the register with the input instruction
    instrReg := io.instr_in
    
    // Output the instruction from the register
    io.instr_out := instrReg

}
