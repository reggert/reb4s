package net.sourceforge.reb4j.scala.experimental

@SerialVersionUID(1L)
final class Alternation(val alternatives : List[Alternative]) extends Expression
{
	lazy val expression = (alternatives addString new StringBuilder).toString
	override def toString = expression
	
	def || (right : Alternation) = new Alternation(alternatives ++ right.alternatives)
	def || (right : Alternative) = new Alternation(alternatives :+ right)
}