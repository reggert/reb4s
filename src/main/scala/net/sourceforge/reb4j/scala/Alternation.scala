package net.sourceforge.reb4j.scala

@SerialVersionUID(1L)
final class Alternation private[scala] (val alternatives : List[Alternative]) 
	extends Expression
{
	lazy val expression = (alternatives addString new StringBuilder).toString
	override def toString = expression
	
	def || (right : Alternation) = new Alternation(alternatives ++ right.alternatives)
	def || (right : Alternative) = new Alternation(alternatives :+ right)
	def or (right : Alternation) = this || right
	def or (right : Alternative) = this || right
}