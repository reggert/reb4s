package net.sourceforge.reb4j.scala

@SerialVersionUID(1L)
class Literal private[scala] (val unescaped : String) extends Expression 
	with Alternation.Alternative
	with Sequence.Sequenceable
{
	require (unescaped != null, "unescaped is null")
	lazy val escaped = Literal.escape(unescaped)
	final override def toString = escaped
	final def + (right : Literal) = new Literal(unescaped + right.unescaped)
	final def then (right : Literal) = this + right
}


object Literal
{
	def apply(unescaped : Char) =
		new Literal(unescaped.toString()) with Quantifiable
	def apply(unescaped : String) = 
		new Literal(unescaped.toString())
	val needsEscape = "()[]{}.,-\\|+*?$^&:!<>="
	def escapeChar(c : Char) = if (needsEscape.contains(c)) "\\" + c else String.valueOf(c)
	def escape(unescaped : Seq[Char]) = 
		(unescaped.map(escapeChar) addString (new StringBuilder)).toString
}