package com.xebia
package inoovation

import com.xebia.innovation.utils.Style._
import com.xebia.innovation.utils.Table2D.{Separator, AbstractRow, Row}
import com.xebia.innovation.utils.YPosition._
import com.xebia.innovation.utils._
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
    ) add(sum(Columns)(c ⇒ x(n)(l)(c)) <= 1)

    /* Every number, in every column should be chosen only once */
    for (
      n ← Numbers;
      c ← Columns
    ) add(sum(Lines)(l ⇒ x(n)(l)(c)) <= 1)

    /* One number should be assigned to every (line,column) positiion */
    for (
      l ← Lines;
      c ← Columns
    ) add(sum(Numbers)(n ⇒ x(n)(l)(c)) <= 1)

    /* One number should be assigned to every (line,column) positiion */
    for (
      n ← Numbers;
      s ← List.range(0, n, squareSize);
      t ← List.range(0, n, squareSize)
    ) add(sum((s until s + squareSize), (t until t + squareSize))((i, j) ⇒ x(n)(i)(j)) <= 1)

    start()

    println(status, objectiveValue.get)

    val sol = Lines.map {l => Columns.map { c => Numbers.filter(x(_)(c)(l).value.fold(false)(_ == 1.0)) } }

    def format(sol: IndexedSeq[IndexedSeq[IndexedSeq[Int]]]) = {
      val rows = sol.map(_.grouped(squareSize).toSeq.map(_.map(_.mkString(",")).mkString(" "," "," "))).map(Row(_: _*))
      def withSep(sections: Seq[Seq[Row]]): List[AbstractRow] = sections match {
        case section :: Nil  => section.toList
        case section :: tail => section.toList ::: Separator(Normal, Middle) :: withSep(tail)
      }
      Table2D.formatTable(Separator(Bold, Top) :: withSep(rows.grouped(squareSize).toList) ::: Separator(Bold, Bottom) :: Nil)
    }
    println(format(sol))

    release()
  }

}

