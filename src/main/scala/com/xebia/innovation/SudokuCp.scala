package com.xebia.innovation

import oscar.cp.core._
import oscar.cp.modeling.{add, start, _}

object SudokuCp extends CPModel with App {

  val Block = 3
  val Size = Block * Block
  val Lines = 0 until Size
  val Blocks = 0 until Block
  val Columns = 0 until Size
  val Numbers = 1 to Size

  val x = Array.tabulate(Size, Size)((l, c) ⇒ CPIntVar(Numbers, "x" + (l, c)))

  onSolution {
    println("------ Solution ------")
    for (
      i ← 0 until Size
    ) {
      for (j <- 0 until Size)
        print(x(i)(j).value + " ")
      println()
    }
    println()
  }

  for (
    l ← Lines
  ) add(allDifferent(Columns.map(c ⇒ x(l)(c))))

  for (
    l ← Columns
  ) add(allDifferent(Lines.map(c ⇒ x(l)(c))))

  for (
    bl <- Blocks;
    bc <- Blocks
  ) add(allDifferent(
    for (bl1 <- Blocks;
         bc1 <- Blocks) yield x(bl * Block + bl1)(bc * Block + bc1)
  ))

  search(binaryFirstFail(x.flatMap(a => a).toSeq))

  val stats = start()

  println(stats)
}

