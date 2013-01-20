package net.sourceforge.reb4j.scala

/**
 * Expression that matches a series of sub-expressions appearing one after 
 * another.
 */
@SerialVersionUID(1L)
final class Sequence private[scala] (val components : List[Sequence.Sequenceable]) 
	extends Expression 
	with Alternation.Alternative
	with Sequence.Sequenceable
{
	import Sequence.Sequenceable
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


/**
 * Module containing traits for working with sequences.
 */
object Sequence
{
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
		final def then (right : Sequenceable) = this andThen right
		
		/**
		 * Concatenates this expression with the argument.
		 */
		@deprecated(message="then is now a reserved word in Scala 2.10; use andThen instead", since="2.1.0")
		final def then (right : Sequence) = this andThen right
	}
}