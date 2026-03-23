// Branch Target Buffer (BTB)
// 2-Way Set-Associative, 8 Sets, LRU Replacement

package core_tile

import chisel3._
import chisel3.util._

class BTB extends Module {
  val io = IO(new Bundle {
    // Inputs from IF Stage (Read Port)
    val PC = Input(UInt(32.W))
    
    // Inputs from EX Stage (Write/Update Port)
    val update = Input(Bool())
    val updatePC = Input(UInt(32.W))
    val updateTarget = Input(UInt(32.W))
    val mispredicted = Input(Bool())

    // Outputs to IF Stage
    val valid = Output(Bool())
    val target = Output(UInt(32.W))
    val predictTaken = Output(Bool())
  })

  // -----------------------------------------
  // FSM States for 2-Bit Predictor
  // -----------------------------------------
  val strongNotTaken = 0.U(2.W)
  val weakNotTaken   = 1.U(2.W)
  val weakTaken      = 2.U(2.W)
  val strongTaken    = 3.U(2.W)

  // -----------------------------------------
  // BTB Data Structures
  // -----------------------------------------
  class BTBEntry extends Bundle {
    val valid  = Bool()
    val tag    = UInt(27.W)
    val target = UInt(32.W)
    val state  = UInt(2.W)
  }

  // 8 sets, 2 ways per set. Initialized to 0.
  val btb = RegInit(VecInit(Seq.fill(8)(VecInit(Seq.fill(2)(0.U.asTypeOf(new BTBEntry()))))))
  
  // LRU Tracking: 1 bit per set. 
  // 0 means Way 0 is the Least Recently Used (evict Way 0)
  // 1 means Way 1 is the Least Recently Used (evict Way 1)
  val lru = RegInit(VecInit(Seq.fill(8)(0.U(1.W))))

  // -----------------------------------------
  // Read Logic (IF Stage)
  // -----------------------------------------
  val readIdx = io.PC(4, 2) // RISC-V instructions are 32-bit (4 bytes), addresses always end in 00 in binary. Therefore, bits [1:0] are useless. So, we use bits 4, 3, and 2 as the index.
  val readTag = io.PC(31, 5) //The remaining upper bits [31:5] become the Tag
  
  // Simultaneously checks Way 0 and Way 1. If an entry is valid AND the tag matches our current PC, we have a Hit.  

  val hitWay0 = btb(readIdx)(0).valid && (btb(readIdx)(0).tag === readTag)
  val hitWay1 = btb(readIdx)(1).valid && (btb(readIdx)(1).tag === readTag)

  io.valid := hitWay0 || hitWay1
  io.target := Mux(hitWay0, btb(readIdx)(0).target, btb(readIdx)(1).target)
  
  // If the state is weakTaken or strongTaken, it outputs predictTaken = true
  val readState = Mux(hitWay0, btb(readIdx)(0).state, btb(readIdx)(1).state)
  io.predictTaken := (readState === weakTaken) || (readState === strongTaken)

  // Update LRU on read (If we use Way 0, Way 1 becomes LRU, and vice versa)
  when(hitWay0) {
    lru(readIdx) := 1.U
  } .elsewhen(hitWay1) {
    lru(readIdx) := 0.U
  }

  // -----------------------------------------
  // Update Logic (EX Stage)
  // -----------------------------------------
  val updateIdx = io.updatePC(4, 2)
  val updateTag = io.updatePC(31, 5)

  val updateHitWay0 = btb(updateIdx)(0).valid && (btb(updateIdx)(0).tag === updateTag)
  val updateHitWay1 = btb(updateIdx)(1).valid && (btb(updateIdx)(1).tag === updateTag)
  val updateHit = updateHitWay0 || updateHitWay1

  when(io.update) {
    when(updateHit) {
      // --- A. UPDATE EXISTING ENTRY ---
      val wayToUpdate = Mux(updateHitWay0, 0.U, 1.U)
      val currentState = btb(updateIdx)(wayToUpdate).state
      
      // Update the target (in case the branch target changed)
      btb(updateIdx)(wayToUpdate).target := io.updateTarget

      // FSM State Transitions
      when(io.mispredicted) {
        // We guessed wrong, Move state in the opposite direction.
        when(currentState === strongTaken) { btb(updateIdx)(wayToUpdate).state := weakTaken }
        .elsewhen(currentState === weakTaken) { btb(updateIdx)(wayToUpdate).state := weakNotTaken }
        .elsewhen(currentState === weakNotTaken) { btb(updateIdx)(wayToUpdate).state := weakTaken }
        .elsewhen(currentState === strongNotTaken) { btb(updateIdx)(wayToUpdate).state := weakNotTaken }
      } .otherwise {
        // We guessed right, Strengthen the current conviction.
        when(currentState === weakTaken || currentState === strongTaken) { 
          btb(updateIdx)(wayToUpdate).state := strongTaken 
        } .elsewhen(currentState === weakNotTaken || currentState === strongNotTaken) { 
          btb(updateIdx)(wayToUpdate).state := strongNotTaken 
        }
      }

    } .otherwise {
      // --- B. ALLOCATE NEW ENTRY (CACHE MISS) ---
      val wayToEvict = lru(updateIdx)
      
      btb(updateIdx)(wayToEvict).valid  := true.B
      btb(updateIdx)(wayToEvict).tag    := updateTag
      btb(updateIdx)(wayToEvict).target := io.updateTarget
      btb(updateIdx)(wayToEvict).state  := weakTaken // Initialize to Weak Taken

      // Flip LRU bit so we don't evict this new entry next time
      lru(updateIdx) := ~wayToEvict
    }
  }
}
