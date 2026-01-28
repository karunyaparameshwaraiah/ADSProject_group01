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

  // ---------------------------------------------------------
  // Basic provided test
  // ---------------------------------------------------------
  "RV32I_BasicTester" should "work" in {
    test(new PipelinedRV32I("src/test/programs/BinaryFile_pipelined"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

      dut.clock.setTimeout(0)
      dut.clock.step(5)

      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(4.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(5.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(9.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(2047.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(16.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(2031.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(2022.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(2047.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(64704.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(63.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(63.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(1.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)

      dut.io.result.expect(1.U)
      dut.io.exception.expect(false.B)
      dut.clock.step(1)
    }
  }

  // ---------------------------------------------------------
  // Test 1: Basic R-Type Instructions
  // ---------------------------------------------------------
  "Test1_RType_Basic" should "execute basic R-type operations correctly" in {
    test(new PipelinedRV32I("src/test/programs/Binary_file_2"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

      dut.clock.setTimeout(0)
      dut.clock.step(5)

      dut.io.result.expect(10.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(1)
      dut.io.result.expect(5.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(4)
      dut.io.result.expect(15.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(1)
      dut.io.result.expect(5.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(1)
      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(1)
      dut.io.result.expect(15.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(1)
      dut.io.result.expect(15.U)
      dut.io.exception.expect(false.B)

      println("Test 1: Basic R-Type Instructions - PASSED")
    }
  }

  // ---------------------------------------------------------
  // Test 2: Shift Instructions
  // ---------------------------------------------------------
  "Test2_Shifts" should "execute all shift operations correctly" in {
    test(new PipelinedRV32I("src/test/programs/Binary_file_3"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

      dut.clock.setTimeout(0)
      dut.clock.step(5)

      dut.io.result.expect(8.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(1)
      dut.io.result.expect(2.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      dut.clock.step(1)
      dut.io.result.expect(32.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      dut.clock.step(1)
      dut.io.result.expect(2.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      dut.clock.step(1)
      dut.io.result.expect(64.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      dut.clock.step(1)
      dut.io.result.expect(4.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(1)
      dut.io.result.expect("hFFFFFFF8".U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      dut.clock.step(1)
      dut.io.result.expect("hFFFFFFFC".U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      dut.clock.step(1)
      dut.io.result.expect("hFFFFFFFE".U)
      dut.io.exception.expect(false.B)

      println("Test 2: Shift Instructions - PASSED")
    }
  }

  // ---------------------------------------------------------
  // Test 3: Comparison Instructions
  // ---------------------------------------------------------
  "Test3_Comparisons" should "execute comparison operations correctly" in {
    test(new PipelinedRV32I("src/test/programs/Binary_file_4"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

      dut.clock.setTimeout(0)
      dut.clock.step(5)

      dut.io.result.expect(10.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(1)
      dut.io.result.expect(20.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(1)
      dut.io.result.expect("hFFFFFFFB".U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      dut.clock.step(1)
      dut.io.result.expect(1.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      dut.clock.step(1)
      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      dut.clock.step(1)
      dut.io.result.expect(1.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      dut.clock.step(1)
      dut.io.result.expect(1.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      dut.clock.step(1)
      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      dut.clock.step(1)
      dut.io.result.expect(1.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      dut.clock.step(1)
      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      dut.clock.step(1)
      dut.io.result.expect(1.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      dut.clock.step(1)
      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)

      println("Test 3: Comparison Instructions - PASSED")
    }
  }

  // ---------------------------------------------------------
  // Test 4: x0 Register Behavior
  // ---------------------------------------------------------
  "Test4_x0_Register" should "enforce x0 always being zero" in {
    test(new PipelinedRV32I("src/test/programs/Binary_file_x0"))
      .withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

      dut.clock.setTimeout(0)
      dut.clock.step(5)

      // addi x0, x0, 123
      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(1)

      // addi x1, x0, 5
      dut.io.result.expect(5.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      // add x0, x1, x1
      dut.clock.step(1)
      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      // add x2, x0, x1
      dut.clock.step(1)
      dut.io.result.expect(5.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)

      // add x3, x0, x0
      dut.clock.step(1)
      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)

      println("Test 4: x0 Register Behavior - PASSED")
    }
  }
}