package com.xebia.innovation

import com.xebia.innovation.utils.Style._
import com.xebia.innovation.utils.Table2D
import com.xebia.innovation.utils.Table2D.{ AbstractRow, Separator, Row }
import com.xebia.innovation.utils.YPosition._
import oscar.algebra._
import oscar.linprog.modeling._

object MaximumSum extends App {

  implicit val mip = MIPSolver(LPSolverLib.glpk)

  val N = 10

  val matrix = Array.tabulate(N, N)((l, c) ⇒ (math.random * 1000).toInt)

  println(format(matrix.toSeq.map(_.toSeq.map(_.toString))))

  val x = Array.tabulate(N, N)((l, c) ⇒ MIPIntVar(0 to 1))

  maximize(sum(0 until N, 0 until N) { (l, c) ⇒ x(l)(c) * matrix(l)(c) })

  /* Every number, in every line should be chosen only once */
  for (l ← 0 until N) add(sum(0 until N)(c ⇒ x(l)(c)).==(1))

  /* Every number, in every column should be chosen only once */
  for (c ← 0 until N) add(sum(0 until N)(l ⇒ x(l)(c)).==(1))

  start()

  val sol = (0 until N).map { l ⇒ (0 until N).map { c ⇒ x(l)(c).value.fold("")(v ⇒ if (v == 1.0) matrix(l)(c).toString else "") } }

  def format(sol: Seq[Seq[String]]) = {
    val rows: Seq[Row] = sol.map(_.toSeq).map(Row(_: _*))
    def withSep(sections: List[Row]): List[AbstractRow] = sections match {
      case section :: Nil  ⇒ section :: Nil
      case section :: tail ⇒ section :: Separator(Normal, Middle) :: withSep(tail)
    }
    Table2D.formatTable(Separator(Bold, Top) :: withSep(rows.toList) ::: Separator(Bold, Bottom) :: Nil)
  }
  println(format(sol))

  println(status, objectiveValue.get)

  release()

}
