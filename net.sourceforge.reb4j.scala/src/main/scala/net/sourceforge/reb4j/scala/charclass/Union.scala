package net.sourceforge.reb4j.scala.charclass

case class Union(val left : CharClass, val right : CharClass) 
	extends BracketsRequired with WrappedNegation
{
	override def withoutBrackets =
		(left match
		{
			case x : BracketsRequired => x.withoutBrackets
			case _ => left.toString
		}) + 
		(right match
		{
			case x : BracketsRequired => x.withoutBrackets
			case _ => right.toString
		})
}