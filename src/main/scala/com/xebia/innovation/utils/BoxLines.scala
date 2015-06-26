package com.xebia.innovation.utils

import Style.Style
import XPosition.XPosition
import YPosition.YPosition

object BoxLines {

  import XPosition._
  import YPosition._
  import Style._

  def str(x: X, y: Y) = char(x, y).toString

  def char(x: X, y: Y) = (x.style, y.style) match {
    case (Invisible, Invisible) ⇒ ' '
    case (Invisible, Normal)    ⇒ '│'
    case (Invisible, Bold)      ⇒ '┃'
    case (Normal, Invisible)    ⇒ '─'
    case (Bold, Invisible)      ⇒ '━'

    case (Normal, Normal) ⇒ (x.position.get, y.position.get) match {
      case (Left, Top)      ⇒ '┌'
      case (Left, Middle)   ⇒ '├'
      case (Left, Bottom)   ⇒ '└'
      case (Center, Top)    ⇒ '┬'
      case (Center, Middle) ⇒ '┼'
      case (Center, Bottom) ⇒ '┴'
      case (Right, Top)     ⇒ '┐'
      case (Right, Middle)  ⇒ '┤'
      case (Right, Bottom)  ⇒ '┘'
    }

    case (Normal, Bold) ⇒ (x.position.get, y.position.get) match {
      case (Left, Top)      ⇒ '┎'
      case (Left, Middle)   ⇒ '┠'
      case (Left, Bottom)   ⇒ '┖'
      case (Center, Top)    ⇒ '┰'
      case (Center, Middle) ⇒ '╂'
      case (Center, Bottom) ⇒ '┸'
      case (Right, Top)     ⇒ '┒'
      case (Right, Middle)  ⇒ '┨'
      case (Right, Bottom)  ⇒ '┚'
    }

    case (Bold, Normal) ⇒ (x.position.get, y.position.get) match {
      case (Left, Top)      ⇒ '┍'
      case (Left, Middle)   ⇒ '┝'
      case (Left, Bottom)   ⇒ '┕'
      case (Center, Top)    ⇒ '┯'
      case (Center, Middle) ⇒ '┿'
      case (Center, Bottom) ⇒ '┷'
      case (Right, Top)     ⇒ '┑'
      case (Right, Middle)  ⇒ '┥'
      case (Right, Bottom)  ⇒ '┙'
    }

    case (Bold, Bold) ⇒ (x.position.get, y.position.get) match {
      case (Left, Top)      ⇒ '┏'
      case (Left, Middle)   ⇒ '┣'
      case (Left, Bottom)   ⇒ '┗'
      case (Center, Top)    ⇒ '┳'
      case (Center, Middle) ⇒ '╋'
      case (Center, Bottom) ⇒ '┻'
      case (Right, Top)     ⇒ '┓'
      case (Right, Middle)  ⇒ '┫'
      case (Right, Bottom)  ⇒ '┛'
    }
  }
}

case class X(style: Style, position: Option[XPosition] = None)
case class Y(style: Style, position: Option[YPosition] = None)

object XPosition extends Enumeration {
  type XPosition = Value
  val Left, Center, Right = Value

  implicit def somePosition(pos: XPosition): Option[XPosition] = Some(pos)
}

object YPosition extends Enumeration {
  type YPosition = Value
  val Top, Middle, Bottom = Value

  implicit def somePosition(pos: YPosition): Option[YPosition] = Some(pos)
}

object Style extends Enumeration {
  type Style = Value
  val Invisible, Normal, Bold = Value
}
