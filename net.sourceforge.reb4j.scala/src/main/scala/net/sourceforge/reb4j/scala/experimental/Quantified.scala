package net.sourceforge.reb4j.scala.experimental

class Quantified private[experimental] (val base : Quantifiable, val quantifier : String) 
	extends Expression
{
	lazy val expression = base.toString() + quantifier
	override def toString = expression
}