package net.sourceforge.reb4j.scala.charclass

final class Negated[T <: WrappedNegation] private[charclass] (val positive : T) 
	extends CharClass 
	with SelfContained
	with Union.Subset
	with Intersection.Superset
{
	override def ^ = positive
	override def unitableForm() = "[^" + positive.unitableForm() + "]"
	override def equals (other : Any) = other match
	{
		case that : Negated[_] => this.positive == that.positive
		case _ => false
	}
	override def hashCode() = 31 * positive.hashCode()
}