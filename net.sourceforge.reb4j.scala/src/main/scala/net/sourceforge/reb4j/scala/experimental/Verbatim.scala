package net.sourceforge.reb4j.scala.experimental

class Verbatim private[experimental] (val expression : String) 
	extends Expression with Atomic with Quantifiable
{
	override def toString = expression
	def + (right : Verbatim) = new Verbatim(expression + right.expression)
	def + (right : Literal) = new Verbatim(expression + right.escaped)
}


object AnyChar extends Verbatim(".")
object LineBegin extends Verbatim("^")
object LineEnd extends Verbatim("$")
object WordBoundary extends Verbatim("\\b")
object NonwordBoundary extends Verbatim("\\B")
object InputBegin extends Verbatim("\\A")
object MatchEnd extends Verbatim("\\G")
object InputEndSkipEOL extends Verbatim("\\Z")
object InputEnd extends Verbatim("\\z")