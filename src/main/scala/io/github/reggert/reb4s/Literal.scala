package io.github.reggert.reb4s

import scala.language.postfixOps

/**
 * Expression that matches a specific string.
 */
@SerialVersionUID(1L)
sealed abstract class Literal private[reb4s] extends Expression 
	with Alternative
	with Sequenceable
{
	import Literal.StringLiteral
	
	/**
	 * The literal string to be matched.
	 */
	val unescaped : String
	
	/**
	 * The string in escaped form.
	 */
	lazy val escaped = Literal.escape(unescaped)
	override def expression = escaped
	
	/**
	 * Concatenates this literal with the argument.
	 */
	final def ~~ (right : Literal) = StringLiteral(unescaped + right.unescaped)
	
	/**
	 * Concatenates this literal with the specified raw expression.
	 */
	final def ~~ (right : Raw) = EscapedLiteral(this) ~~ right
	
	/**
	 * Concatenates this literal with the specified raw expression.
	 */
	final def ~~ (right : CompoundRaw) = EscapedLiteral(this) ~~ right
	
	
	/**
	 * Concatenates this literal with the argument.
	 */
	@deprecated(message="Not consistent with API of Sequence; use ~~ instead", since="2.1.0")
	final def + (right : Literal) = StringLiteral(unescaped + right.unescaped)
	
	/**
	 * Concatenates this literal with the argument.
	 */
	@deprecated(message="then is now a reserved word in Scala 2.10; use + instead", since="2.1.0")
	final def `then` (right : Literal) = this + right
	
	/**
	 * Concatenates this literal with the specified raw expression.
	 */
	@deprecated(message="Not consistent with API of Sequence; use ~~ instead", since="2.1.0")
	final def + (right : Raw) = EscapedLiteral(this) + right
	
	/**
	 * Concatenates this literal with the specified raw expression.
	 */
	@deprecated(message="Not consistent with API of Sequence; use ~~ instead", since="2.1.0")
	final def + (right : CompoundRaw) = EscapedLiteral(this) + right
	
	/**
	 * Concatenates this literal with the specified raw expression.
	 */
	@deprecated(message="then is now a reserved word in Scala 2.10; use + instead", since="2.1.0")
	final def `then` (right : Raw) = this + right
}


/**
 * Constructs a literal expression.
 */
object Literal
{
	/**
	 * Expression that matches a specific string.
	 */
	final case class StringLiteral(unescaped : String) extends Literal
	{
		require (unescaped != null, "unescaped is null")
	}
	
	/**
	 * Expression that matches a specific character.
	 */
	final case class CharLiteral(unescapedChar : Char) extends Literal 
		with Quantifiable
	{
		override val unescaped = unescapedChar.toString
	}
	
	/**
	 * Constructs a literal from a character.
	 */
	def apply(unescaped : Char) = CharLiteral(unescaped)
	
	/**
	 * Constructs a literal from a string.
	 */
	def apply(unescaped : String) = 
		if (unescaped.length() == 1) CharLiteral(unescaped(0))
		else StringLiteral(unescaped)
	
	/**
	 * Characters that need to be escaped in expressions.
	 */
	val needsEscape = "()[]{}.,-\\|+*?$^&:!<>=".toSet
		
	/**
	 * Helper function to escape the specified character (if necessary).
	 */
	def escapeChar(c : Char) = if (needsEscape.contains(c)) "\\" + c else String.valueOf(c)
	
	/**
	 * Helper function to escape the specified string (if necessary).
	 */
	def escape(unescaped : Seq[Char]) = unescaped map escapeChar mkString
}
