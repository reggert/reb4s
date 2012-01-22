package net.sourceforge.reb4j.scala
import java.util.regex.Pattern

class Regex (val expression : String) 
{
	if (expression == null)
		throw new NullPointerException("expression");
	
	override def toString() = expression
	
	def this(pattern : Pattern) = this(pattern.toString())
	
	def or(right : Regex*) = right.addString(new StringBuilder(expression), "|")
	
	def then(right : Regex*) = right addString new StringBuilder(expression)
}




object Literal
{
	def apply(literal : String) = new Regex(Pattern.quote(literal))
	
	private val needsEscape = "()[]{}.,-\\|+*?$^&:!<>="
	private def escapeChar(c : Char) = if (needsEscape.contains(c)) "\\" + c else String.valueOf(c)
	private def escape(literal : String) = (literal.map(escapeChar) addString (new StringBuilder)).toString()
}







