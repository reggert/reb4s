package net.sourceforge.reb4j.scala


/**
 * Interface representing an expression that can be used as an alternative
 * in an alternation expression.
 */
trait Alternative extends Expression with Alternation.Ops
{
	override final def || (right : Alternation) = 
			new Alternation(this +: right.alternatives)
	override final def || (right : Alternative) = 
		new Alternation(List(this, right))
}