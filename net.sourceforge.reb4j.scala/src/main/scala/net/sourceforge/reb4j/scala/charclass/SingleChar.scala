package net.sourceforge.reb4j.scala.charclass
import net.sourceforge.reb4j.scala.Alternative

case class SingleChar(val char : Char) extends CharClass with WrappedNegation
{
	def || (right : SingleChar) = new MultiChar(Set(char, right.char))
	def || (right : MultiChar) = new MultiChar(right.chars + char)
	override def || (right : Alternative) = right match
	{
		case rhs : SingleChar => this || rhs
		case rhs : MultiChar => this || rhs
		case _ => super.||(right)
	}
}