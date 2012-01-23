package net.sourceforge.reb4j.scala.experimental

final class Alternation(val alternatives : List[Atomic]) extends Expression
{
	lazy val expression = (alternatives addString new StringBuilder).toString
	override def toString = expression
	
	def | (right : Alternation) = new Alternation(alternatives ++ right.alternatives)
	def | (right : Atomic) = new Alternation(alternatives :+ right)
}