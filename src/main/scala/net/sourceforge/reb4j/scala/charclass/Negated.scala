package net.sourceforge.reb4j.scala.charclass

/**
 * Character class representing the negation of another character class.
 */
final class Negated[T <: WrappedNegation] private[charclass] (val positive : T) 
	extends CharClass 
	with SelfContained
	with Union.Subset
	with Intersection.Superset
{
	override def negated = positive
	override lazy val unitableForm = "[^" + positive.unitableForm + "]"
	override def equals (other : Any) = other match
	{
		case that : Negated[_] => this.positive == that.positive
		case _ => false
	}
	override lazy val hashCode = 31 * positive.hashCode
}