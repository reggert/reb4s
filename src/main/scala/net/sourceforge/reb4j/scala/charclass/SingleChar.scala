package net.sourceforge.reb4j.scala.charclass
import net.sourceforge.reb4j.scala.Alternative
import net.sourceforge.reb4j.scala.Literal

final class SingleChar private[charclass] (val char : Char) 
	extends CharClass 
	with WrappedNegation 
	with SelfContained
	with Union.Subset
	with Intersection.Superset
{
	def || (right : SingleChar) = new MultiChar(Set(char, right.char))
	def || (right : MultiChar) = new MultiChar(right.chars + char)
	override def unitableForm() = Literal.escapeChar(char)
}