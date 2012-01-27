package net.sourceforge.reb4j.scala.experimental

@SerialVersionUID(1L)
case class Raw private[experimental] (val expression : String) 
	extends Expression with Alternative 
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
	def then (right : Raw) = this + right
	def then (right : Literal) = this + right
}

object AnyChar extends Raw(".") with Quantifiable
object LineBegin extends Raw("^") with Quantifiable
object LineEnd extends Raw("$") with Quantifiable
object WordBoundary extends Raw("\\b") with Quantifiable
object NonwordBoundary extends Raw("\\B") with Quantifiable
object InputBegin extends Raw("\\A") with Quantifiable
object MatchEnd extends Raw("\\G") with Quantifiable
object InputEndSkipEOL extends Raw("\\Z") with Quantifiable
object InputEnd extends Raw("\\z") with Quantifiable