package net.sourceforge.reb4j.scala
import java.util.regex.Pattern
import scala.util.matching.Regex

/**
 * An expression that has been precompiled by the [[java.util.regex.Pattern]]
 * class and adopted for use by '''reb4j'''.
 */
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


/**
 * Function that adopts the regular expression represented by the specified
 * [[java.util.regex.Pattern]] or [[scala.util.matching.Regex]].
 */
object Adopt
{
	def apply(pattern : Pattern) = new Adopted(pattern.pattern)
	def apply(regex : Regex) = new Adopted(regex.toString)
}

