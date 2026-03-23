// BTB Testbench
/*
  This testbench verifies the functionality of the Branch Target Buffer (BTB) module according to the specifications outlined in Part d.

  Test Cases:
    1. Basic Allocation & Valid Prediction: Verify that the BTB can allocate an entry for a new PC and provide a valid target address on subsequent accesses.
    2. 2-Bit FSM State Transitions: Ensure that the 2-bit finite state machine correctly transitions between states based on mispredictions and correct predictions.
    3. LRU Replacement Policy: Confirm that when the set is full, the least recently used entry is evicted to make room for a new entry.
*/

package core_tile

import chisel3._
import chiseltest._
import org.scalatest.flatspec.AnyFlatSpec

class BTBTest extends AnyFlatSpec with ChiselScalatestTester {
  "Branch Target Buffer" should "pass all specification requirements (Part d)" in {
    test(new BTB()).withAnnotations(Seq(WriteVcdAnnotation)) { dut =>
      
      // Helper function to step and clear updates
      def stepCycle(): Unit = {
        dut.clock.step(1)
        dut.io.update.poke(false.B)
      }

      println("--- TEST 1: Basic Allocation & Valid Prediction ---")
      dut.io.PC.poke("h00000010".U) 
      dut.io.valid.expect(false.B)  

      // EX stage updates BTB (Allocation)
      dut.io.update.poke(true.B)
      dut.io.updatePC.poke("h00000010".U)
      dut.io.updateTarget.poke("h00000100".U)
      dut.io.mispredicted.poke(false.B)
      stepCycle()

      // Verify Allocation
      dut.io.PC.poke("h00000010".U)
      dut.io.valid.expect(true.B)
      dut.io.target.expect("h00000100".U)
      dut.io.predictTaken.expect(true.B) // Initialized to Weakly Taken


      println("--- TEST 2: 2-Bit FSM State Transitions ---")
      // Currently Weakly Taken.
      
      // Mispredict 1: Weak Taken -> Weak Not Taken
      dut.io.update.poke(true.B)
      dut.io.updatePC.poke("h00000010".U)
      dut.io.mispredicted.poke(true.B)
      stepCycle()
      
      // Verify the state dropped to Weak Not Taken (predicts false)
      dut.io.PC.poke("h00000010".U)
      dut.io.predictTaken.expect(false.B) 

      // Reward it (Correct prediction): Weak Not Taken -> Strong Not Taken
      dut.io.update.poke(true.B)
      dut.io.mispredicted.poke(false.B)
      stepCycle()
      
      // Verify it still predicts false (Strong Not Taken)
      dut.io.PC.poke("h00000010".U)
      dut.io.predictTaken.expect(false.B) 


      println("--- TEST 3: LRU Replacement Policy ---")
      // Map 3 PCs to the exact same Set Index. (Index is bits [4:2])
      val pc1 = "h00000000".U // Idx 0, Tag 0
      val pc2 = "h00000020".U // Idx 0, Tag 1
      val pc3 = "h00000040".U // Idx 0, Tag 2

      // Allocate PC1
      dut.io.update.poke(true.B)
      dut.io.updatePC.poke(pc1)
      stepCycle()

      // Allocate PC2 (Set is now full)
      dut.io.update.poke(true.B)
      dut.io.updatePC.poke(pc2)
      stepCycle()

      // Read PC1 to make PC2 the "Least Recently Used"
      dut.io.PC.poke(pc1)
      dut.io.valid.expect(true.B)
      stepCycle()

      // Allocate PC3 (Should evict PC2, keep PC1)
      dut.io.update.poke(true.B)
      dut.io.updatePC.poke(pc3)
      stepCycle()

      // Verify Eviction
      dut.io.PC.poke(pc1)
      dut.io.valid.expect(true.B) // PC1 survived!

      dut.io.PC.poke(pc2)
      dut.io.valid.expect(false.B) // PC2 was evicted!

      dut.io.PC.poke(pc3)
      dut.io.valid.expect(true.B) // PC3 is valid!
      
      println("BTB Unit Tests Complete: LRU, Valid Targets, and FSM Transitions verified.")
    }
  }
}