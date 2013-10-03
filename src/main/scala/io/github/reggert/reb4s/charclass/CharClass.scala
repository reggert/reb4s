package io.github.reggert.reb4s.charclass
import java.lang.Character.UnicodeBlock
import io.github.reggert.reb4s.{Expression, Alternative, Quantifiable, Sequenceable}

/**
 * Base class representing an expression that matches a single character 
 * within a class of characters.
 */
@SerialVersionUID(1L)
abstract class CharClass extends Expression 
	with Alternative
	with Sequenceable
	with Quantifiable
{
	/**
	 * Returns an expressing matching a single character that is not within
	 * the class of characters matched by this expression.
	 */
	final def unary_~ = negated
	
	/**
	 * Returns an expressing matching a single character that is not within
	 * the class of characters matched by this expression.
	 */
	def negated : CharClass
	
	/**
	 * The regular expression string that can be used within square brackets
	 * to merge with other character classes.
	 */
	protected[charclass] def unitableForm : String
	
	/**
	 * The regular expression string that can be used independently of square 
	 * brackets.
	 */
	protected[charclass] def independentForm : String
	
	final override def expression = independentForm
	
	/**
	 * Returns the intersection of this character class with the 
	 * specified character class.
	 */
	def && (right : CharClass) : Intersection =
		new Intersection(List(this, right))
		
	/**
	 * Returns the intersection of this character class with the 
	 * specified character classes.
	 */
	def && (right : Intersection) : Intersection =
		new Intersection(this::right.supersets)
		
	/**
	 * Returns the intersection of this character class with the 
	 * specified character class.
	 */
	final def intersect (right : CharClass) = this && right
		
	/**
	 * Returns the intersection of this character class with the 
	 * specified character classes.
	 */
	final def intersect (right : Intersection) = this && right
	
	/**
	 * Returns the union of this character class with the 
	 * specified character class.
	 */
	def || (right : Union) : Union =
		new Union(this::right.subsets)
	
	/**
	 * Returns the union of this character class with the 
	 * specified character classes.
	 */
	def || (right : CharClass) : Union =
		new Union(List(this, right))
	
	/**
	 * Returns the union of this character class with the 
	 * specified character classes.
	 */
	final def union (right : Union) : Union = this || right
	
	/**
	 * Returns the union of this character class with the 
	 * specified character class.
	 */
	final def union (right : CharClass) : Union = this || right
	
	/**
	 * Returns the union of this character class with the 
	 * specified character classes.
	 */
	final def or (right : Union) : Union = this || right
	
	/**
	 * Returns the union of this character class with the 
	 * specified character class.
	 */
	final def or (right : CharClass) : Union = this || right
}


/**
 * Factory object for creating character class expressions.
 */
object CharClass
{
	/**
	 * Creates an expression matching a single specified character.
	 */
	def char(c : Char) = new SingleChar(c)
	
	/**
	 * Creates an expression that matches a single occurrence of any of the 
	 * specified characters.
	 */
	def chars(cs : Set[Char]) = new MultiChar(cs)
	
	/**
	 * Creates an expression that matches a single occurrence of any of the 
	 * specified characters.
	 */
	def chars(cs : Traversable[Char]) = new MultiChar(Set.empty ++ cs)
	
	/**
	 * Creates an expression that matches a single occurence of any character
	 * within the specified range.
	 * 
	 * @param first the lowest value character that will match
	 * @param last the highest value character that will match
	 * @throws IllegalArgumentException if {first} is greater than {last}.
	 */
	def range(first : Char, last : Char) = new CharRange(first, last)
	
	/**
	 * Module containing Perl-style predefined character classes.
	 */
	object Perl
	{
		/**
		 * Perl-style character class that matches a single decimal digit.
		 */
		val Digit = new PredefinedClass('d')
		
		/**
		 * Perl-style character class that matches whitespace.
		 */
		val Space = new PredefinedClass('s')
		
		/**
		 * Perl-style character class that matches "word" characters.
		 */
		val Word = new PredefinedClass('w')
	}
	
	/**
	 * Module containing POSIX-style predefined character classes.
	 */
	object Posix
	{
		/**
		 * POSIX-style character class that matches a lowercase letter.
		 */
		val Lower = new NamedPredefinedClass("Lower")
		
		/**
		 * POSIX-style character class that matches an uppercase letter.
		 */
		val Upper = new NamedPredefinedClass("Upper")
		
		/**
		 * POSIX-style character class that matches an alphabetical letter.
		 */
		val Alpha = new NamedPredefinedClass("Alpha")
		
		/**
		 * POSIX-style character class that matches a decimal digit.
		 */
		val Digit = new NamedPredefinedClass("Digit")
		
		/**
		 * POSIX-style character class that matches letters or digits.
		 */
		val Alnum = new NamedPredefinedClass("Alnum")
		
		/**
		 * POSIX-style character class that matches a punctuation character.
		 */
		val Punct = new NamedPredefinedClass("Punct")
		
		/**
		 * POSIX-style character class that matches a graphical character.
		 */
		val Graph = new NamedPredefinedClass("Graph")
		
		/**
		 * POSIX-style character class that matches any printable character.
		 */
		val Print = new NamedPredefinedClass("Print")
		
		/**
		 * POSIX-style character class that matches a space or tab.
		 */
		val Blank = new NamedPredefinedClass("Blank")
		
		/**
		 * POSIX-style character class that matches a control character.
		 */
		val Control = new NamedPredefinedClass("Cntrl")
		
		/**
		 * POSIX-style character class that matches a hexadecimal digit.
		 */
		val HexDigit = new NamedPredefinedClass("XDigit")
		
		/**
		 * POSIX-style character class that matches any whitespace character.
		 */
		val Space = new NamedPredefinedClass("Space")
	}
	
	/**
	 * Module containing predefined character classes matching character traits
	 * defined by the [[java.lang.Character]] class.
	 */
	object Java
	{
		/**
		 * Matches any single lowercase letter.
		 */
		val LowerCase = new NamedPredefinedClass("javaLowerCase")
		
		/**
		 * Matches any single uppercase letter.
		 */
		val UpperCase = new NamedPredefinedClass("javaUpperCase")
		
		/**
		 * Matches any single whitespace character.
		 */
		val Whitespace = new NamedPredefinedClass("javaWhitespace")
		
		/**
		 * Matches any single "mirrored" character.
		 */
		val Mirrored = new NamedPredefinedClass("javaMirrored")
	}
	
	/**
	 * Module containing predefined character classes matching a single
	 * character that has certain traits defined by the Unicode specification.
	 */
	object Unicode
	{
		private type UnicodeBlock = java.lang.Character.UnicodeBlock
		private def % (className : String) = new NamedPredefinedClass(className)
		
		/**
		 * Creates a character class matching any single character within the
		 * specified Unicode block.
		 */
		def block(unicodeBlock : UnicodeBlock) = %("In" + unicodeBlock.toString)
		
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
		
		/**
		 * Module containing predefined character classes matching any single
		 * character defined as a "letter" by the Unicode specification.
		 */
		object Letter
		{
			/**
			 * Matches uppercase letters.
			 */
			val Uppercase = %("Lu")
			
			/**
			 * Matches lowercase letters.
			 */
			val Lowercase = %("Ll")
			
			/**
			 * Matches titlecase letters.
			 */
			val Titlecase = %("Lt")
			
			/**
			 * Matches letter modifiers.
			 */
			val Modifier = %("Lm")
			
			/**
			 * Matches "other" letters defined by Unicode.
			 */
			val Other = %("Lo")
		}
		
		/**
		 * Module containing predefined character classes matching any single
		 * character defined as a "mark" by the Unicode specification.
		 */
		object Mark
		{
			/**
			 * Matches nonspacing marks.
			 */
			val Nonspacing = %("Mn")
			
			/**
			 * Matches spacing-combining marks.
			 */
			val SpacingCombining = %("Mc")
			
			/**
			 * Matches enclosing marks.
			 */
			val Enclosing = %("Me")
		}
		
		/**
		 * Module containing predefined character classes matching any single
		 * character defined as a "number" by the Unicode specification.
		 */
		object Number
		{
			/**
			 * Matches decimal digits.
			 */
			val DecimalDigit = %("Nd")
			
			/**
			 * Matches letter characters used as digits.
			 */
			val Letter = %("Nl")
			
			/**
			 * Matches "other" digit characters defined by Unicode.
			 */
			val Other = %("No")
		}
		
		/**
		 * Module containing predefined character classes matching any single
		 * character defined as a "separator" by the Unicode specification.
		 */
		object Separator
		{
			/**
			 * Mataches spaces.
			 */
			val Space = %("Zs")
			
			/**
			 * Matches line breaks.
			 */
			val Line = %("Zl")
			
			/**
			 * Matches paragraph breaks.
			 */
			val Paragraph = %("Zp")
		}
		
		/**
		 * Module containing predefined character classes matching any single
		 * character that does not fit into any other category defined by the
		 * Unicode specification.
		 */
		object Other
		{
			/**
			 * Matches control characters.
			 */
			val Control = %("Cc")
			
			/**
			 * Matches formatting characters.
			 */
			val Format = %("Cf")
			
			/**
			 * Matches surrogate characters.
			 */
			val Surrogate = %("Cs")
			
			/**
			 * Matches characters defined for private use.
			 */
			val PrivateUse = %("Co")
			
			/**
			 * Matches unassigned characters.
			 */
			val NotAssigned = %("Cn")
		}
		
		/**
		 * Module containing predefined character classes matching any single
		 * character that is defined as "punctuation" by the Unicode 
		 * specification.
		 */
		object Punctuation
		{
			/**
			 * Matches connectors.
			 */
			val Connector = %("Pc")
			
			/**
			 * Matches dashes.
			 */
			val Dash = %("Pd")
			
			/**
			 * Matches "opening" punctuation.
			 */
			val Open = %("Po")
			
			/**
			 * Matches "closing" punctuation.
			 */
			val Close = %("Pe")
			
			/**
			 * Matches initial quotes.
			 */
			val InitialQuote = %("Pi")
			
			/**
			 * Matches closing quotes.
			 */
			val FinalQuote = %("Pf")
			
			/**
			 * Matches other punctuation.
			 */
			val Other = %("Po")
		}
		
		/**
		 * Module containing predefined character classes matching any single
		 * character that is defined as a "symbol" by the Unicode 
		 * specification.
		 */
		object Symbol
		{
			/**
			 * Matches mathematical symbols.
			 */
			val Math = %("Sm")
			
			/**
			 * Matches currency symbols.
			 */
			val Currency = %("Sc")
			
			/**
			 * Matches symbol modifiers.
			 */
			val Modifier = %("Sk")
			
			/**
			 * Matches other symbols.
			 */
			val Other = %("So")
		}
	}
}




