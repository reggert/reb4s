package io.github.reggert.reb4s
import java.util.regex.Pattern
import scala.util.matching.Regex

/**
 * An expression that has been precompiled by the java.util.regex.Pattern
 * class and adopted for use by '''reb4j'''.
 */
@SerialVersionUID(1L)
final class Adopted private[reb4s] (override val expression : String) extends Expression
{
	override def equals(other : Any) = other match
	{
		case that : Adopted => this.expression == that.expression
		case _ => false
	}
	
	override lazy val hashCode = 31 * expression.hashCode
	
	protected[reb4s] override def boundedLength = None // can't know for sure
	
	protected[reb4s] override def repetitionInvalidatesBounds : Boolean = true // can't know for sure
	
	protected[reb4s] override def possiblyZeroLength : Boolean = true // can't know for sure
}


/**
 * Factory methods that adopt the regular expression represented by the specified
 * java.util.regex.Pattern or [[scala.util.matching.Regex]].
 */
object Adopted
{
	def fromPattern(pattern : Pattern) = new Adopted(pattern.pattern)
	def fromRegex(regex : Regex) = new Adopted(regex.toString)
}

