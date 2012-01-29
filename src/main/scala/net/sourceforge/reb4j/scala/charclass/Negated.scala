package net.sourceforge.reb4j.scala.charclass

final case class Negated[T <: WrappedNegation](val positive : T) 
	extends CharClass with SelfContained
{
	override def ^ = positive
	override def unitableForm() = "[^" + positive.unitableForm() + "]"
}