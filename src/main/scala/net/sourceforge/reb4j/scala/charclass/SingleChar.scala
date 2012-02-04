package net.sourceforge.reb4j.scala.charclass
import net.sourceforge.reb4j.scala.Literal

final class SingleChar private[charclass] (val char : Char) 
	extends CharClass 
	with WrappedNegation 
	with SelfContained
	with Union.Subset
	with Intersection.Superset
{
	override def unitableForm() = Literal.escapeChar(char)
	def || (right : SingleChar) = new MultiChar(Set(char, right.char))
	def || (right : MultiChar) = new MultiChar(right.chars + char)
	def union (right : SingleChar) = this || right
	def union (right : MultiChar) = this || right
	def or (right : SingleChar) = this || right
	def or (right : MultiChar) = this || right
	override def equals (other : Any) = other match
	{
		case that : SingleChar => this.char == that.char
		case _ => false
	}
	override def hashCode() = 31 * char.hashCode()
}