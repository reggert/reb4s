package net.sourceforge.reb4j.scala

/**
 * Expression that matches a specific string.
 */
@SerialVersionUID(1L)
sealed abstract class Literal private[scala] extends Expression 
	with Alternation.Alternative
	with Sequence.Sequenceable
{
	import Literal.StringLiteral
	
	val unescaped : String
	
	/**
	 * The string in escaped form.
	 */
	lazy val escaped = Literal.escape(unescaped)
	override def expression = escaped
	
	/**
	 * Concatenates this literal with the argument.
	 */
	final def + (right : Literal) = StringLiteral(unescaped + right.unescaped)
	
	/**
	 * Concatenates this literal with the argument.
	 */
	final def then (right : Literal) = this + right
	
	/**
	 * Concatenates this literal with the specified raw expression.
	 */
	final def + (right : Raw) = EscapedLiteral(this) + right
	
	/**
	 * Concatenates this literal with the specified raw expression.
	 */
	final def + (right : CompoundRaw) = EscapedLiteral(this) + right
	
	/**
	 * Concatenates this literal with the specified raw expression.
	 */
	final def then (right : Raw) = this + right
}


/**
 * Constructs a literal expression.
 */
object Literal
{
	final case class StringLiteral(unescaped : String) extends Literal
	{
		require (unescaped != null, "unescaped is null")
	}
	
	// TODO: should this be superceded by SingleChar?
	final case class CharLiteral(unescapedChar : Char) extends Literal 
		with Quantifiable
	{
		override val unescaped = unescapedChar.toString
	}
	
	
	def apply(unescaped : Char) = CharLiteral(unescaped)
	def apply(unescaped : String) = 
		if (unescaped.length() == 1) CharLiteral(unescaped(0))
		else StringLiteral(unescaped)
		
	val needsEscape = "()[]{}.,-\\|+*?$^&:!<>="
	def escapeChar(c : Char) = if (needsEscape.contains(c)) "\\" + c else String.valueOf(c)
	def escape(unescaped : Seq[Char]) = unescaped map escapeChar mkString
}