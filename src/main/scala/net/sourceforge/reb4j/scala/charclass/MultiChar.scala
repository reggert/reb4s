package net.sourceforge.reb4j.scala.charclass
import net.sourceforge.reb4j.scala.Alternative


final case class MultiChar(val chars : Set[Char]) 
	extends BracketsRequired with WrappedNegation
{
	override def unitableForm() = 
		chars.addString(new StringBuilder).toString()
	def || (right : SingleChar) = new MultiChar(chars + right.char)
	def || (right : MultiChar) = new MultiChar(chars ++ right.chars)
	
	override def || (right : Alternative) = right match
	{
		case rhs : SingleChar => this || rhs
		case rhs : MultiChar => this || rhs
		case _ => super.|| (right)
	}
}