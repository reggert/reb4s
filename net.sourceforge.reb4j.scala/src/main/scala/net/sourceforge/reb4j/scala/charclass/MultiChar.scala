package net.sourceforge.reb4j.scala.charclass
import net.sourceforge.reb4j.scala.Literal


final class MultiChar private[charclass] (val chars : Set[Char]) 
	extends CharClass
	with BracketsRequired 
	with WrappedNegation
	with Union.Subset
	with Intersection.Superset
{
	override def unitableForm() = 
		Literal.escape(chars.toSeq)
	def || (right : SingleChar) = new MultiChar(chars + right.char)
	def || (right : MultiChar) = new MultiChar(chars ++ right.chars)
	def union (right : SingleChar) = this || right
	def union (right : MultiChar) = this || right
	def or (right : SingleChar) = this || right
	def or (right : MultiChar) = this || right
	
	override def equals(other : Any) = other match
	{
		case that : MultiChar => this.chars == that.chars
		case _ => false
	}
	override def hashCode() = 31 * chars.hashCode()
}