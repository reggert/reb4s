package net.sourceforge.reb4j.scala
import java.util.regex.Pattern
import scala.util.matching.Regex

@SerialVersionUID(1L)
final class Adopted private[scala] (override val expression : String) extends Expression
{
	override def equals(other : Any) = other match
	{
		case that : Adopted => this.expression == that.expression
		case _ => false
	}
	
	override lazy val hashCode = 31 * expression.hashCode
}


object Adopt
{
	def apply(pattern : Pattern) = new Adopted(pattern.pattern)
	def apply(regex : Regex) = new Adopted(regex.toString)
}

