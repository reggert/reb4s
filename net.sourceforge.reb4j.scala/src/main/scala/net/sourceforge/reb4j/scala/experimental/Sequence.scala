package net.sourceforge.reb4j.scala.experimental

@SerialVersionUID(1L)
final case class Sequence private[experimental] (val components : List[Alternative]) 
	extends Expression with Alternative
{
	lazy val expression = (components addString new StringBuilder).toString
	override def toString = expression
	
	override def + (right : Alternative) : Sequence = right match
	{
		case Sequence(rhs) => new Sequence(components ++ rhs)
		case _ => new Sequence(components :+ right)
	}
	override def + (right : Sequence) = 
		new Sequence(components ++ right.components)
}
