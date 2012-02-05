package net.sourceforge.reb4j.scala

/**
 * Raw regular expression.
 */
@SerialVersionUID(1L)
sealed class Raw private[scala] (val expression : String) 
	extends Expression 
	with Alternation.Alternative 
	with Sequence.Sequenceable
{
	/**
	 * Concatenates this expression with the argument.
	 */
	final def + (right : Raw) = new Raw(expression + right.expression)
	
	/**
	 * Concatenates this expression with the escaped form of the argument.
	 */
	final def + (right : Literal) = new Raw(expression + right.escaped)
	
	/**
	 * Concatenates this expression with the argument.
	 */
	final def then (right : Raw) = this + right
	
	/**
	 * Concatenates this expression with the escaped form of the argument.
	 */
	final def then (right : Literal) = this + right
	
	final override def equals(other : Any) = other match
	{
		case that : Raw => this.expression == that.expression
		case _ => false
	}
	override lazy val hashCode = 31 * expression.hashCode
}

/**
 * Matches any single character.
 */
object AnyChar extends Raw(".") with Quantifiable

/**
 * Matches the beginning of a line.
 */
object LineBegin extends Raw("^") with Quantifiable

/**
 * Matches the end of a line.
 */
object LineEnd extends Raw("$") with Quantifiable

/**
 * Matches a word boundary.
 */
object WordBoundary extends Raw("\\b") with Quantifiable

/**
 * Matches a non-word-boundary.
 */
object NonwordBoundary extends Raw("\\B") with Quantifiable

/**
 * Matches the beginning of input.
 */
object InputBegin extends Raw("\\A") with Quantifiable

/**
 * Matches the end of the previous match.
 */
object MatchEnd extends Raw("\\G") with Quantifiable

/**
 * Matches the end of input, skipping end-of-line markers.
 */
object InputEndSkipEOL extends Raw("\\Z") with Quantifiable

/**
 * Matches the end of input.
 */
object InputEnd extends Raw("\\z") with Quantifiable