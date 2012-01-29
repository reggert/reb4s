package net.sourceforge.reb4j.scala.charclass

final class Negated[T <: WrappedNegation] private[charclass] (val positive : T) 
	extends CharClass 
	with SelfContained
	with Union.Subset
	with Intersection.Superset
{
	override def ^ = positive
	override def unitableForm() = "[^" + positive.unitableForm() + "]"
}