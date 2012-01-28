package net.sourceforge.reb4j.scala.charclass

class CharRange(val first : Char, val last : Char) 
	extends WrappedNegation with BracketsRequired
{
	require (first < last, "first must be < last")
	
	override def withoutBrackets = first + "-" + last
}