// ADS I Class Project
// Pipelined RISC-V Core
//
// Chair of Electronic Design Automation, RPTU in Kaiserslautern
// File created on 01/15/2023 by Tobias Jauch (@tojauch)

package PipelinedRV32I_Tester

import chisel3._
import chiseltest._
import PipelinedRV32I._
import org.scalatest.flatspec.AnyFlatSpec

class PipelinedRISCV32ITest extends AnyFlatSpec with ChiselScalatestTester {

  "RV32I_BasicTester" should "work" in {
    test(new PipelinedRV32I("src/test/programs/BinaryFile_pipelined")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

      dut.clock.setTimeout(0)
      dut.clock.step(5)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(4.U)     // ADDI x1, x0, 4
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(5.U)     // ADDI x2, x0, 5
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(9.U)     // ADD x3, x1, x2
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(2047.U)  // ADDI x4, x0, 2047
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(16.U)    // ADDI x5, x0, 16
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(2031.U)  // SUB x6, x4, x5
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(2022.U)  // XOR x7, x6, x3
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(2047.U)  // OR x8, x6, x5
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // AND x9, x6, x5
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // ADDI x0, x0, 0
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(64704.U) // SLL x10, x7, x2
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(63.U)    // SRL x11, x7, x2
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(63.U)    // SRA x12, x7, x2
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // SLT x13, x4, x4
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // SLT x13, x4, x5
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(1.U)     // SLT x13, x5, x4
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // SLTU x13, x4, x4
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(0.U)     // SLTU x13, x4, x5
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
      dut.io.result.expect(1.U)     // SLTU x13, x5, x4
      dut.io.exception.expect(false.B)
      dut.clock.step(1)           
    }
  }

  // Test 1: Basic R-Type Instructions
  "Test1_RType_Basic" should "execute basic R-type operations correctly" in {
    test(new PipelinedRV32I("src/test/programs/Binary_file_2")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      
      dut.clock.setTimeout(0)
      dut.clock.step(5)
      
      // addi x1, x0, 10
      // dut.clock.step(1)
      dut.io.result.expect(10.U)
      dut.io.exception.expect(false.B)
      
      // addi x2, x0, 5
      dut.clock.step(1)
      dut.io.result.expect(5.U)
      dut.io.exception.expect(false.B)
      
      // add x3, x1, x2
      dut.clock.step(4)
      dut.io.result.expect(15.U)
      dut.io.exception.expect(false.B)
      
      // sub x4, x1, x2
      dut.clock.step(1)
      dut.io.result.expect(5.U)
      dut.io.exception.expect(false.B)
      
      // and x5, x1, x2
      dut.clock.step(1)
      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
    
      // or x6, x1, x2
      dut.clock.step(1)
      dut.io.result.expect(15.U)
      dut.io.exception.expect(false.B)
      
      // xor x7, x1, x2
      dut.clock.step(1)
      dut.io.result.expect(15.U)
      dut.io.exception.expect(false.B)
      
      println("Test 1: Basic R-Type Instructions - PASSED")
    }
  }

  // Test 2: Shift Instructions
  "Test2_Shifts" should "execute all shift operations correctly" in {
    test(new PipelinedRV32I("src/test/programs/Binary_file_3")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      
      dut.clock.setTimeout(0)
      dut.clock.step(5)
      
      // addi x1, x0, 8
      // dut.clock.step(1)
      dut.io.result.expect(8.U)
      dut.io.exception.expect(false.B)
      
      // addi x2, x0, 2
      dut.clock.step(1)
      dut.io.result.expect(2.U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) // NOPs
      
      // sll x3, x1, x2 (8 << 2 = 32)
      dut.clock.step(1)
      dut.io.result.expect(32.U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) // NOPs
      
      // srl x4, x1, x2 (8 >> 2 = 2)
      dut.clock.step(1)
      dut.io.result.expect(2.U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) // NOPs
      
      // slli x5, x1, 3 (8 << 3 = 64)
      dut.clock.step(1)
      dut.io.result.expect(64.U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) // NOPs
      
      // srli x6, x1, 1 (8 >> 1 = 4)
      dut.clock.step(1)
      dut.io.result.expect(4.U)
      dut.io.exception.expect(false.B)
      
      // addi x7, x0, -8
      dut.clock.step(1)
      dut.io.result.expect("hFFFFFFF8".U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) // NOPs
      
      // srai x8, x7, 1 (-8 >>> 1 = -4)
      dut.clock.step(1)
      dut.io.result.expect("hFFFFFFFC".U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) // NOPs
      
      // sra x9, x7, x2 (-8 >>> 2 = -2)
      dut.clock.step(1)
      dut.io.result.expect("hFFFFFFFE".U)
      dut.io.exception.expect(false.B)
      
      println("Test 2: Shift Instructions - PASSED")
    }
  }

  // Test 3: Comparison Instructions
  "Test3_Comparisons" should "execute comparison operations correctly" in {
    test(new PipelinedRV32I("src/test/programs/Binary_file_4")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      
      dut.clock.setTimeout(0)
      dut.clock.step(5)
      
      // addi x1, x0, 10
      // dut.clock.step(1)
      dut.io.result.expect(10.U)
      dut.io.exception.expect(false.B)
      
      // addi x2, x0, 20
      dut.clock.step(1)
      dut.io.result.expect(20.U)
      dut.io.exception.expect(false.B)
      
      // addi x3, x0, -5
      dut.clock.step(1)
      dut.io.result.expect("hFFFFFFFB".U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) // NOPs
      
      // slt x4, x1, x2 (10 < 20 = 1)
      dut.clock.step(1)
      dut.io.result.expect(1.U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) // NOPs
      
      // slt x5, x2, x1 (20 < 10 = 0)
      dut.clock.step(1)
      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) // NOPs
      
      // slt x6, x3, x1 (-5 < 10 = 1, signed)
      dut.clock.step(1)
      dut.io.result.expect(1.U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) // NOPs
      
      // sltu x7, x1, x2 (10 < 20 = 1, unsigned)
      dut.clock.step(1)
      dut.io.result.expect(1.U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) // NOPs
      
      // sltu x8, x3, x1 (-5 < 10 = 0, unsigned, -5 is large)
      dut.clock.step(1)
      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) // NOPs
      
      // slti x9, x1, 15 (10 < 15 = 1)
      dut.clock.step(1)
      dut.io.result.expect(1.U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) // NOPs
      
      // slti x10, x1, 5 (10 < 5 = 0)
      dut.clock.step(1)
      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) // NOPs
      
      // sltiu x11, x1, 15 (10 < 15 = 1, unsigned)
      dut.clock.step(1)
      dut.io.result.expect(1.U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) // NOPs
      
      // sltiu x12, x3, 10 (-5 < 10 = 0, unsigned)
      dut.clock.step(1)
      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      
      println("Test 3: Comparison Instructions - PASSED")
    }
  }
}