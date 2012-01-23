package net.sourceforge.reb4j.scala.experimental

final class Sequence private[experimental] (val components : List[Atomic]) 
	extends Expression with Atomic
{
	lazy val expression = (components addString new StringBuilder).toString
	override def toString = expression
	
	override def + (right : Sequence) = new Sequence(components ++ right.components)
	def + (right : Atomic) = new Sequence(components :+ right)
}
