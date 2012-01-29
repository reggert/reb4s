package net.sourceforge.reb4j.scala.charclass

final case class Intersection(val supersets : Set[CharClass]) extends CharClass
	with BracketsRequired with WrappedNegation
{
	override def unitableForm() = 
		(supersets map ((charClass : CharClass) => charClass.independentForm())).addString(new StringBuilder, "&&").toString()
}