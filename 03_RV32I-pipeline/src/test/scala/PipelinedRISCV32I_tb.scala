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

  // Test 9: Full ISA Control Flow (All Branches & Jumps)
  "Test9_AllBranches" should "correctly execute all branch and jump types" in {
    test(new PipelinedRV32I("src/test/programs/BinaryFile_all_branches")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      
      // Disable timeout just in case it takes a few extra cycles to flush
      dut.clock.setTimeout(0)
      
      /* 0x00:  00500093    addi x1, x0, 5      // x1 = 5
      0x04:  00a00113    addi x2, x0, 10     // x2 = 10
      0x08:  00108463    beq x1, x1, 8       // 5 == 5 (True). Taken -> jumps to 0x10
      0x0C:  00000e63    beq x0, x0, 28      // [TRAP] Flush failed! Jumps to FAIL (0x28)
      0x10:  00209463    bne x1, x2, 8       // 5 != 10 (True). Taken -> jumps to 0x18
      0x14:  00000a63    beq x0, x0, 20      // [TRAP] Flush failed! Jumps to FAIL (0x28)
      0x18:  0020c463    blt x1, x2, 8       // 5 < 10 (True). Taken -> jumps to 0x20
      0x1C:  00000663    beq x0, x0, 12      // [TRAP] Flush failed! Jumps to FAIL (0x28)
      0x20:  05800513    addi x10, x0, 88    // Load SUCCESS CODE (88) into x10
      0x24:  fe000ee3    beq x0, x0, -4      // Infinite Loop: Jump back to 0x20
      0x28:  00100513    addi x10, x0, 1     // Load FAIL CODE (1) into x10
      0x2C:  fe000ee3    beq x0, x0, -4      // Infinite Loop: Jump back to 0x28 */
      
      // Step enough clock cycles to traverse the entire gauntlet
      // 12 instructions + 5-stage pipeline fill + branch flush penalties
      dut.clock.step(80) 
      
      // Check the final result. 
      // 88 = Success (passed all tests and bypassed all traps)
      // 1 = Failure (took a wrong path and hit a trap)
      dut.io.result.expect(88.U)
      
      println("Test 9: All Branches and Jumps Test - PASSED")
    }
  }

  // Test 10: Control Hazards - Branching and Flushing
  "Test10_Branching" should "execute loops and flush pipeline correctly" in {
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

      println("Test 10: Control Hazards (Branch Loop) - PASSED")
    }
  }

  // Test 11: Forwarding Complex - Different Operands (Simultaneous Forwarding)
  /*
       ASM CODE FOR: src/test/programs/Binary_file_forwarding_complex
       
         Hex       | Assembly          | Action
      -------------------------------------------------------------------------
       * 00A00093  | addi x1, x0, 10   | x1 = 10
       * 01400113  | addi x2, x0, 20   | x2 = 20
       * 002081B3  | add x3, x1, x2    | x3 = 30
       * 00118233  | add x4, x3, x1    | x4 = 40 (Forwards x3 from EX/MEM to rs1)
       * 003202B3  | add x5, x4, x3    | DOUBLE HAZARD: x5 = 70. 
       * 00000013  | nop               | Requires x4 forwarded from EX/MEM (rs1) AND
       * 00000013  | nop               | x3 forwarded from MEM/WB (rs2) simultaneously!
  */
  "Test11_Forwarding_Complex" should "forward to both rs1 and rs2 simultaneously from different stages" in {
    test(new PipelinedRV32I("src/test/programs/Binary_file_forwarding_complex")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      
      dut.clock.setTimeout(0)
      
      // Fast forward to first Writeback
      dut.clock.step(5)
      dut.io.result.expect(10.U) // addi x1, x0, 10
      dut.io.exception.expect(false.B)

      dut.clock.step(1)
      dut.io.result.expect(20.U) // addi x2, x0, 20
      dut.io.exception.expect(false.B)

      dut.clock.step(1)
      dut.io.result.expect(30.U) // add x3, x1, x2  (30)
      dut.io.exception.expect(false.B)

      dut.clock.step(1)
      dut.io.result.expect(40.U) // add x4, x3, x1  (40 - forwards x3 from EX/MEM to rs1)
      dut.io.exception.expect(false.B)

      dut.clock.step(1)
      // add x5, x4, x3
      // Forwards x4 from EX/MEM to rs1 AND forwards x3 from MEM/WB to rs2 simultaneously
      dut.io.result.expect(70.U) 
      dut.io.exception.expect(false.B)
      
      println("Test 11: Complex Forwarding (Different Operands) - PASSED")
    }
  }

  // Test 12: Forwarding Barriers - Delayed Hazards
  /*
       * ASM CODE FOR: src/test/programs/Binary_file_forwarding_barrier
       *
       * Hex       | Assembly          | Action / Hazard
       * -------------------------------------------------------------------------
       * 00500093  | addi x1, x0, 5    | x1 = 5
       * 00000013  | nop               | 1-cycle gap
       * 00000013  | nop               | 2-cycle gap
       * 00A08113  | addi x2, x1, 10   | DELAYED HAZARD: x2 = 15.
       * 00000013  | nop               | Because of the NOPs, the new x1 value is sitting 
       * 00000013  | nop               | in the MEM/WB barrier, not the EX/MEM barrier.
       * 001101B3  | add x3, x2, x1    | x3 = 20 (Safely reads from Register File)
       * 00000013  | nop               | padding
       */
  "Test12_Forwarding_With_Barriers" should "forward correctly across different pipeline barriers (MEM stage)" in {
    test(new PipelinedRV32I("src/test/programs/Binary_file_forwarding_barrier")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      
      dut.clock.setTimeout(0)
      
      dut.clock.step(5)
      dut.io.result.expect(5.U) // addi x1, x0, 5
      dut.io.exception.expect(false.B)

      dut.clock.step(1)
      dut.io.result.expect(0.U) // NOP
      
      dut.clock.step(1)
      dut.io.result.expect(0.U) // NOP
      
      dut.clock.step(1)
      // addi x2, x1, 10
      // Because of the NOPs, x1 is sitting further down the pipeline.
      // The forwarding unit must grab it from the MEM/WB barrier, not the EX/MEM barrier.
      dut.io.result.expect(15.U) 
      dut.io.exception.expect(false.B)

      dut.clock.step(1)
      dut.io.result.expect(0.U) // NOP
      
      dut.clock.step(1)
      dut.io.result.expect(0.U) // NOP

      dut.clock.step(1)
      // add x3, x2, x1 
      // Safe read from register file, confirms data wasn't corrupted by forwarding logic
      dut.io.result.expect(20.U) 
      dut.io.exception.expect(false.B)
      
      println("Test 12: Barrier Forwarding (Delayed Hazards) - PASSED")
    }
  }
  // Test 13: Pipeline Flush Functionality
  /*
       * ASM CODE FOR: src/test/programs/Binary_file_flush_test
       * Hex       | Assembly          | Action / Hazard
       * -------------------------------------------------------------------------
       * 00100093  | addi x1, x0, 1    | x1 = 1
       * 00108463  | beq x1, x1, 8     | 1 == 1 (True). Jumps to PC+8, skipping the next line.
       * 3E700113  | addi x2, x0, 999  | Speculatively fetched into ID
       * 05800513  | addi x10, x0, 88  | TARGET REACHED: x10 = 88. 
       * 00000013  | nop               | If flush fails, x2 becomes 999 and ruins the pipeline.
       * 00000013  | nop               | If flush succeeds, the poison pill becomes a NOP.
       */
  "Test13_Flush" should "squash the speculatively fetched instruction in the delay slot" in {
    test(new PipelinedRV32I("src/test/programs/Binary_file_flush_test")).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>

      dut.clock.setTimeout(0)
      
      // Fast forward to the first Writeback
      dut.clock.step(5)
      dut.io.result.expect(1.U) // addi x1, x0, 1
      dut.io.exception.expect(false.B)

      // Step forward 4 cycles to allow the fetched '88' instruction 
      // to travel from the IF stage all the way to the WB stage.
      dut.clock.step(4)
      
      // Check the output. 
      // If the flush failed, the output here would be 999!
      // Because it is 88, it proves the flush successfully squashed the poison pill.
      dut.io.result.expect(88.U) // addi x10, x0, 88
      dut.io.exception.expect(false.B)
      
      println("Test 13: Pipeline Flush Functionality - PASSED")
    }
  }
}