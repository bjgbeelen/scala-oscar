package com.xebia
package inoovation

import oscar.linprog.modeling._
import oscar.linprog._
import oscar.algebra._

object Main extends App {

  optimize

  def optimize = {
    val n = 40
    val Lines = 0 until n
    val Columns = 0 until n
    implicit val mip = MIPSolver(LPSolverLib.glpk)

    mip.name = "Queens Test"

    val x = Array.tabulate(n, n)((l, c) ⇒ MIPIntVar("x" + (l, c), 0 to 1))

    maximize(sum(Lines, Columns) { (l, c) ⇒ x(l)(c) })

    /* at most one queen can be placed in each row */
    for (l ← Lines)
      add(sum(Columns)(c ⇒ x(l)(c)) <= 1)
    /* at most one queen can be placed in each column */
    for (c ← Columns)
      add(sum(Lines)(l ⇒ x(l)(c)) <= 1)

    /* at most one queen can be placed in each "/"-diagonal  upper half*/
    for (i ← 1 until n)
      add(sum(0 to i)((j) ⇒ x(i - j)(j)) <= 1)

    /* at most one queen can be placed in each "/"-diagonal  lower half*/
    for (i ← 1 until n)
      add(sum(i until n)((j) ⇒ x(j)(n - 1 - j + i)) <= 1)

    /* at most one queen can be placed in each "/"-diagonal  upper half*/
    for (i ← 0 until n)
      add(sum(0 until n - i)((j) ⇒ x(j)(j + i)) <= 1)

    /* at most one queen can be placed in each "/"-diagonal  lower half*/
    for (i ← 1 until n)
      add(sum(0 until n - i)((j) ⇒ x(j + i)(j)) <= 1)

    start()

    println(status, objectiveValue.get)

    for (
      i ← 0 until n;
      j ← 0 until n;
      if x(i)(j).value != Some(0.0)
    ) println(x(i)(j).name + " = " + x(i)(j).value)

    release()
  }

}

