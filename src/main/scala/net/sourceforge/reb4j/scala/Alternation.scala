package net.sourceforge.reb4j.scala

/**
 * Expression representing a set of alternatives that may be matched.
 */
@SerialVersionUID(1L)
final class Alternation private[scala] (val alternatives : List[Alternative]) 
	extends Expression
	with Alternation.Ops
{
	override lazy val expression = alternatives.mkString("|")
	
	override def || (right : Alternation) = new Alternation(alternatives ++ right.alternatives)
	override def || (right : Alternative) = new Alternation(alternatives :+ right)
	
	override def equals (other : Any) = other match
	{
		case that : Alternation => this.alternatives == that.alternatives
		case _ => false
	}
	
	override lazy val hashCode = 31 * alternatives.hashCode
}


/**
 * Module providing the implementation of alternation.
 */
object Alternation
{
	/**
	 * Interface providing the operations that relate to alternation.
	 */
	trait Ops extends Expression
	{
		/**
		 * Constructs an expression matching either the receiver or the 
		 * any of the alternatives contained within the specified argument 
		 * expression.
		 */
		def || (right : Alternation) : Alternation
		
		/**
		 * Constructs an expression matching either the receiver or the 
		 * specified argument expression.
		 */
		def || (right : Alternative) : Alternation
		
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
}