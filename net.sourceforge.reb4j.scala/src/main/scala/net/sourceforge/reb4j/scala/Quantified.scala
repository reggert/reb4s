package net.sourceforge.reb4j.scala

@SerialVersionUID(1L)
class Quantified private[scala] (val base : Quantifiable, val quantifier : String) 
	extends Expression 
	with Alternation.Alternative
	with Sequence.Sequenceable
{
	lazy val expression = base.toString() + quantifier
	override def toString = expression
}