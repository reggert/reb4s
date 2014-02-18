package io.github.reggert.reb4s.charclass

import io.github.reggert.reb4s.Literal.escapeChar

/**
 * Character class matching any character between a range of characters.
 */
final class CharRange private[charclass] (val first : Char, val last : Char) 
	extends CharClass 
	with WrappedNegation[CharRange] 
	with BracketsRequired 
{
	require (first <= last, "first must be <= last")
	
	override lazy val unitableForm = escapeChar(first) + "-" + escapeChar(last)
	override def equals(other : Any) = other match
	{
		case that : CharRange => 
			this.first == that.first && this.last == that.last
		case _ => false
	}
	override lazy val hashCode = 
		31 * first.hashCode + last.hashCode
}
