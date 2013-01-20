package net.sourceforge.reb4j.scala

/**
 * An expression to which a quantifier has been attached.
 * No further quantifier may be attached without first wrapping this expression
 * in a [[net.sourceforge.reb4j.scala.Group]] or other container.
 */
@SerialVersionUID(1L)
final class Quantified private[scala] (val base : Quantifiable, val quantifier : String) 
	extends Expression 
	with Alternation.Alternative
	with Sequenceable
{
	override lazy val expression = base.expression + quantifier
	override def equals (other : Any) = other match
	{
		case that : Quantified => 
			this.quantifier == that.quantifier &&
			this.base == that.base
		case _ => false
	}
	override lazy val hashCode = 
		31 * quantifier.hashCode + base.hashCode;
}