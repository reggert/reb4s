package net.sourceforge.reb4j.scala

@SerialVersionUID(1L)
sealed class Raw private[scala] (val expression : String) 
	extends Expression 
	with Alternation.Alternative 
	with Sequence.Sequenceable
{
	final def + (right : Raw) = new Raw(expression + right.expression)
	final def + (right : Literal) = new Raw(expression + right.escaped)
	final def then (right : Raw) = this + right
	final def then (right : Literal) = this + right
	final override def equals(other : Any) = other match
	{
		case that : Raw => this.expression == that.expression
		case _ => false
	}
	override lazy val hashCode = 31 * expression.hashCode
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