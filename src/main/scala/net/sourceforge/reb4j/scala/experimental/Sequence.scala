package net.sourceforge.reb4j.scala.experimental

final class Sequence private[experimental] (val components : List[Alternative]) 
	extends Expression with Alternative
{
	lazy val expression = (components addString new StringBuilder).toString
	override def toString = expression
	
	override def + (right : Sequence) = new Sequence(components ++ right.components)
	def + (right : Alternative) = new Sequence(components :+ right)
}
