package io.github.reggert.reb4s

/**
 * Interface implemented by expressions that may be used as 
 * sub-expressions in sequences.
 */
trait Sequenceable extends Expression
{
	/**
	 * Concatenates this expression with the argument.
	 */
	def ~~ (right : Sequenceable) = new Sequence(List(this, right))
		
	/**
	 * Concatenates this expression with the argument.
	 */
	def ~~ (right : Sequence) = new Sequence(this::right.components)
		
	/**
	 * Concatenates this expression with the argument.
	 */
	final def andThen (right : Sequenceable) : Sequence = this ~~ right
		
	/**
	 * Concatenates this expression with the argument.
	 */
	final def andThen (right : Sequence) : Sequence = this ~~ right
}
