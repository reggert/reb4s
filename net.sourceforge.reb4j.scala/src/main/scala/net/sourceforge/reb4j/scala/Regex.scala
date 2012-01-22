package net.sourceforge.reb4j.scala
import java.util.regex.Pattern

class Regex protected[scala] (val expression : String) 
{
	if (expression == null)
		throw new NullPointerException("expression");
	
	override def toString = expression
	
	def this(pattern : Pattern) = this(pattern.toString())
	
	def toPattern = Pattern.compile(expression)
	def toPattern(flags : Int) = Pattern.compile(expression, flags)
	
	def or(right : Regex*) = right addString(new StringBuilder(expression), "|")
	def | (right : Regex) = or(right)
	
	def then(right : Regex*) = right addString new StringBuilder(expression)
	def :: (left : Regex) = left then this
	
	def * = new Regex(delimit + '*')
	def *? = new Regex(delimit + "*?")
	def *+ = new Regex(delimit + "*+")
	
	def + = new Regex(delimit + '+')
	def +? = new Regex(delimit + "+?")
	def ++ = new Regex(delimit + "++")
	
	def ## (n : Int) = new Regex(delimit + "{" + n + "}")
	def ##? (n : Int) = new Regex(delimit + "{" + n + "}?")
	def ##+ (n : Int) = new Regex(delimit + "{" + n + "}+")
	def ## (min : Int, max : Int) = new Regex(delimit + "{" + min + "," + max + "}")
	def ##? (min : Int, max : Int) = new Regex(delimit + "{" + min + "," + max + "}?")
	def ##+ (min : Int, max : Int) = new Regex(delimit + "{" + min + "," + max + "}+")
	def ##> (n : Int) = new Regex(delimit + "{" + n + ",}")
	def ##>? (n : Int) = new Regex(delimit + "{" + n + ",}?")
	def ##>+ (n : Int) = new Regex(delimit + "{" + n + ",}+")
	def repeat (n : Int) = ## (n)
	def repeat (min : Int, max : Int) = ## (min, max)
	def atLeast (n : Int) = ##> (n)
	
	def ? = new Regex(delimit + "?")
	def ?? = new Regex(delimit + "??")
	def ?+ = new Regex(delimit + "?+")
	def optional = ?
	
	def backReference (group : Int) = 
		if (group > 0) "\\" + group
		else throw new IllegalArgumentException("group <= 0") 
	
	private def delimit = 
		(new StringBuilder("(?:") append expression append (')')).toString
}




object Literal
{
	def apply(literal : String) = new Regex(escape(literal))
	
	private val needsEscape = "()[]{}.,-\\|+*?$^&:!<>="
	private def escapeChar(c : Char) = if (needsEscape.contains(c)) "\\" + c else String.valueOf(c)
	private def escape(literal : String) = (literal.map(escapeChar) addString (new StringBuilder)).toString
}






