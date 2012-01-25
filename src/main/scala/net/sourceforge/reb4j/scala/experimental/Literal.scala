package net.sourceforge.reb4j.scala.experimental

@SerialVersionUID(1L)
case class Literal(val literal : String) extends Expression with Alternative with Quantifiable
{
	lazy val escaped = Literal.escape(literal)
	override def toString = escaped
	override def + (right : Alternative) = right match
	{
		case Literal(rhs) => new Literal(literal + rhs)
		case _ => super.+(right)
	}
}


object Literal
{
	val needsEscape = "()[]{}.,-\\|+*?$^&:!<>="
	def escapeChar(c : Char) = if (needsEscape.contains(c)) "\\" + c else String.valueOf(c)
	def escape(literal : Seq[Char]) = 
		(literal.map(escapeChar) addString (new StringBuilder)).toString
}