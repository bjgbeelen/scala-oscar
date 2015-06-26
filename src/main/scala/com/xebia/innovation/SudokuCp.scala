package com.xebia.innovation

import oscar.cp.core._
import oscar.cp.modeling.{add, start, _}

/**
 * Find a number consisting of 9 digits in which each of the digits from
 * 1 to 9 appears only once. This number must also satisfy these divisibility requirements:
 * 1. The number should be divisible by 9.
 * 2. If the rightmost digit is removed, the remaining number should be divisible by 8.
 * 3. If the rightmost digit of the new number is removed, the remaining number should be divisible by 7.
 * 4. And so on, until there's only one digit (which will necessarily be divisible by 1).
 *
 *
 */
object SudokuCp extends CPModel with App {

  val Block = 3
  val Size = Block * Block
  val Lines = 0 until Size
  val Blocks = 0 until Block
  val Columns = 0 until Size
  val Numbers = 1 to Size

  val x = Array.tabulate(Size, Size)((l, c) ⇒ CPIntVar(Numbers, "x" + (l, c)))

  onSolution {
    for (
      i ← 0 until Size;
      j ← 0 until Size
    ) println(x(i)(j).name + " = " + x(i)(j).value)
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

