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
		
	/**
	 * Concatenates this expression with the argument.
	 */
	@deprecated(message="then is now a reserved word in Scala 2.10; use andThen instead", since="2.1.0")
	final def `then` (right : Sequenceable) = this andThen right
		
	/**
	 * Concatenates this expression with the argument.
	 */
	@deprecated(message="then is now a reserved word in Scala 2.10; use andThen instead", since="2.1.0")
	final def `then` (right : Sequence) = this andThen right
}
