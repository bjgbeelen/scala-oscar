package com.xebia.innovation.utils

object Table2D {

  import Style._
  import XPosition._
  import YPosition._
  import BoxLines._

  abstract class AbstractRow
  case class Separator(xStyle: Style, yPosition: YPosition) extends AbstractRow
  case class Row(cells: Any*) extends AbstractRow

  object Row {
    def apply(cells: List[Any]): Row = Row(cells.toSeq: _*)
  }

  type TABLE = Seq[AbstractRow]

  def formatTable(table: TABLE): String = formatTable(minColSizesTable(table))(table)

  def formatTable(colSizes: Seq[Int])(table: TABLE): String =
    table.map(_ match {
      case row: Row             ⇒ formatRow(colSizes)(row)
      case separator: Separator ⇒ formatSeparator(colSizes)(separator)
    }).mkString("\n")

  def minColSizesTable(table: TABLE): Seq[Int] =
    table.collect {
      case row: Row ⇒ row.cells.map(_.toString.size)
    }.transpose.map(_.max)

  def formatRow(colSizes: Seq[Int])(row: Row) = mkString(Invisible, (Bold, Normal, Bold))(None)(formatCells(colSizes)(row))

  def formatCells(colSizes: Seq[Int])(row: Row) = for ((item, size) ← row.cells.zip(colSizes)) yield formatCell(size)(item)

  def formatCell(colSize: Int)(item: Any) = if (colSize == 0) "" else ("%-" + colSize + "s").format(item)

  def formatSeparator(colSizes: Seq[Int])(separator: Separator) =
    mkString(separator.xStyle, (Bold, Normal, Bold))(separator.yPosition)(colSizes.map(str(X(separator.xStyle), Y(Invisible)) * _))

  def mkString(xStyle: Style, yStyles: (Style, Style, Style))(yPosition: Option[YPosition])(cells: Seq[String]) =
    cells.mkString(str(X(xStyle, Left), Y(yStyles._1, yPosition)), str(X(xStyle, Center), Y(yStyles._2, yPosition)), str(X(xStyle, Right), Y(yStyles._3, yPosition)))
}
