package net.sourceforge.reb4j.scala


trait Implicits
{
	implicit def wrapInParens(e : Expression) = Group.NonCapturing(e)
	implicit def char2Literal(c : Char) = Literal(c)
	implicit def string2Literal(s : String) = Literal(s)
}

object Implicits extends Implicits
