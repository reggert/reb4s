package net.sourceforge.reb4j.scala.charclass

/**
 * Trait marking character classes that must be wrapped in
 * square brackets in order to be negated.
 */
private[charclass] trait WrappedNegation[T <: WrappedNegation[T]] extends CharClass
{
	this : T =>
	override final def negated = new Negated[T](this)
}