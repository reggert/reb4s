package net.sourceforge.reb4j.scala

class CharClass private[scala] (expression : Seq[Char]) 
	extends Regex(expression, true)
{
	def negate = new CharClass("[^" + expression + "]")
	def ^ = negate
	
	def union(classes : CharClass*) = 
		new CharClass(classes addString new StringBuilder("[") append "]")
	def | (charClass : CharClass) = union (charClass)
	
	def intersection(classes : CharClass*) =
		new CharClass(classes addString(new StringBuilder("["), "&&") append "]")
	def & (charClass : CharClass) = intersection (charClass)
	
	
}


object Characters
{
	def apply(characters : Seq[Char]) = 
		new CharClass("[" + Literal.escape(characters) + "]")
	
	object Not
	{
		def apply(characters : Seq[Char]) = 
			new CharClass("[^" + Literal.escape(characters) + "]")
	}
}



object Range
{
	def apply(first : Char, last : Char) =
		new CharClass(
				new StringBuilder("[")
					append Literal.escapeChar(first)
					append '-' 
					append Literal.escapeChar(last)
					append ']'
			)
	
	object Not
	{
		def apply(first : Char, last : Char) =
			new CharClass(
					new StringBuilder("[^")
						append Literal.escapeChar(first)
						append '-' 
						append Literal.escapeChar(last)
						append ']'
				)
	}
}


object UnicodeBlock
{
	def apply(blockName : String) =
		new CharClass(
				new StringBuilder("\\p{In")
					append Character.UnicodeBlock.forName(blockName)
					append '}'
			)
	object Not
	{
		def apply(blockName : String) =
			new CharClass(
					new StringBuilder("\\P{In")
						append Character.UnicodeBlock.forName(blockName)
						append '}'
				)
	}
}

object UnicodeCategory
{
	private val CATEGORIES = List(
			"C",
			"Cc",
			"Cf",
			"Cn",
			"Co", 
			"Cs",
			"L",
			"Ll", 
			"Lm",
			"Lo", 
			"Lt",
			"Lu",
			"M",
			"Mc",
			"Me",
			"Mn",
			"N",
			"Nd",
			"Nl",
			"No",
			"P",
			"Pc",
			"Pd",
			"Pe",
			"Pf",
			"Pi",
			"Po",
			"Ps",
			"S",
			"Sc",
			"Sk",
			"Sm",
			"So",
			"Z",
			"Zl",
			"Zp",
			"Zs"
		)
	def apply(category : String) =
		if (CATEGORIES.contains(category))
			new CharClass(
					new StringBuilder("\\p{Is")
						append category
						append '}'
				)
		else
			throw new IllegalArgumentException("category")
	
	object Not
	{
		def apply(category : String) =
			if (CATEGORIES.contains(category))
				new CharClass(
						new StringBuilder("\\P{Is")
							append category
							append '}'
						)
			else
				throw new IllegalArgumentException("category")
	}
}


/**
 * Matches ASCII lowercase letters.  Equivalent to the POSIX "lower"
 * character class.
 */
object POSIX_LOWER extends CharClass("\\p{Lower}");

/**
 * Matches ASCII uppercase letters.  Equivalent to the POSIX "upper"
 * character class.
 */
object POSIX_UPPER extends CharClass("\\p{Upper}");

/**
 * Matches all ASCII characters.  Equivalent to the POSIX "ascii"
 * character class.
 */
object POSIX_ASCII extends CharClass("\\p{ASCII}");

/**
 * Matches all ASCII alphabetic characters.  Equivalent to the POSIX
 * "alpha" character class.
 */
object POSIX_ALPHA extends CharClass("\\p{Alpha}");

/**
 * Matches all decimal digits.  Equivalent to the POSIX "digit"
 * character class.
 */
object POSIX_DIGIT extends CharClass("\\p{Digit}");

/**
 * Matches all decimal digits AND ASCII alphabetic characters.  Equivalent
 * to the POSIX "alnum" character class.
 */
object POSIX_ALNUM extends CharClass("\\p{Alnum}");

/**
 * Matches all ASCII punctuation characters.  Equivalent to the POSIX
 * "punct" character class.
 */
object POSIX_PUNCT extends CharClass("\\p{Punct}");

/**
 * Matches all ASCII graphical characters.  Equivalent to the POSIX
 * "graph" character class.
 */
object POSIX_GRAPH extends CharClass("\\p{Graph}");

/**
 * Matches all ASCII printable characters.  Equivalent to the POSIX
 * "print" character class.
 */
object POSIX_PRINT extends CharClass("\\p{Print}");

/**
 * Matches spaces and tabs.  Equivalent to the POSIX "blank" character
 * class.
 */
object POSIX_BLANK extends CharClass("\\p{Blank}");

/**
 * Matches all ASCII control characters.  Equivalent to the POSIX "cntrl"
 * character class.
 */
object POSIX_CNTRL extends CharClass("\\p{Cntrl}");

/**
 * Matches all hexadecimal digit characters.  Equivalent to the POSIX
 * "xdigit" character class.
 */
object POSIX_XDIGIT extends CharClass("\\p{XDigit}");

/**
 * Matches all ASCII whitespace characters.  Equivalent to the POSIX
 * "space" character class.
 */
object POSIX_SPACE extends CharClass("\\p{Space}");

/**
 * Matches all uppercase characters, as defined by the 
 * {@link java.lang.Character} class.
 */
object UPPERCASE extends CharClass("\\p{javaUpperCase}");

/**
 * Matches all lowercase characters, as defined by the 
 * {@link java.lang.Character} class.
 */
object LOWERCASE extends CharClass("\\p{javaLowerCase}");

/**
 * Matches all whitespace characters, as defined by the 
 * {@link java.lang.Character} class.
 */
object WHITESPACE extends CharClass("\\p{javaWhitespace}");

/**
 * Matches all mirrored characters, as defined by the 
 * {@link java.lang.Character} class.
 */
object MIRRORED extends CharClass("\\p{javaMirrored}");

/**
 * Matches all decimal digits using the corresponding Perl-style character
 * class.
 */
object PERL_DIGIT extends CharClass("\\d");

/**
 * Matches all characters that are not decimal digits using the 
 * corresponding Perl-style character class.
 */
object PERL_NONDIGIT extends CharClass("\\D");

/**
 * Matches all ASCII whitespace characters, as defined by the
 * Perl-style '\s' character class.
 */
object PERL_SPACE extends CharClass("\\s");

/**
 * Matches all characters except ASCII whitespace characters.
 * Equivalent to the Perl-style '\S' character class.
 */
object PERL_NONSPACE extends CharClass("\\S");

/**
 * Matches all "word" characters, as defined by the Perl-style
 * '\w' character class.
 */
object PERL_WORD extends CharClass("\\w");

/**
 * Matches all "non-word" characters, as defined by the Perl-style
 * '\W' character class.
 */
object PERL_NONWORD extends CharClass("\\W");

