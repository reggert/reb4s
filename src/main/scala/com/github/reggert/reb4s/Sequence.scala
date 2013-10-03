package com.github.reggert.reb4s

/**
 * Expression that matches a series of sub-expressions appearing one after 
 * another.
 */
@SerialVersionUID(1L)
final class Sequence private[reb4s] (val components : List[Sequenceable]) 
	extends Expression 
	with Alternative
	with Sequenceable
{
	lazy val expression = components.mkString
	
	override def ~~ (right : Sequenceable) : Sequence = new Sequence(components :+ right)
	override def ~~ (right : Sequence) = 
		new Sequence(components ++ right.components)
	
	override def equals (other : Any) = other match
	{
		case that : Sequence => this.components == that.components
		case _ => false
	}
	override lazy val hashCode = 31 * components.hashCode
}
