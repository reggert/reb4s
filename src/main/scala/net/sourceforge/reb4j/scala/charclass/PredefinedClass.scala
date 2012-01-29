package net.sourceforge.reb4j.scala.charclass

private[charclass] class PredefinedClass(val nameChar : Char) extends CharClass
	with SelfContained
{
	protected def invertedNameChar = 
		if (nameChar.isUpper) 
			nameChar.toLower
		else
			nameChar.toUpper
	override def ^ = new PredefinedClass(invertedNameChar)
	override def unitableForm = "\\" + nameChar
}


private[charclass] final class NamedPredefinedClass(
		nameChar : Char, 
		val className : String
	) extends PredefinedClass(nameChar)
{
	def this (className : String) = this('p', className)
	override def ^ = new NamedPredefinedClass(invertedNameChar, className)
	override def unitableForm = super.unitableForm() + "{" + className + "}"
}


object PredefinedClass
{
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
		private type UnicodeBlock = java.lang.Character.UnicodeBlock
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

