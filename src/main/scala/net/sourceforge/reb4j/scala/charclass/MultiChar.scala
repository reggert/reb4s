package net.sourceforge.reb4j.scala.charclass
import net.sourceforge.reb4j.scala.Alternative
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
}