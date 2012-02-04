package net.sourceforge.reb4j.scala

@SerialVersionUID(1L)
final class Quantified private[scala] (val base : Quantifiable, val quantifier : String) 
	extends Expression 
	with Alternation.Alternative
	with Sequence.Sequenceable
{
	lazy val expression = base.toString() + quantifier
	override def toString = expression
	override def equals (other : Any) = other match
	{
		case that : Quantified => 
			this.quantifier == that.quantifier &&
			this.base == that.base
		case _ => false
	}
	override def hashCode() = 
		31 * quantifier.hashCode() + base.hashCode();
}