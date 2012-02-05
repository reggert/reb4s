package net.sourceforge.reb4j.scala

/**
 * Expression that matches a specific string.
 */
@SerialVersionUID(1L)
sealed class Literal private[scala] (val unescaped : String) extends Expression 
	with Alternation.Alternative
	with Sequence.Sequenceable
{
	require (unescaped != null, "unescaped is null")
	
	/**
	 * The string in escaped form.
	 */
	lazy val escaped = Literal.escape(unescaped)
	override def expression = escaped
	
	/**
	 * Concatenates this literal with the argument.
	 */
	final def + (right : Literal) = new Literal(unescaped + right.unescaped)
	
	/**
	 * Concatenates this literal with the argument.
	 */
	final def then (right : Literal) = this + right
	
	/**
	 * Concatenates this literal with the specified raw expression.
	 */
	final def + (right : Raw) = new Raw(escaped + right.expression)
	
	/**
	 * Concatenates this literal with the specified raw expression.
	 */
	final def then (right : Raw) = this + right
	
	override def equals(other : Any) = other match
	{
		case that : Literal => this.unescaped == that.unescaped
		case _ => false
	}
	override lazy val hashCode = 31 * unescaped.hashCode
}


/**
 * Constructs a literal expression.
 */
object Literal
{
	def apply(unescaped : Char) =
		new Literal(unescaped.toString()) with Quantifiable
	def apply(unescaped : String) = 
		if (unescaped.length() == 1)
			new Literal(unescaped) with Quantifiable
		else
			new Literal(unescaped)
	val needsEscape = "()[]{}.,-\\|+*?$^&:!<>="
	def escapeChar(c : Char) = if (needsEscape.contains(c)) "\\" + c else String.valueOf(c)
	def escape(unescaped : Seq[Char]) = 
		(unescaped.map(escapeChar) addString (new StringBuilder)).toString
}