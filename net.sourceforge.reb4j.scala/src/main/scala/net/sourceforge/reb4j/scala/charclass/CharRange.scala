package net.sourceforge.reb4j.scala.charclass

/**
 * Character class matching any character between a range of characters.
 */
final class CharRange private[charclass] (val first : Char, val last : Char) 
	extends CharClass 
	with WrappedNegation[CharRange] 
	with BracketsRequired 
	with Union.Subset 
	with Intersection.Superset
{
	require (first < last, "first must be < last")
	
	override lazy val unitableForm = first + "-" + last
	override def equals(other : Any) = other match
	{
		case that : CharRange => 
			this.first == that.first && this.last == that.last
		case _ => false
	}
	override lazy val hashCode = 
		31 * first.hashCode + last.hashCode
}