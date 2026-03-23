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

  // ========== NEW TESTS ==========

  // Test 4: I-Type Arithmetic Instructions
  "Test4_IType_Arithmetic" should "execute I-type arithmetic operations and does not write to zero" in {
    test(new PipelinedRV32I("src/test/programs/Binary_file_itype_arith")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      
      dut.clock.setTimeout(0)
      dut.clock.step(5)
      
      // addi x1, x0, 100
      dut.io.result.expect(100.U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) // NOPs

      // addi x2, x1, 50 (100 + 50 = 150)
      dut.clock.step(1)
      dut.io.result.expect(150.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3) // NOPs
      
      // addi x3, x2, -30 (150 - 30 = 120)
      dut.clock.step(1)
      dut.io.result.expect(120.U)
      dut.io.exception.expect(false.B)
      
      dut.clock.step(3) 

      // addi x4, x0, -1 (test negative immediate)
      dut.clock.step(1)
      dut.io.result.expect("hFFFFFFFF".U)
      dut.io.exception.expect(false.B)
      
      // addi x5, x0, 2047 (max positive immediate for 12-bit)
      dut.clock.step(1)
      dut.io.result.expect(2047.U)
      dut.io.exception.expect(false.B)
      
      // addi x6, x0, -2048 (min negative immediate for 12-bit)
      dut.clock.step(1)
      dut.io.result.expect("hFFFFF800".U)
      dut.io.exception.expect(false.B)

      // addi x0, x0, 100 (should not change x0)
      dut.clock.step(1)
      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      
      // add x1, x0, x0 (should be 0)
      dut.clock.step(1)
      dut.io.result.expect(0.U)
      dut.io.exception.expect(false.B)
      
      println("Test 4: I-Type Arithmetic Instructions with writing to x0 - PASSED")
    }
  }

  // Test 5: I-Type Logical Instructions
  "Test5_IType_Logical" should "execute I-type logical operations correctly" in {
    test(new PipelinedRV32I("src/test/programs/Binary_file_itype_logical")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      
      dut.clock.setTimeout(0)
      dut.clock.step(5)
      
      // addi x1, x0, 0xFF
      dut.io.result.expect(0xFF.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3) 
      
      // xori x2, x1, 0x0F (0xFF ^ 0x0F = 0xF0)
      dut.clock.step(1)
      dut.io.result.expect(0xF0.U)
      dut.io.exception.expect(false.B)
      
      // ori x3, x1, 0x100 (0xFF | 0x100 = 0x1FF)
      dut.clock.step(1)
      dut.io.result.expect(0x1FF.U)
      dut.io.exception.expect(false.B)
      
      // andi x4, x1, 0x0F (0xFF & 0x0F = 0x0F)
      dut.clock.step(1)
      dut.io.result.expect(0x0F.U)
      dut.io.exception.expect(false.B)
      
      // xori x6, x1, -1 (bitwise NOT: 0xFF ^ 0xFFF = 0xAAA)
      dut.clock.step(1)
      dut.io.result.expect("hFFFFFF00".U)
      dut.io.exception.expect(false.B)
      
      println("Test 5: I-Type Logical Instructions - PASSED")
    }
  }

  // Test 6: I-Type Shift Instructions
  "Test6_IType_Shifts" should "execute I-type shift operations correctly" in {
    test(new PipelinedRV32I("src/test/programs/Binary_file_itype_shift")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      
      dut.clock.setTimeout(0)
      dut.clock.step(5)
      
      // addi x1, x0, 0x80
      dut.io.result.expect(0x80.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)
      
      // slli x2, x1, 4 (0x80 << 4 = 0x800)
      dut.clock.step(1)
      dut.io.result.expect(0x800.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)
      
      // srli x3, x2, 8 (0x800 >> 8 = 0x8)
      dut.clock.step(1)
      dut.io.result.expect(0x8.U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)
      
      // addi x4, x0, -256 (0xFFFFFF00)
      dut.clock.step(1)
      dut.io.result.expect("hFFFFFF00".U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)
      
      // srai x5, x4, 4 (arithmetic right shift preserves sign)
      dut.clock.step(1)
      dut.io.result.expect("hFFFFFFF0".U)
      dut.io.exception.expect(false.B)

      dut.clock.step(3)
      
      // srli x6, x4, 4 (logical right shift)
      dut.clock.step(1)
      dut.io.result.expect(0x0FFFFFF0.U)
      dut.io.exception.expect(false.B)
      
      println("Test 6: I-Type Shift Instructions - PASSED")
    }
  }

  // Test 7: Data Hazard - RAW (Read After Write)
  "Test7_Hazard_RAW" should "handle data hazards with forwarding/stalling (must Pass now)" in {
    test(new PipelinedRV32I("src/test/programs/Binary_file_hazard_raw")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      
      // dut.clock.setTimeout(0)
      // dut.clock.step(5)
      
      // addi x1, x0, 10
      // dut.io.result.expect(10.U)
      // dut.io.exception.expect(false.B)
      
      // addi x2, x1, 5 - RAW
      // dut.clock.step(1)
      // dut.io.result.expect(15.U)
      // dut.io.exception.expect(false.B)

      dut.clock.setTimeout(0)
      
      // Get first instruction to WB
      dut.clock.step(5) 
      dut.io.result.expect(10.U) // x1 = 10
      
      // Step once to move the second instruction into WB
      dut.clock.step(1)
      
      // NOW check the result
      dut.io.result.expect(15.U) // x2 = 10 + 5 (Forwarded!)
      dut.io.exception.expect(false.B)

      
      println("Test 7: RAW Hazard Detection - PASSED")
    }
  }

  // Test 8: Data Hazard - WAW/WAR (Read After Write)
  "Test8_Hazard_WAW_WAR" should "handle data hazards with forwarding/stalling (must Pass now)" in {
    test(new PipelinedRV32I("src/test/programs/Binary_file_hazard_waw")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      
      dut.clock.setTimeout(0)
      dut.clock.step(5)
      
      // addi x1, x0, 10
      dut.io.result.expect(10.U)
      dut.io.exception.expect(false.B)
      
      // // addi x2, x1, 5 - RAW
      dut.clock.step(1)
      dut.io.result.expect(15.U)
      dut.io.exception.expect(false.B)

      // addi x1, x0, 20 - WAR
      dut.clock.step(1)
      dut.io.result.expect(20.U)
      dut.io.exception.expect(false.B)

      // add x2, x0, 10 - WAW
      dut.clock.step(1)
      dut.io.result.expect(10.U)
      dut.io.exception.expect(false.B)

      
      println("Test 8: WAW/WAR Hazard Detection - PASSED")
    }
  }

  // Test 9: Control Hazards - Branching and Flushing
  "Test9_Branching" should "execute loops and flush pipeline correctly" in {
    test(new PipelinedRV32I("src/test/programs/Binary_file_branch")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      
      dut.clock.setTimeout(0)
      
      var found = false
      
      // Run for up to 500 cycles to find the result
      for (_ <- 0 until 500) {
        dut.clock.step(1)
        
        // Check if the result 100 appears on the output
        if (dut.io.result.peek().litValue == 100) {
          found = true
        }
      }
      
      // Assert that we found the success value
      assert(found, "The processor never output the value 100! (Loop might be stuck or result missed)")
      
      // FETCH PERFORMANCE COUNTERS
      val branches = dut.io.total_branches.peek().litValue.toDouble
      val mispredicts = dut.io.total_mispredicts.peek().litValue.toDouble
      val correct = branches - mispredicts
      val accuracy = if (branches > 0) (correct / branches) * 100.0 else 0.0

      println("==================================================")
      println("          BTB PERFORMANCE EVALUATION              ")
      println("==================================================")
      println(f"Total Branches Executed:  ${branches.toInt}")
      println(f"Total Mispredictions:     ${mispredicts.toInt}")
      println(f"Total Correct Predictions:${correct.toInt}")
      println(f"Prediction Accuracy:      ${accuracy}%.2f%%")
      println("==================================================")

      println("Test 9: Control Hazards (Branch Loop) - PASSED")
    }
  }
}