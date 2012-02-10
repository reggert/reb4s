package net.sourceforge.reb4j.scala

/**
 * Expression that matches a series of sub-expressions appearing one after 
 * another.
 */
@SerialVersionUID(1L)
final class Sequence private[scala] (val components : List[Sequence.Sequenceable]) 
	extends Expression 
	with Alternation.Alternative
	with Sequence.Ops
{
	import Sequence.Sequenceable
	lazy val expression = components.mkString
	
	override def + (right : Sequenceable) : Sequence = new Sequence(components :+ right)
	override def + (right : Sequence) = 
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
	 * Operations related to sequences.
	 */
	trait Ops extends Expression
	{
		/**
		 * Concatenates this expression with the argument.
		 */
		def + (right : Sequenceable) : Sequence
		
		/**
		 * Concatenates this expression with the argument.
		 */
		def + (right : Sequence) : Sequence
		
		/**
		 * Concatenates this expression with the argument.
		 */
		final def then (right : Sequenceable) = this + right
		
		/**
		 * Concatenates this expression with the argument.
		 */
		final def then (right : Sequence) = this + right
	}
	
	/**
	 * Interface implemented by expressions that may be used as 
	 * sub-expressions in sequences.
	 */
	trait Sequenceable extends Expression with Ops
	{
		final override def + (right : Sequenceable) = new Sequence(List(this, right))
		final override def + (right : Sequence) = new Sequence(this::right.components)
	}
}