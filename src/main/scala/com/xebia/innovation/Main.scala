package com.xebia
package inoovation

import oscar.linprog.modeling._
import oscar.linprog._
import oscar.algebra._

object Main extends App {

  optimize

  def optimize = {
    val squareSize = 3
    val n = squareSize * squareSize
    val Lines = 0 until n
    val Columns = 0 until n
    val Numbers = 0 until n
    implicit val mip = MIPSolver(LPSolverLib.glpk)

    mip.name = "Hexadecimal Sudoku"

    val x = Array.tabulate(n, n, n)((number, l, c) ⇒ MIPIntVar("x" + (number, l, c), 0 to 1))

    maximize(sum(Lines, Columns, Numbers) { (number, l, c) ⇒ x(number)(l)(c) })

    /* Every number, in every line should be chosen only once */
    for (
      n ← Numbers;
      l ← Lines
    ) add(sum(Columns)(c ⇒ x(n)(l)(c)) == 1)

    /* Every number, in every column should be chosen only once */
    for (
      n ← Numbers;
      c ← Columns
    ) add(sum(Lines)(l ⇒ x(n)(l)(c)) == 1)

    /* One number should be assigned to every (line,column) positiion */
    for (
      l ← Lines;
      c ← Columns
    ) add(sum(Numbers)(n ⇒ x(n)(l)(c)) == 1)

    // /* at most one queen can be placed in each column */
    // for (c ← Columns)
    //   add(sum(Lines)(l ⇒ x(l)(c)) <= 1)

    // /* at most one queen can be placed in each "/"-diagonal  upper half*/
    // for (i ← 1 until n)
    //   add(sum(0 to i)((j) ⇒ x(i - j)(j)) <= 1)

    //  at most one queen can be placed in each "/"-diagonal  lower half
    // for (i ← 1 until n)
    //   add(sum(i until n)((j) ⇒ x(j)(n - 1 - j + i)) <= 1)

    // /* at most one queen can be placed in each "/"-diagonal  upper half*/
    // for (i ← 0 until n)
    //   add(sum(0 until n - i)((j) ⇒ x(j)(j + i)) <= 1)

    // /* at most one queen can be placed in each "/"-diagonal  lower half*/
    // for (i ← 1 until n)
    //   add(sum(0 until n - i)((j) ⇒ x(j + i)(j)) <= 1)

    start()

    println(status, objectiveValue.get)

    for (
      i ← 0 until n;
      j ← 0 until n;
      k ← 0 until n;
      if x(i)(j)(k).value != Some(0.0)
    ) println(x(i)(j)(k).name + " = " + x(i)(j)(k).value)

    release()
  }

}

