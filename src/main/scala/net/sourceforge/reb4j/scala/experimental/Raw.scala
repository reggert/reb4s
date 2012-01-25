package net.sourceforge.reb4j.scala.experimental

@SerialVersionUID(1L)
case class Raw private[experimental] (val expression : String) 
	extends Expression with Alternative with Quantifiable
{
	override def toString = expression
	override def + (right : Alternative) = right match
	{
		case Raw(rhs) => new Raw(expression + rhs)
		case right : Literal => new Raw(expression + right.escaped)
		case _ => super.+(right)
	}
	def + (right : Raw) = new Raw(expression + right.expression)
	def + (right : Literal) = new Raw(expression + right.escaped)
}


object AnyChar extends Raw(".")
object LineBegin extends Raw("^")
object LineEnd extends Raw("$")
object WordBoundary extends Raw("\\b")
object NonwordBoundary extends Raw("\\B")
object InputBegin extends Raw("\\A")
object MatchEnd extends Raw("\\G")
object InputEndSkipEOL extends Raw("\\Z")
object InputEnd extends Raw("\\z")