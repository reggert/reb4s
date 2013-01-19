package net.sourceforge.reb4j.scala


/**
 * Implicit functions to simply the construction of expressions.
 * 
 * The functions may be imported en masse by having the trait or class
 * that uses it extend it with <code>extends</code> or <code>with</code>
 * as appropriate, or may be imported selectively or as a group by
 * using <code>import</code> on the companion object.
 */
trait Implicits
{
	/**
	 * Implicit function that wraps an expression in a non-capturing group 
	 * when necessary to use the expression in a context in which it could
	 * not otherwise be used.
	 */
	implicit def wrapInParens(e : Expression) = Group.NonCapturing(e)
	
	/**
	 * Implicit function that converts a character to a [[net.sourceforge.reb4j.scala.Literal]].
	 */
	implicit def char2Literal(c : Char) = Literal(c)
	
	/**
	 * Implicit function that converts a string to a [[net.sourceforge.reb4j.scala.Literal]].
	 */
	implicit def string2Literal(s : String) = Literal(s)
}

/**
 * Companion object to [[Implicits]] that allows the implicit
 * functions to be selectively imported. 
 */
object Implicits extends Implicits
