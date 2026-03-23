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
    val flush = Input(Bool()) // From IF stage
    val pc_in = Input(UInt(32.W)) // Program Counter must travel with instruction

    // Input prediction from BTB
    val inPredictTaken = Input(Bool())

    // Output to ID stage
    val instr_out = Output(UInt(32.W))
    val pc_out = Output(UInt(32.W)) // Pass the PC to ID stage

    // Output prediction to ID
    val outPredictTaken = Output(Bool())
  })

    // Internal register to hold instruction
    val instrReg = RegInit(0.U(32.W))
    val pcReg = RegInit(0.U(32.W)) // Register to hold the PC
    val predictReg = RegInit(false.B) // Register to hold the branch prediction result

    when(io.flush) {
        instrReg := "h00000013".U // NOP instruction
        pcReg := 0.U // Reset PC to 0 on flush
        predictReg := false.B // Reset prediction to false on flush
    } .otherwise {
        instrReg := io.instr_in
        pcReg := io.pc_in // Update PC register with incoming PC
        predictReg := io.inPredictTaken // Update prediction register with incoming prediction
    }

    // Output the instruction from the register
    io.instr_out := instrReg
    io.pc_out := pcReg // Output the PC to ID stage
    io.outPredictTaken := predictReg // Output the branch prediction result to ID stage

}
