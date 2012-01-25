package net.sourceforge.reb4j.scala.experimental

@SerialVersionUID(1L)
class Quantified private[experimental] (val base : Quantifiable, val quantifier : String) 
	extends Expression with Alternative
{
	lazy val expression = base.toString() + quantifier
	override def toString = expression
}