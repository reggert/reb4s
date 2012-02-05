package net.sourceforge.reb4j.scala.charclass

trait WrappedNegation extends CharClass
{
	// TODO: get the genericity right
	override final def negated = new Negated(this)
}