/*
	Copyright 2009 by Richard W. Eggert II.
 
	This file is part of the raa4j library.
  
	raa4j is free software: you can redistribute it and/or modify
	it under the terms of the GNU Lesser General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	raa4j is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU Lesser General Public License
	along with raa4j.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.reb4j;

import java.lang.Character.UnicodeBlock;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Specialization of {@link Regex} representing a character class
 * expression. 
 * 
 * Because character classes can be composed of other character classes (but
 * not other type of regular expressions), a separate CharClass class is
 * needed in order to distinguish character classes from other types of
 * regular expressions.  This facilitates the creation of methods representing
 * operations specific to character classes.
 * 
 * @author Richard W. Eggert II
 *
 */
public class CharClass extends Regex
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Matches ASCII lowercase letters.  Equivalent to the POSIX "lower"
	 * character class.
	 */
	public static final CharClass POSIX_LOWER = 
		new CharClass("\\p{Lower}");
	
	/**
	 * Matches ASCII uppercase letters.  Equivalent to the POSIX "upper"
	 * character class.
	 */
	public static final CharClass POSIX_UPPER =
		new CharClass("\\p{Upper}");
	
	/**
	 * Matches all ASCII characters.  Equivalent to the POSIX "ascii"
	 * character class.
	 */
	public static final CharClass POSIX_ASCII = 
		new CharClass("\\p{ASCII}");
	
	/**
	 * Matches all ASCII alphabetic characters.  Equivalent to the POSIX
	 * "alpha" character class.
	 */
	public static final CharClass POSIX_ALPHA =
		new CharClass("\\p{Alpha}");
	
	/**
	 * Matches all decimal digits.  Equivalent to the POSIX "digit"
	 * character class.
	 */
	public static final CharClass POSIX_DIGIT =
		new CharClass("\\p{Digit}");
	
	/**
	 * Matches all decimal digits AND ASCII alphabetic characters.  Equivalent
	 * to the POSIX "alnum" character class.
	 */
	public static final CharClass POSIX_ALNUM =
		new CharClass("\\p{Alnum}");
	
	/**
	 * Matches all ASCII punctuation characters.  Equivalent to the POSIX
	 * "punct" character class.
	 */
	public static final CharClass POSIX_PUNCT = 
		new CharClass("\\p{Punct}");
	
	/**
	 * Matches all ASCII graphical characters.  Equivalent to the POSIX
	 * "graph" character class.
	 */
	public static final CharClass POSIX_GRAPH =
		new CharClass("\\p{Graph}");
	
	/**
	 * Matches all ASCII printable characters.  Equivalent to the POSIX
	 * "print" character class.
	 */
	public static final CharClass POSIX_PRINT = 
		new CharClass("\\p{Print}");
	
	/**
	 * Matches spaces and tabs.  Equivalent to the POSIX "blank" character
	 * class.
	 */
	public static final CharClass POSIX_BLANK =
		new CharClass("\\p{Blank}");
	
	/**
	 * Matches all ASCII control characters.  Equivalent to the POSIX "cntrl"
	 * character class.
	 */
	public static final CharClass POSIX_CNTRL =
		new CharClass("\\p{Cntrl}");
	
	/**
	 * Matches all hexadecimal digit characters.  Equivalent to the POSIX
	 * "xdigit" character class.
	 */
	public static final CharClass POSIX_XDIGIT =
		new CharClass("\\p{XDigit}");
	
	/**
	 * Matches all ASCII whitespace characters.  Equivalent to the POSIX
	 * "space" character class.
	 */
	public static final CharClass POSIX_SPACE =
		new CharClass("\\p{Space}");
	
	/**
	 * Matches all uppercase characters, as defined by the 
	 * {@link java.lang.Character} class.
	 */
	public static final CharClass UPPERCASE =
		new CharClass("\\p{javaUpperCase}");
	
	/**
	 * Matches all lowercase characters, as defined by the 
	 * {@link java.lang.Character} class.
	 */
	public static final CharClass LOWERCASE =
		new CharClass("\\p{javaLowerCase}");
	
	/**
	 * Matches all whitespace characters, as defined by the 
	 * {@link java.lang.Character} class.
	 */
	public static final CharClass WHITESPACE =
		new CharClass("\\p{javaWhitespace}");
	
	/**
	 * Matches all mirrored characters, as defined by the 
	 * {@link java.lang.Character} class.
	 */
	public static final CharClass MIRRORED =
		new CharClass("\\p{javaMirrored}");
	
	/**
	 * Matches all decimal digits using the corresponding Perl-style character
	 * class.
	 */
	public static final CharClass PERL_DIGIT =
		new CharClass("\\d");
	
	/**
	 * Matches all characters that are not decimal digits using the 
	 * corresponding Perl-style character class.
	 */
	public static final CharClass PERL_NONDIGIT =
		new CharClass("\\D");
	
	/**
	 * Matches all ASCII whitespace characters, as defined by the
	 * Perl-style '\s' character class.
	 */
	public static final CharClass PERL_SPACE =
		new CharClass("\\s");
	
	/**
	 * Matches all characters except ASCII whitespace characters.
	 * Equivalent to the Perl-style '\S' character class.
	 */
	public static final CharClass PERL_NONSPACE =
		new CharClass("\\S");
	
	/**
	 * Matches all "word" characters, as defined by the Perl-style
	 * '\w' character class.
	 */
	public static final CharClass PERL_WORD =
		new CharClass("\\w");
	
	/**
	 * Matches all "non-word" characters, as defined by the Perl-style
	 * '\W' character class.
	 */
	public static final CharClass PERL_NONWORD =
		new CharClass("\\W");
	
	
	
	CharClass(String expression)
	{
		super(expression);	
	}

	
	/**
	 * Creates a CharClass that matches any of the specified characters, or
	 * if <i>negate</i> is true, any character except those specified.
	 * 
	 * This method automatically escapes all characters that need escaping.
	 * 
	 * @param characters
	 * 	the set of characters to match.
	 * @param negate
	 * 	if true, the returned CharClass will match any character EXCEPT
	 * 	those specified by <code>characters</code>; otherwise, only the
	 * 	specified characters will match.
	 * @return a CharClass matching the specified characters or their
	 * 	inverse set.
	 * @throws NullPointerException if <code>characters</code> is null.
	 */
	public static CharClass characters
		(CharSequence characters, boolean negate)
	{
		if (characters == null)
			throw new NullPointerException("characters");
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if (negate) builder.append('^');
		builder.append(escapeLiteral(characters));
		builder.append(']');
		return new CharClass(builder.toString());
	}

	/**
	 * Creates a CharClass that matches any of the specified characters.
	 * 
	 * This method is equivalent to calling
	 * <code>characters(<i>characters</i>, false).</code>
	 * 
	 * @param characters
	 * 	the set of characters to match.
	 * @return a CharClass matching the specified characters.
	 * @throws NullPointerException if <code>characters</code> is null.
	 * @see #characters(CharSequence, boolean)
	 */
	public static CharClass characters
		(CharSequence characters)
	{
		return characters(characters, false);
	}

	/**
	 * Constructs a CharClass representing the inverse of the specified
	 * argument CharClass.
	 * 
	 * @param charClass the CharClass to invert.
	 * @return a CharClass that matches all characters EXCEPT those matched by
	 * 	<code>charClass</code>.
	 */
	public static CharClass negate(CharClass charClass)
	{
		if (charClass == null)
			throw new NullPointerException("charClass");
		StringBuilder builder = new StringBuilder();
		builder.append("[^");
		builder.append(charClass.toString());
		builder.append(']');
		return new CharClass(builder.toString());
	}

	/**
	 * Constructs a CharClass representing the union of all the specified
	 * CharClasses.
	 * 
	 * @param operands
	 * 	the character classes to combine.
	 * @return a CharClass that matches any character matched by any of the
	 * 	character classes specified by <code>operands</code>.
	 */
	public static CharClass union(CharClass... operands)
	{
		if (operands == null)
			throw new NullPointerException("operands");
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		for (CharClass operand : operands)
		{
			builder.append(operand.toString());
		}
		builder.append(']');
		return new CharClass(builder.toString());
	}

	
	/**
	 * Constructs a CharClass representing the intersection of all the
	 * specified character classes.  
	 * 
	 * @param operands
	 * 	the character classes to intersect.
	 * @return a CharClass that matches only those characters that are
	 * 	are matched by ALL character classes specified by 
	 * 	<code>operands</code>.
	 */
	public static CharClass intersection
		(CharClass... operands)
	{
		if (operands == null)
			throw new NullPointerException("operands");
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		boolean first = true;
		for (CharClass operand : operands)
		{
			if (!first)
				builder.append("&&");
			builder.append(operand.toString());
			first = false;
		}
		builder.append(']');
		return new CharClass(builder.toString());
	}

	
	/**
	 * Constructs a character class representing a range of characters
	 * or its inverse.
	 * 
	 * This method automatically escapes all characters that need
	 * escaping.
	 * 
	 * @param first the first character in the range.
	 * @param last the last character in the range.
	 * @param negate
	 * 	if true, the returned CharClass will match all characters EXCEPT
	 * 	those in the specified range; if false, the returned CharClass
	 * 	will match only those characters in the specified range.
	 * @return a CharClass matching all character values between 
	 * 	<code>first</code> and <code>last</code>, inclusive, or the inverse 
	 * 	of that (if <code>negate</code> is true).
	 */
	public static CharClass range(char first, char last, boolean negate)
	{
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if (negate)
			builder.append('^');
		if (needsEscape(first))
			builder.append('\\');
		builder.append(first);
		builder.append('-');
		if (needsEscape(last))
			builder.append('\\');
		builder.append(last);
		builder.append(']');
		return new CharClass(builder.toString());
	}

	
	/**
	 * Constructs a character class representing a range of characters.
	 * 
	 * This method is equivalent to calling
	 * <code>range(<i>first</i>, <i>last</i>, false)</code>.
	 * 
	 * @param first the first character in the range.
	 * @param last the last character in the range.
	 * @return a CharClass matching all character values between 
	 * 	<code>first</code> and <code>last</code>, inclusive.
	 * @see #range(char, char, boolean)
	 */
	public static CharClass range(char first, char last)
	{
		return range(first, last, false);
	}
	
	
	/**
	 * Constructs a CharClass matching all characters within the 
	 * specified named Unicode block, as defined by the
	 * {@link java.lang.Character} class.
	 * 
	 * @param blockName the name of the Unicode block to match.
	 * @return a CharClass matching the specified Unicode block.
	 * @throws NullPointerException if <code>blockName</code> is null.
	 * @throws IllegalArgumentException 
	 * 	if <code>blockName</code> does not name a valid Unicode block.
	 */
	public static CharClass unicodeBlock(String blockName)
	{
		UnicodeBlock.forName(blockName);
		StringBuilder builder = new StringBuilder();
		builder.append("\\p{In").append(blockName).append('}');
		return new CharClass(builder.toString());
	}
	
	/**
	 * Constructs a CharClass matching all characters NOT within the 
	 * specified named Unicode block, as defined by the
	 * {@link java.lang.Character} class.
	 * 
	 * @param blockName the name of the Unicode block to match.
	 * @return a CharClass matching characters not in the specified Unicode 
	 * 	block.
	 * @throws NullPointerException if <code>blockName</code> is null.
	 * @throws IllegalArgumentException 
	 * 	if <code>blockName</code> does not name a valid Unicode block.
	 */
	public static CharClass notUnicodeBlock(String blockName)
	{
		UnicodeBlock.forName(blockName);
		StringBuilder builder = new StringBuilder();
		builder.append("\\P{In").append(blockName).append('}');
		return new CharClass(builder.toString());
	}
	  
	private static final Set<String> UNICODE_CATEGORIES =
		new HashSet<String>(
				Arrays.asList(
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
			);
	
	
	
	/**
	 * Constructs a CharClass matching all characters within the 
	 * specified named Unicode category, as defined by the
	 * {@link java.lang.Character} class.
	 * 
	 * @param category the name of the Unicode category to match.
	 * @return a CharClass matching the specified Unicode category.
	 * @throws NullPointerException if <code>category</code> is null.
	 * @throws IllegalArgumentException 
	 * 	if <code>category</code> does not name a valid Unicode category.
	 */
	public static CharClass unicodeCategory(String category)
	{
		if (category == null)
			throw new NullPointerException("category");
		if (!UNICODE_CATEGORIES.contains(category))
			throw new IllegalArgumentException("category");
		StringBuilder builder = new StringBuilder();
		builder.append("\\p{Is").append(category).append('}');
		return new CharClass(builder.toString());
	}
	
	
	/**
	 * Constructs a CharClass matching all characters NOT within the 
	 * specified named Unicode category, as defined by the
	 * {@link java.lang.Character} class.
	 * 
	 * @param category the name of the Unicode category to match.
	 * @return a CharClass matching all characters NOT in the specified 
	 * 	Unicode category.
	 * @throws NullPointerException if <code>category</code> is null.
	 * @throws IllegalArgumentException 
	 * 	if <code>category</code> does not name a valid Unicode category.
	 */
	public static CharClass notUnicodeCategory(String category)
	{
		if (category == null)
			throw new NullPointerException("category");
		if (!UNICODE_CATEGORIES.contains(category))
			throw new IllegalArgumentException("category");
		StringBuilder builder = new StringBuilder();
		builder.append("\\P{Is").append(category).append('}');
		return new CharClass(builder.toString());
	}
	
}
