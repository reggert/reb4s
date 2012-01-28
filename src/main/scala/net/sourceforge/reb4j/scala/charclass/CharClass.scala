package net.sourceforge.reb4j.scala
import java.lang.Character.UnicodeBlock

trait CharClass extends Expression with Alternative with Quantifiable
{
	def ^ : CharClass
	def negate = ^
	
	/* 
	 * We could do an override def || (right : Alternative) here, but it's
	 * unlikely to be useful.
	 */
	def || (right : CharClass) = right match
	{
		case Union(rhs, negated) if (!negated) => new Union(this::rhs, false)
		case _ => new Union(List(this, right), false)
	}
	def || (right : Union) = new Union(List(this, right), false)
	def union (right : CharClass) = this || right
	def union (right : Union) = this || right
	
	private[CharClass] case class Union (
			val subclasses : List[CharClass], 
			val negated : Boolean
		) extends CharClass
	{
		override def toString = 
			(
				subclasses addString 
				new StringBuilder(if (negated) "[^" else "[") 
				append "]"
			).toString()
		override def ^ = new Union(subclasses, !negated)
	}
}





object CharClass
{
	// TODO: handle explicit character lists and character ranges
	

	private[CharClass] class PredefinedClass (val nameChar : Char) extends CharClass
	{
		protected def invertedNameChar = 
			if (nameChar.isUpper) 
				nameChar.toLower
			else
				nameChar.toUpper
		override def ^ = new PredefinedClass(invertedNameChar)
		override def toString = "\\" + nameChar
	}

	private[CharClass] class NamedPredefinedClass (
			nameChar : Char, 
			val className : String
		) extends PredefinedClass(nameChar : Char)
	{
		def this (className : String) = this('p', className)
		override def ^ = new NamedPredefinedClass(invertedNameChar, className)
		override def toString = super.toString() + "{" + className + "}"
	}
	
	object Perl
	{
		val digit = new PredefinedClass('d')
		val space = new PredefinedClass('s')
		val word = new PredefinedClass('w')
	}
	
	object Posix
	{
		val lower = new NamedPredefinedClass("Lower")
		val upper = new NamedPredefinedClass("Upper")
		val alpha = new NamedPredefinedClass("Alpha")
		val digit = new NamedPredefinedClass("Digit")
		val alnum = new NamedPredefinedClass("Alnum")
		val punct = new NamedPredefinedClass("Punct")
		val graph = new NamedPredefinedClass("Graph")
		val print = new NamedPredefinedClass("Print")
		val blank = new NamedPredefinedClass("Blank")
		val control = new NamedPredefinedClass("Cntrl")
		val hexDigit = new NamedPredefinedClass("XDigit")
		val space = new NamedPredefinedClass("Space")
	}
	
	object Java
	{
		val lowerCase = new NamedPredefinedClass("javaLowerCase")
		val upperCase = new NamedPredefinedClass("javaUpperCase")
		val whitespace = new NamedPredefinedClass("javaWhitespace")
		val mirrored = new NamedPredefinedClass("javaMirrored")
	}
	
	object Unicode
	{
		private def $ (className : String) = new NamedPredefinedClass(className)
		def block(unicodeBlock : UnicodeBlock) = $("In" + unicodeBlock.toString())
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
			val uppercase = $("Lu")
			val lowercase = $("Ll")
			val titlecase = $("Lt")
			val modifier = $("Lm")
			val other = $("Lo")
		}
		
		object Mark
		{
			val nonspacing = $("Mn")
			val spacingCombining = $("Mc")
			val enclosing = $("Me")
		}
		
		object Number
		{
			val decimalDigit = $("Nd")
			val letter = $("Nl")
			val other = $("No")
		}
		
		object Separator
		{
			val space = $("Zs")
			val line = $("Zl")
			val paragraph = $("Zp")
		}
		
		object Other
		{
			val control = $("Cc")
			val format = $("Cf")
			val surrogate = $("Cs")
			val privateUse = $("Co")
			val notAssigned = $("Cn")
		}
		
		object Punctuation
		{
			val connector = $("Pc")
			val dash = $("Pd")
			val open = $("Po")
			val close = $("Pe")
			val initialQuote = $("Pi")
			val finalQuote = $("Pf")
			val other = $("Po")
		}
		
		object Symbol
		{
			val math = $("Sm")
			val currency = $("Sc")
			val modifier = $("Sk")
			val other = $("So")
		}
	}
}

