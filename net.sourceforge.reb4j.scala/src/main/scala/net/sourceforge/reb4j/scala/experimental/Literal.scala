package net.sourceforge.reb4j.scala.experimental

@SerialVersionUID(1L)
class Literal(val literal : String) extends Expression with Atomic
{
	lazy val escaped = Literal.escape(literal)
	override def toString = escaped
}


object Literal
{
	val needsEscape = "()[]{}.,-\\|+*?$^&:!<>="
	def escapeChar(c : Char) = if (needsEscape.contains(c)) "\\" + c else String.valueOf(c)
	def escape(literal : Seq[Char]) = 
		(literal.map(escapeChar) addString (new StringBuilder)).toString
}