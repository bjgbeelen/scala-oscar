package com.xebia.innovation

import oscar.cp.core._
import oscar.cp.modeling.{ add, start, _ }

/**
 * Constraint-based solver
 */
object SudokuCp extends CPModel with App {

  val Block = 4
  val Size = Block * Block
  val Lines = 0 until Size
  val Blocks = 0 until Block
  val Columns = 0 until Size
  val Numbers = 0 until Size

  val x = Array.tabulate(Size, Size)((l, c) ⇒ CPIntVar(Numbers, "x" + (l, c)))

  // Each line should have distinct numbers
  for (
    l ← Lines
  ) add(allDifferent(Columns.map(c ⇒ x(l)(c))))

  // Each column should have distinct numbers
  for (
    c ← Columns
  ) add(allDifferent(Lines.map(l ⇒ x(l)(c))))

  // Each block should have distinct numbers
  for (
    bl ← Blocks;
    bc ← Blocks
  ) add(allDifferent(
    for (
      bl1 ← Blocks;
      bc1 ← Blocks
    ) yield x(bl * Block + bl1)(bc * Block + bc1)
  ))

  search(binaryFirstFail(x.flatten.toSeq))

  val stats = start()

  println(stats)

  onSolution {
    println("------ Solution ------")
    for (
      i ← 0 until Size
    ) {
      for (j ← 0 until Size) {
        print(Integer.toHexString(x(i)(j).value).toUpperCase + " ")
        if (j % Block == Block - 1) print(" ")
      }
      println()
      if (i % Block == Block - 1) println()
    }
  }
}

