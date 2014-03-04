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
	final def andThen (right : Literal) = this ~~ right
	
	/**
	 * Concatenates this literal with the specified raw expression.
	 */
	final def andThen (right : Raw) = this ~~ right
	
	/**
	 * Concatenates this literal with the specified raw expression.
	 */
	final def andThen (right : CompoundRaw) = this ~~ right
	
	override final def boundedLength = Some(unescaped.length)
	
	override final def repetitionInvalidatesBounds : Boolean = possiblyZeroLength
	
	override final def possiblyZeroLength = unescaped.isEmpty
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
	val needsEscape = """()[]{}.,-\|+*?$^&:!<>=#""".toSet
	
	val specialEscapes = Map (
			'\n' -> """\n""",
			'\t' -> """\t""",
			'\r' -> """\r""",
			'\f' -> """\f""",
			'\u0007' -> """\a""",
			'\u001b' -> """\e""",
			'\0' -> """\00"""
		)
		
	/**
	 * Helper function to escape the specified character (if necessary).
	 */
	def escapeChar(c : Char) = 
		if (needsEscape(c)) 
			"\\" + c
		else 
			specialEscapes.get(c) getOrElse c.toString
	
	/**
	 * Helper function to escape the specified string (if necessary).
	 */
	def escape(unescaped : Seq[Char]) = unescaped map escapeChar mkString
}
