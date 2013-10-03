package io.github.reggert.reb4s


/**
 * Interface representing an expression that can be used as an alternative
 * in an alternation expression.
 */
trait Alternative extends Expression
{
	/**
	 * Constructs an expression matching either the receiver or the 
	 * any of the alternatives contained within the specified argument 
	 * expression.
	 */
	def || (right : Alternation) : Alternation =
		new Alternation(this +: right.alternatives)
		
	/**
	 * Constructs an expression matching either the receiver or the 
	 * specified argument expression.
	 */
	def || (right : Alternative) : Alternation =
		new Alternation(List(this, right))
		
	/**
	 * Constructs an expression matching either the receiver or the 
	 * any of the alternatives contained within the specified argument 
	 * expression.
	 */
	final def or (right : Alternation) = this || right
		
	/**
	 * Constructs an expression matching either the receiver or the 
	 * specified argument expression.
	 */
	final def or (right : Alternative) = this || right
}
