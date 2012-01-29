package net.sourceforge.reb4j.scala.charclass

final case class CharRange(val first : Char, val last : Char) 
	extends WrappedNegation with BracketsRequired
{
	require (first < last, "first must be < last")
	
	override def unitableForm() = first + "-" + last
}