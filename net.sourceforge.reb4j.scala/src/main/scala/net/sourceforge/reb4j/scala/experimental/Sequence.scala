package net.sourceforge.reb4j.scala.experimental

final case class Sequence private[experimental] (val components : List[Alternative]) 
	extends Expression with Alternative
{
	lazy val expression = (components addString new StringBuilder).toString
	override def toString = expression
	
	override def + (right : Alternative) = right match
	{
		case Sequence(rhs) => new Sequence(components ++ rhs)
		case _ => new Sequence(components :+ right)
	}
	override def + (right : Sequence) = 
		new Sequence(components ++ right.components)
}
