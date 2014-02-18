package io.github.reggert.reb4s
import java.util.regex.Pattern
import scala.util.matching.Regex

/**
 * Base class extended by all '''reb4j''' expression builders.
 */
@SerialVersionUID(1L)
abstract class Expression extends Serializable with NotNull with Immutable
{
	/**
	 * Returns the regular expression represented by this object, in a form 
	 * suitable to passing to the [[java.util.regex.Pattern]] class.
	 */
	def expression : String
	
	/**
	 * Always returns the same value as [[Expression#expression]].
	 */
	final override def toString = expression
	
	/**
	 * Passes the regular expression represented by this object to 
	 * [[java.util.regex.Pattern]] and returns the result.
	 */
	final def toPattern : Pattern = Pattern.compile(expression)
	
	/**
	 * Uses the regular expression represented by this object to
	 * construct a [[scala.util.matching.Regex]].
	 */
	final def toRegex(groupNames : String*) : Regex = 
		new Regex(toString, groupNames.toList : _*)
	
	/**
	 * If the expression has a computable maximum length, this returns it.
	 * Otherwise, it returns None.
	 * 
	 * This is used for determining whether an expression is suitable for 
	 * look-behind.
	 */
	def boundedLength : Option[Int]
}

