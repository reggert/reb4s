package net.sourceforge.reb4j.scala.charclass

case class Negated[T <: WrappedNegation](val positive : T) extends CharClass
	with BracketsRequired
{
	override def ^ = positive
	private def positiveWithoutBrackets = positive match
	{
		case x : BracketsRequired => x.withoutBrackets
		case _ => positive.toString()
	}
	override def withoutBrackets = "^" + positiveWithoutBrackets
}