package net.sourceforge.reb4j.scala
import java.util.regex.Pattern

class Regex private[scala] (val expression : String, val atomic : Boolean) 
{
	if (expression == null)
		throw new NullPointerException("expression");
	
	override def toString = expression
	
	def this(pattern : Pattern) = this(pattern.toString(), false)
	private[scala] def this(sb : StringBuilder, atomic : Boolean) = 
		this(sb.toString(), atomic)
	
	def toPattern = Pattern.compile(expression)
	def toPattern(flags : Int) = Pattern.compile(expression, flags)
	
	def or(right : Regex*) = 
		new Regex(right addString(new StringBuilder(expression), "|"), false)
	def | (right : Regex) = or(right)
	
	def then(right : Regex*) = 
		new Regex(right addString new StringBuilder(expression), false)
	def :: (left : Regex) = left then this
	
	def * = new Regex(delimit + '*', false)
	def *? = new Regex(delimit + "*?", false)
	def *+ = new Regex(delimit + "*+", false)
	def anyTimes = *
	def anyTimesReluctantly = *?
	def anyTimesPossessively = *+
	
	def + = new Regex(delimit + '+', false)
	def +? = new Regex(delimit + "+?", false)
	def ++ = new Regex(delimit + "++", false)
	def atLeastOnce = this+
	def atLeastOnceReluctantly = +?
	def atLeastOncePossessively = ++
	
	def ## (n : Int) = new Regex(delimit + "{" + n + "}", false)
	def ##? (n : Int) = new Regex(delimit + "{" + n + "}?", false)
	def ##+ (n : Int) = new Regex(delimit + "{" + n + "}+", false)
	def ## (min : Int, max : Int) = new Regex(delimit + "{" + min + "," + max + "}", false)
	def ##? (min : Int, max : Int) = new Regex(delimit + "{" + min + "," + max + "}?", false)
	def ##+ (min : Int, max : Int) = new Regex(delimit + "{" + min + "," + max + "}+", false)
	def ##> (n : Int) = new Regex(delimit + "{" + n + ",}", false)
	def ##>? (n : Int) = new Regex(delimit + "{" + n + ",}?", false)
	def ##>+ (n : Int) = new Regex(delimit + "{" + n + ",}+", false)
	def repeat (n : Int) = ## (n)
	def repeatReluctantly (n : Int) = ##? (n)
	def repeatPossessively (n : Int) = ##+ (n)
	def repeat (min : Int, max : Int) = ## (min, max)
	def repeatReluctantly (min : Int, max : Int) = ##? (min, max)
	def repeatPossessively (min : Int, max : Int) = ##+ (min, max)
	def atLeast (n : Int) = ##> (n)
	def atLeastReluctantly (n : Int) = ##>? (n)
	def atLeastPossessively (n : Int) = ##>+ (n)
	
	def ? = new Regex(delimit + "?", false)
	def ?? = new Regex(delimit + "??", false)
	def ?+ = new Regex(delimit + "?+", false)
	def optional = ?
	def optionalReluctantly = ??
	def optionalPossessively = ?+
	
	def group = new Regex("(" + expression + ")", true)
	def <> = group
	
	def lookAheadPositive = new Regex("(?=" + expression + ")", true)
	def lookAheadNegative = new Regex("(?!" + expression + ")", true)
	def >>+ = lookAheadPositive
	def >>- = lookAheadNegative
	
	def lookBehindPositive = new Regex("(?<=" + expression + ")", true)
	def lookBehindNegative = new Regex("(?<!" + expression + ")", true)
	def <<+ = lookBehindPositive
	def <<- = lookBehindNegative
	
	def independent = new Regex("(?>" + expression + ")", true)
	def <|> = independent
	
	def enable(flags : Flag*) = 
		new Regex(
				(flags addString new StringBuilder("(?")) 
					append ':' 
					append expression 
					append ')', 
				true
			)
	def disable(flags : Flag*) = 
		new Regex(
				(flags addString new StringBuilder("(?-")) 
					append ':' 
					append expression 
					append ')', 
				true
			)
	def ~+ (flag : Flag) = enable (flag)
	def ~- (flag : Flag) = disable (flag)
	
	private def delimit = 
		(new StringBuilder("(?:") append expression append (')')).toString
}




object Literal
{
	def apply(literal : String) = new Regex(escape(literal), true)
	
	private val needsEscape = "()[]{}.,-\\|+*?$^&:!<>="
	private def escapeChar(c : Char) = if (needsEscape.contains(c)) "\\" + c else String.valueOf(c)
	private def escape(literal : String) = (literal.map(escapeChar) addString (new StringBuilder)).toString
}


object BackReference
{
	def apply (group : Int) = 
		if (group > 0) new Regex("\\" + group, true)
		else throw new IllegalArgumentException("group <= 0") 
}


final class Flag private[scala] (val c : Char)
{
	override def toString = String.valueOf(c)
}

object Flag
{
	val CASE_INSENSITIVE = new Flag('i')
	val UNIX_LINES = new Flag('d')
	val MULTILINE = new Flag('m')
	val DOTALL = new Flag('s')
	val UNICODE_CASE = new Flag('u') 
	val COMMENTS = new Flag('x')
}


object LINE_BEGIN extends Regex("^", true)
object LINE_END extends Regex("$", true)
object WORD_BOUNDARY extends Regex("\\b", true)
object NONWORD_BOUNDARY extends Regex("\\B", true)
object INPUT_BEGIN extends Regex("\\A", true)
object MATCH_END extends Regex("\\G", true)
object INPUT_END_SKIP_EOL extends Regex("\\Z", true);
object INPUT_END extends Regex("\\z", true);



