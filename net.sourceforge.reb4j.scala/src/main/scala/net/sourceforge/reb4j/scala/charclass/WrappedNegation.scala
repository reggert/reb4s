package net.sourceforge.reb4j.scala.charclass

trait WrappedNegation extends CharClass
{
	// TODO: get the genericity right
	override def ^ () = new Negated(this)
}