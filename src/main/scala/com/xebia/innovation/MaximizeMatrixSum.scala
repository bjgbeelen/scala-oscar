package com.xebia
package inoovation

import com.xebia.innovation.utils.Style._
import com.xebia.innovation.utils.Table2D.{ Separator, AbstractRow, Row }
import com.xebia.innovation.utils.YPosition._
import com.xebia.innovation.utils._
import oscar.linprog.modeling._
import oscar.algebra._

object MaximizeMatrixSum extends App {

  optimize

  def optimize = {

    val matrixSmall: Seq[Seq[Int]] = Seq(
      Seq(7, 53, 183, 439, 863),
      Seq(497, 383, 563, 79, 973),
      Seq(287, 63, 343, 169, 583),
      Seq(627, 343, 773, 959, 943),
      Seq(767, 473, 103, 699, 303)
    )

    val matrix: Seq[Seq[Int]] = Seq(
      Seq(7, 53, 183, 439, 863, 497, 383, 563, 79, 973, 287, 63, 343, 169, 583),
      Seq(627, 343, 773, 959, 943, 767, 473, 103, 699, 303, 957, 703, 583, 639, 913),
      Seq(447, 283, 463, 29, 23, 487, 463, 993, 119, 883, 327, 493, 423, 159, 743),
      Seq(217, 623, 3, 399, 853, 407, 103, 983, 89, 463, 290, 516, 212, 462, 350),
      Seq(960, 376, 682, 962, 300, 780, 486, 502, 912, 800, 250, 346, 172, 812, 350),
      Seq(870, 456, 192, 162, 593, 473, 915, 45, 989, 873, 823, 965, 425, 329, 803),
      Seq(973, 965, 905, 919, 133, 673, 665, 235, 509, 613, 673, 815, 165, 992, 326),
      Seq(322, 148, 972, 962, 286, 255, 941, 541, 265, 323, 925, 281, 601, 95, 973),
      Seq(445, 721, 11, 525, 473, 65, 511, 164, 138, 672, 18, 428, 154, 448, 848),
      Seq(414, 456, 310, 312, 798, 104, 566, 520, 302, 248, 694, 976, 430, 392, 198),
      Seq(184, 829, 373, 181, 631, 101, 969, 613, 840, 740, 778, 458, 284, 760, 390),
      Seq(821, 461, 843, 513, 17, 901, 711, 993, 293, 157, 274, 94, 192, 156, 574),
      Seq(34, 124, 4, 878, 450, 476, 712, 914, 838, 669, 875, 299, 823, 329, 699),
      Seq(815, 559, 813, 459, 522, 788, 168, 586, 966, 232, 308, 833, 251, 631, 107),
      Seq(813, 883, 451, 509, 615, 77, 281, 613, 459, 205, 380, 274, 302, 35, 805)
    )

    val matrixLength = matrix(0).length
    val matrixHeight = matrix.length

    val Lines = 0 until matrixHeight
    val Columns = 0 until matrixLength
    implicit val mip = MIPSolver(LPSolverLib.glpk)

    mip.name = "Maximize Matrix Test"

    val x = Array.tabulate(matrixHeight, matrixLength)((l, c) ⇒ MIPIntVar("x" + (l, c), 0 to 1))

    maximize(sum(Lines, Columns) { (l, c) ⇒ x(l)(c) * matrix(l)(c) })

    /* at most one queen can be placed in each row */
    for (l ← Lines)
      add(sum(Columns)(c ⇒ x(l)(c)) <= 1)
    /* at most one queen can be placed in each column */
    for (c ← Columns)
      add(sum(Lines)(l ⇒ x(l)(c)) <= 1)

    /* at most one queen can be placed in each "/"-diagonal  upper half*/
    // for (i ← 1 until n)
    //   add(sum(0 to i)((j) ⇒ x(i - j)(j)) <= 1)

    /* at most one queen can be placed in each "/"-diagonal  lower half*/
    // for (i ← 1 until n)
    //   add(sum(i until n)((j) ⇒ x(j)(n - 1 - j + i)) <= 1)

    // /* at most one queen can be placed in each "/"-diagonal  upper half*/
    // for (i ← 0 until n)
    //   add(sum(0 until n - i)((j) ⇒ x(j)(j + i)) <= 1)

    /* at most one queen can be placed in each "/"-diagonal  lower half*/
    // for (i ← 1 until n)
    //   add(sum(0 until n - i)((j) ⇒ x(j + i)(j)) <= 1)

    start()

    println(status, objectiveValue.get)

    for (
      i ← 0 until matrixHeight;
      j ← 0 until matrixLength;
      if x(i)(j).value != Some(0.0)
    ) println(x(i)(j).name + " = " + matrix(i)(j))

    release()
  }

}

