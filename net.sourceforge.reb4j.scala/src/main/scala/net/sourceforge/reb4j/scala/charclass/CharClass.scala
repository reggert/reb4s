package net.sourceforge.reb4j.scala.charclass
import java.lang.Character.UnicodeBlock
import net.sourceforge.reb4j.scala.{Expression, Alternation, Quantifiable, Sequence}

trait CharClass extends Expression 
	with Alternation.Alternative
	with Sequence.Sequenceable
	with Quantifiable
{
	def ^ : CharClass
	final def negate = ^
	
	protected[charclass] def unitableForm() : String
	protected[charclass] def independentForm() : String
	final override def toString() = independentForm()
}


object CharClass
{
	def char(c : Char) = new SingleChar(c)
	def chars(cs : Set[Char]) = new MultiChar(cs)
	def chars(cs : Seq[Char]) = new MultiChar(Set.empty ++ cs)
	def range(first : Char, last : Char) = new CharRange(first, last)
	
	object Perl
	{
		val Digit = new PredefinedClass('d')
		val Space = new PredefinedClass('s')
		val Word = new PredefinedClass('w')
	}
	
	object Posix
	{
		val Lower = new NamedPredefinedClass("Lower")
		val Upper = new NamedPredefinedClass("Upper")
		val Alpha = new NamedPredefinedClass("Alpha")
		val Digit = new NamedPredefinedClass("Digit")
		val Alnum = new NamedPredefinedClass("Alnum")
		val Punct = new NamedPredefinedClass("Punct")
		val Graph = new NamedPredefinedClass("Graph")
		val Print = new NamedPredefinedClass("Print")
		val Blank = new NamedPredefinedClass("Blank")
		val Control = new NamedPredefinedClass("Cntrl")
		val HexDigit = new NamedPredefinedClass("XDigit")
		val Space = new NamedPredefinedClass("Space")
	}
	
	object Java
	{
		val LowerCase = new NamedPredefinedClass("javaLowerCase")
		val UpperCase = new NamedPredefinedClass("javaUpperCase")
		val Whitespace = new NamedPredefinedClass("javaWhitespace")
		val Mirrored = new NamedPredefinedClass("javaMirrored")
	}
	
	object Unicode
	{
		private type UnicodeBlock = java.lang.Character.UnicodeBlock
		private def % (className : String) = new NamedPredefinedClass(className)
		def block(unicodeBlock : UnicodeBlock) = %("In" + unicodeBlock.toString())
		/*
			From the Unicode Specification, version 4.0.0, the Unicode categories are:
				Lu = Letter, uppercase
				Ll = Letter, lowercase
				Lt = Letter, titlecase
				Lm = Letter, modifier
				Lo = Letter, other
				Mn = Mark, nonspacing
				Mc = Mark, spacing combining
				Me = Mark, enclosing
				Nd = Number, decimal digit
				Nl = Number, letter
				No = Number, other
				Zs = Separator, space
				Zl = Separator, line
				Zp = Separator, paragraph
				Cc = Other, control
				Cf = Other, format
				Cs = Other, surrogate
				Co = Other, private use
				Cn = Other, not assigned (including noncharacters)
				Pc = Punctuation, connector
				Pd = Punctuation, dash
				Ps = Punctuation, open
				Pe = Punctuation, close
				Pi = Punctuation, initial quote (may behave like Ps or Pe depending on usage)
				Pf = Punctuation, final quote (may behave like Ps or Pe depending on usage)
				Po = Punctuation, other
				Sm = Symbol, math
				Sc = Symbol, currency
				Sk = Symbol, modifier
				So = Symbol, other
		 */
		object Letter
		{
			val Uppercase = %("Lu")
			val Lowercase = %("Ll")
			val Titlecase = %("Lt")
			val Modifier = %("Lm")
			val Other = %("Lo")
		}
		
		object Mark
		{
			val Nonspacing = %("Mn")
			val SpacingCombining = %("Mc")
			val Enclosing = %("Me")
		}
		
		object Number
		{
			val DecimalDigit = %("Nd")
			val Letter = %("Nl")
			val Other = %("No")
		}
		
		object Separator
		{
			val Space = %("Zs")
			val Line = %("Zl")
			val Paragraph = %("Zp")
		}
		
		object Other
		{
			val Control = %("Cc")
			val Format = %("Cf")
			val Surrogate = %("Cs")
			val PrivateUse = %("Co")
			val NotAssigned = %("Cn")
		}
		
		object Punctuation
		{
			val Connector = %("Pc")
			val Dash = %("Pd")
			val Open = %("Po")
			val Close = %("Pe")
			val InitialQuote = %("Pi")
			val FinalQuote = %("Pf")
			val Other = %("Po")
		}
		
		object Symbol
		{
			val Math = %("Sm")
			val Currency = %("Sc")
			val Modifier = %("Sk")
			val Other = %("So")
		}
	}
}




