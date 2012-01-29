package net.sourceforge.reb4j.scala.charclass

final class CharRange private[charclass] (val first : Char, val last : Char) 
	extends CharClass 
	with WrappedNegation 
	with BracketsRequired 
	with Union.Subset 
	with Intersection.Superset
{
	require (first < last, "first must be < last")
	
	override def unitableForm() = first + "-" + last
}