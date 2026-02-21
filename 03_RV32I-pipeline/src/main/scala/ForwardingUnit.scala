package core_tile

import chisel3._
import chisel3.util._

class ForwardingUnit extends Module {
  val io = IO(new Bundle {
    // Inputs: Source Registers from the instruction currently in EX stage
    val rs1_ex = Input(UInt(5.W))
    val rs2_ex = Input(UInt(5.W))

    // Inputs: Destination Register from MEM stage (1 instruction ahead)
    val rd_mem = Input(UInt(5.W))
    val regWrite_mem = Input(Bool())

    // Inputs: Destination Register from WB stage (2 instructions ahead)
    val rd_wb = Input(UInt(5.W))
    val regWrite_wb = Input(Bool())

    // Outputs: Forwarding Control Signals
    val forwardA = Output(UInt(2.W))
    val forwardB = Output(UInt(2.W))
  })

  // Default: No forwarding
  io.forwardA := 0.U
  io.forwardB := 0.U

  // FORWARD A Logic (rs1)
  
  // Priority 1: Forward from MEM
  when (io.regWrite_mem && io.rd_mem =/= 0.U && io.rd_mem === io.rs1_ex) {
    io.forwardA := "b10".U 
  }
  // Priority 2: Forward from WB
  .elsewhen (io.regWrite_wb && io.rd_wb =/= 0.U && io.rd_wb === io.rs1_ex) {
    io.forwardA := "b01".U
  }

  // FORWARD B Logic (rs2)
  
  // Priority 1: Forward from MEM
  when (io.regWrite_mem && io.rd_mem =/= 0.U && io.rd_mem === io.rs2_ex) {
    io.forwardB := "b10".U
  }
  // Priority 2: Forward from WB
  .elsewhen (io.regWrite_wb && io.rd_wb =/= 0.U && io.rd_wb === io.rs2_ex) {
    io.forwardB := "b01".U
  }

   printf("RS1_EX: %d RS2_EX: %d | RD_MEM: %d WR_MEM: %d | ForwardA: %d ForwardB: %d\n", 
    io.rs1_ex, io.rs2_ex, io.rd_mem, io.regWrite_mem, io.forwardA, io.forwardB)



}