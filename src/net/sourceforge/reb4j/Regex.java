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

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Represents a regular expression as defined by the 
 * {@link java.util.regex.Pattern} class.  
 * 
 * This class serves as a wrapper around the actual String expression.
 * It ensures that the expressions are well-formed and will not result in
 * {@link java.util.regex.PatternSyntaxException}s.  It also provides 
 * facilities to construct and compose complex expressions without resorting 
 * to (manually) building them from {@link java.lang.String}s.
 * 
 * Instances of this class are immutable and therefore thread safe.
 * 
 * The constructor for this class is not publicly visible.  Instances may
 * only be created using the class's static methods and by the non-static
 * methods of existing instances.
 * 
 * This class is not intended to be extended outside this package.
 * 
 * @author Richard W. Eggert II
 * @see CharClass
 *
 */
public class Regex implements Serializable 
{
	private static final long serialVersionUID = 1L;
	private final String expression;
	
	Regex(String expression)
	{
		assert expression != null;
		this.expression = expression;
	}

	/**
	 * This implementation returns true if and only if the underlying
	 * {@link java.lang.String} expressions are exactly equal 
	 * (equivalence is not sufficient).
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Regex)
		{
			Regex other = (Regex)obj;
			return this.expression.equals(other.expression);
		}
		return false;
	}

	/**
	 * This implementation returns the hash of the underlying String 
	 * expression.
	 */
	@Override
	public int hashCode()
	{
		return expression.hashCode();
	}

	/**
	 * This implementation returns the underlying {@link java.lang.String}
	 * expression that can be passed to 
	 * {@link java.util.regex.Pattern#compile(String)}.
	 */
	@Override
	public String toString()
	{
		return expression;
	}
	
	
	/**
	 * Creates a {@link java.util.regex.Pattern} that matches the expression
	 * represented by this object.  It invokes 
	 * {@link java.util.regex.Pattern#compile(String)} and returns the result.
	 * @return a compiled {@link java.util.regex.Pattern} object.
	 */
	public Pattern toPattern()
	{
		return Pattern.compile(expression);
	}
	
	
	/**
	 * Creates a {@link java.util.regex.Pattern} that matches the expression
	 * represented by this object.  It invokes 
	 * {@link java.util.regex.Pattern#compile(String, int)} and returns the 
	 * result.
	 * @param flags the flags to pass to 
	 * 	{@link java.util.regex.Pattern#compile(String, int)}.
	 * @return a compiled {@link java.util.regex.Pattern} object.
	 */
	public Pattern toPattern(int flags)
	{
		return Pattern.compile(expression, flags);
	}
	
	/**
	 * Constructs an instance of Regex from the specified 
	 * {@link java.util.regex.Pattern}.
	 * This method invokes {@link java.util.regex.Pattern#toString()} 
	 * in order to obtain the underlying {@link java.lang.String} expression.
	 * 
	 * @param pattern 
	 * 	the pattern from which to construct a Regex; must not be null.
	 * @return a Regex representing the regular expression matched by the
	 * 	specified Pattern.
	 * @throws NullPointerException if <code>pattern</code> is null.
	 */
	public static Regex fromPattern(Pattern pattern)
	{
		if (pattern == null)
			throw new NullPointerException("pattern");
		return new Regex(pattern.toString());
	}
	
	
	static boolean needsEscape(char c)
	{
		switch (c)
		{
			case '(':
			case ')':
			case '[':
			case ']':
			case '{':
			case '}':
			case '.':
			case ',':
			case '-':
			case '\\':
			case '|':
			case '+':
			case '*':
			case '?':
			case '$':
			case '^':
			case '&':
			case ':':
			case '!':
			case '<':
			case '>':
			case '=':
				return true;
		}
		return false;
	}
	
	
	static CharSequence escapeLiteral(CharSequence literal)
	{
		assert literal != null;
		StringBuilder builder = new StringBuilder();		
		for (int i = 0; i < literal.length(); i++)
		{
			char c = literal.charAt(i);
			if (needsEscape(c))
				builder.append('\\');
			builder.append(c);
		}
		return builder;
	}
	
	
	/**
	 * Constructs a Regex that matches the specified literal string.
	 * All punctuation characters that have meaning to the 
	 * {@link java.util.regex.Pattern} syntax are escaped.
	 * 
	 * @param literal the literal string to be matched by the resultant
	 * 	expression.
	 * @return a Regex that matches the (escaped) literal.
	 * @throws NullPointerException if <code>literal</code> is null.
	 */
	public static Regex literal(CharSequence literal)
	{
		if (literal == null)
			throw new NullPointerException("literal");
		StringBuilder builder = new StringBuilder();
		// bound the literal as a non-capturing group
		builder.append("(?:");
		builder.append(escapeLiteral(literal));
		builder.append(')');
		return new Regex(builder.toString());
	}
	
	
	/**
	 * Constructs a Regex composed of the specified sequence of
	 * sub-expressions.
	 * 
	 * @param regexes the sub-expressions to compose.
	 * @return a Regex that matches the specified sub-expressions, in order.
	 * @throws NullPointerException
	 * 	if <code>regexes</code> or any of its elements is null.
	 */
	public static Regex sequence(Regex... regexes)
	{
		if (regexes == null)
			throw new NullPointerException("regexes");
		StringBuilder builder = new StringBuilder();
		builder.append("(?:");
		for (Regex regex : regexes)
		{
			builder.append(regex.toString());
		}
		builder.append(')');
		return new Regex(builder.toString());
	}
	
	
	
	/**
	 * Constructs a Regex composed of the current expression followed
	 * by the specified sub-expression.
	 * 
	 * Equivalent to <code>sequence(this, <i>second</i>)</code>.
	 * 
	 * @param second the sub-expression to append to this expression in order
	 * 	to form the composite expression.
	 * @return a Regex that matches this expression followed by 
	 * 	<code>second</code>.
	 * @throws NullPointerException if <code>second</code> is null.
	 */
	public Regex then(Regex second)
	{
		if (second == null)
			throw new NullPointerException("second");
		return sequence(this, second);
	}
	
	
	/**
	 * Constructs a Regex that matches any of the specified
	 * sub-expressions.
	 * 
	 * @param regexes the sub-expressions to compose
	 * @return a Regex representing the "OR" of all the specified expressions.
	 * @see #or(Regex)
	 * @throws NullPointerException 
	 * 	if <code>regexes</code> or any of its elements is null.
	 */
	public static Regex or(Regex... regexes)
	{
		if (regexes == null)
			throw new NullPointerException("regexes");
		
		StringBuilder builder = new StringBuilder();
		builder.append("(?:");
		boolean first = true;
		for (Regex regex : regexes)
		{
			if (!first)
				builder.append('|');
			builder.append(regex);
			first = false;
		}
		builder.append(')');
		return new Regex(builder.toString());
	}
	
	
	/**
	 * Constructs a Regex that matches any string that matches this Regex OR 
	 * the specified alternate Regex.
	 * @param second the alternate Regex
	 * @return a Regex representing the "OR" of this Regex and
	 * 	<code>second</code>.
	 * @see #or(Regex...)
	 * @throws NullPointerException if <code>second</code> is null.
	 */
	public Regex or(Regex second)
	{
		if (second == null)
			throw new NullPointerException("second");
		StringBuilder builder = new StringBuilder();
		builder.append("(?:")
			.append(toString())
			.append('|')
			.append(second.toString())
			.append(')');
		return new Regex(builder.toString());
	}
	
	
	/**
	 * Constructs a Regex that matches this Regex repeated a number of times
	 * between the specified limits.
	 * 
	 * Equivalent to the regular expression phrase 
	 * <code>{<i>minTimes</i>, <i>maxTimes</i>}</code>.
	 * 
	 * @param minTimes 
	 * 	the minimum number of times that this Regex must be repeated in order 
	 * 	for the new Regex to match.
	 * @param maxTimes
	 * 	the maximum number of times that this Regex may be repeated for the
	 * 	new Regex to match.
	 * @param mode
	 * 	whether to use a greedy, reluctant (?), or possessive (+) quantifier.
	 * @return a Regex that matches this Regex repeated the specified number
	 * 	times.
	 * @throws NullPointerException if <code>mode</code> is null.
	 * @throws IllegalArgumentException 
	 * 	if <code>minTimes</code> is negative or <code>maxTimes</code> is 
	 * 	less than <code>minTimes</code>.
	 */
	public Regex repeat(int minTimes, int maxTimes, QuantifierMode mode)
	{
		if (minTimes < 0)
			throw new IllegalArgumentException("minTimes < 0");
		if (maxTimes < minTimes)
			throw new IllegalArgumentException("maxTimes < minTimes");
		if (mode == null)
			throw new NullPointerException("mode");
		StringBuilder builder = new StringBuilder();
		builder.append("(?:")
			.append(toString())
			.append('{')
			.append(minTimes);
		if (maxTimes != minTimes)
			builder.append(',').append(maxTimes);
		builder.append('}');
		switch (mode)
		{
			case RELUCTANT: builder.append('?'); break;
			case POSSESSIVE: builder.append('+'); break;
		}
		builder.append(')');
		return new Regex(builder.toString());
	}
	
	
	/**
	 * Constructs a Regex that matches this Regex repeated a number of times
	 * between the specified limits.
	 * 
	 * This method is equivalent to 
	 * <code>repeat(minTimes, maxTimes, {@link QuantifierMode#GREEDY})</code>.
	 * 
	 * @param minTimes 
	 * 	the minimum number of times that this Regex must be repeated in order 
	 * 	for the new Regex to match.
	 * @param maxTimes
	 * 	the maximum number of times that this Regex may be repeated for the
	 * 	new Regex to match.
	 * @return a Regex that matches this Regex repeated the specified number
	 * 	times.
	 * @see #repeat(int, int, QuantifierMode)
	 * @throws IllegalArgumentException 
	 * 	if <code>minTimes</code> is negative or <code>maxTimes</code> is 
	 * 	less than <code>minTimes</code>.
	 */
	public Regex repeat(int minTimes, int maxTimes)
	{return repeat(minTimes, maxTimes, QuantifierMode.GREEDY);}
	
	
	/**
	 * Constructs a Regex that matches this Regex repeated a specified number 
	 * of times.
	 * 
	 * This method is equivalent to 
	 * <code>repeat(times, times, {@link QuantifierMode#GREEDY})</code>.
	 * 
	 * @param times 
	 * 	the exact number of times that this Regex must be repeated in order 
	 * 	for the new Regex to match.
	 * @return a Regex that matches this Regex repeated the specified number
	 * 	times.
	 * @see #repeat(int, int, QuantifierMode)
	 * @throws IllegalArgumentException 
	 * 	if <code>times</code> is not positive.
	 */
	public Regex repeat(int times)
	{
		if (times <= 0)
			throw new IllegalArgumentException("times <= 0");
		return repeat(times, times, QuantifierMode.GREEDY);
	}
	
	
	/**
	 * Constructs a Regex that matches this Regex repeated a specified number 
	 * of times.
	 * 
	 * This method is equivalent to 
	 * <code>repeat(times, times, mode)</code>.
	 * 
	 * @param times 
	 * 	the exact number of times that this Regex must be repeated in order 
	 * 	for the new Regex to match.
	 * @param mode
	 * 	whether to use a greedy, reluctant (?), or possessive (+) quantifier.
	 * @return a Regex that matches this Regex repeated the specified number
	 * 	times.
	 * @see #repeat(int, int, QuantifierMode)
	 * @throws NullPointerException if <code>mode</code> is null.
	 * @throws IllegalArgumentException 
	 * 	if <code>times</code> is not positive.
	 */
	public Regex repeat(int times, QuantifierMode mode)
	{
		if (times <= 0)
			throw new IllegalArgumentException("times <= 0");
		return repeat(times, times, mode);
	}
	
	

	/**
	 * Constructs a Regex that matches this Regex repeated at least the
	 * specified number of times.
	 * 
	 * Equivalent to the regular expression phrase 
	 * <code>{<i>minTimes</i>,}</code>.
	 * 
	 * @param minTimes
	 * 	the minimum number of times that this Regex must be repeated in order
	 * 	for the new Regex to match.
	 * @param mode
	 * 	whether to use a greedy, reluctant (?), or possessive (+) quantifier.
	 * @return a Regex that matches this Regex repeated the specified number
	 * 	of times.
	 * @throws NullPointerException if <code>mode</code> is null.
	 * @throws IllegalArgumentException if <code>minTimes</code> is negative.
	 */
	public Regex atLeast(int minTimes, QuantifierMode mode)
	{
		if (minTimes < 0)
			throw new IllegalArgumentException("minTimes < 0");
		if (mode == null)
			throw new NullPointerException("mode");
		StringBuilder builder = new StringBuilder();
		builder.append("(?:")
			.append(toString())
			.append('{')
			.append(minTimes)
			.append(',')
			.append('}');
		switch (mode)
		{
			case RELUCTANT: builder.append('?'); break;
			case POSSESSIVE: builder.append('+'); break;
		}
		builder.append(')');
		return new Regex(builder.toString());
	}
	
	
	
	/**
	 * Constructs a Regex that matches this Regex repeated at least the
	 * specified number of times.
	 * 
	 * This method is equivalent to calling
	 * <code>atLeast(<i>minTimes</i>, {@link QuantifierMode#GREEDY})</code>.
	 * 
	 * @param minTimes
	 * 	the minimum number of times that this Regex must be repeated in order
	 * 	for the new Regex to match.
	 * @return a Regex that matches this Regex repeated the specified number
	 * 	of times.
	 * @see #atLeast(int, QuantifierMode)
	 * @throws IllegalArgumentException if <code>minTimes</code> is negative.
	 */
	public Regex atLeast(int minTimes)
	{return atLeast(minTimes, QuantifierMode.GREEDY);}
	

	
	/**
	 * Constructs a Regex that matches this Regex occurring exactly once or
	 * not at all.
	 * 
	 * Equivalent to the regular expression symbol <b>?</b>.
	 * 
	 * @param mode
	 * 	whether to use a greedy, reluctant (?), or possessive (+) quantifier.
	 * @return a Regex that matches this Regex occurring once or not at all.
	 * @throws NullPointerException if <code>mode</code> is null.
	 */
	public Regex optional(QuantifierMode mode)
	{
		if (mode == null)
			throw new NullPointerException("mode");
		StringBuilder builder = new StringBuilder();
		builder.append("(?:").append(toString()).append('?');
		switch (mode)
		{
			case RELUCTANT: builder.append('?'); break;
			case POSSESSIVE: builder.append('+'); break;
		}
		builder.append(')');
		return new Regex(builder.toString());
	}
	
	
	/**
	 * Constructs a Regex that matches this Regex occurring exactly once or
	 * not at all.
	 * 
	 * This method is equivalent to calling
	 * <code>optional({@link QuantifierMode#GREEDY})</code>.
	 * 
	 * @return a Regex that matches this Regex occurring once or not at all.
	 * @see #optional(QuantifierMode)
	 */
	public Regex optional()
	{return optional(QuantifierMode.GREEDY);}
	
	
	/**
	 * Constructs a Regex that matches this Regex repeated any number 
	 * of times, including not at all.
	 * 
	 * Equivalent to the regular expression symbol <b>*</b>.
	 * 
	 * @param mode
	 * 	whether to use a greedy, reluctant (?), or possessive (+) quantifier.
	 * @return a Regex that matches this Regex repeated any number of times.
	 * @throws NullPointerException if <code>mode</code> is null.
	 */
	public Regex star(QuantifierMode mode)
	{
		if (mode == null)
			throw new NullPointerException("mode");
		StringBuilder builder = new StringBuilder();
		builder.append("(?:").append(toString()).append('*');
		switch (mode)
		{
			case RELUCTANT: builder.append('?'); break;
			case POSSESSIVE: builder.append('+'); break;
		}
		builder.append(')');
		return new Regex(builder.toString());
	}
	
	
	/**
	 * Constructs a Regex that matches this Regex repeated any number 
	 * of times, including not at all.
	 * 
	 * This method is equivalent to calling
	 * <code>star({@link QuantifierMode#GREEDY})</code>.
	 * 
	 * @return a Regex that matches this Regex repeated any number of times.
	 * @see #star(QuantifierMode)
	 */
	public Regex star()
	{return star(QuantifierMode.GREEDY);}
	
	
	/**
	 * Constructs a Regex that matches this Regex repeated any number of times,
	 * but occurring at least once.
	 * 
	 * Equivalent to the regular expression symbol <b>+</b>.
	 * 
	 * @param mode
	 * 	whether to use a greedy, reluctant (?), or possessive (+) quantifier.
	 * @return a Regex that matches this Regex repeated at least once.
	 * @throws NullPointerException if <code>mode</code> is null.
	 */
	public Regex atLeastOnce(QuantifierMode mode)
	{
		if (mode == null)
			throw new NullPointerException("mode");
		StringBuilder builder = new StringBuilder();
		builder.append("(?:").append(toString()).append('+');
		switch (mode)
		{
			case RELUCTANT: builder.append('?'); break;
			case POSSESSIVE: builder.append('+'); break;
		}
		builder.append(')');
		return new Regex(builder.toString());
	}
	
	
	
	/**
	 * Constructs a Regex that matches this Regex repeated any number of times,
	 * but occurring at least once.
	 * 
	 * This method is equivalent to calling
	 * <code>atLeastOnce({@link QuantifierMode#GREEDY})</code>.
	 * 
	 * @return a Regex that matches this Regex repeated at least once.
	 * @see #atLeastOnce(QuantifierMode)
	 */
	public Regex atLeastOnce()
	{return atLeastOnce(QuantifierMode.GREEDY);}
	
	
	/**
	 * Constructs a Regex representing a "back reference" to the specified
	 * numbered capturing group.
	 * 
	 * Equivalent to the regular expression phrase
	 * <code>\<i>group</i></code>
	 * 
	 * @param group
	 * 	the number of the group to reference; must be greater than 0.
	 * @return a Regex representing the specified back reference.
	 * @throws IllegalArgumentException 
	 * 	if <code>group</code> is not greater than 0.
	 */
	public static Regex backReference(int group)
	{
		if (group <= 0)
			throw new IllegalArgumentException("group <= 0");
		StringBuilder builder = new StringBuilder();
		builder.append("(?:\\").append(group).append(')');
		return new Regex(builder.toString());
	}
	
	
	/**
	 * Constructs a Regex representing this Regex as a capturing group.
	 * 
	 * Equivalent to wrapping the expression in plain parentheses.
	 * 
	 * @return a Regex representing a capturing group.
	 */
	public Regex group()
	{
		StringBuilder builder = new StringBuilder();
		builder.append('(').append(toString()).append(')');
		return new Regex(builder.toString());
	}
	
	
	/**
	 * Constructs a Regex representing this Regex as a non-capturing group
	 * that uses look-ahead.
	 * 
	 * Equivalent to the regular expression phrase
	 * <code>(?=</code> (for positive look-ahead) or
	 * <code>(?!</code> (for negative look-ahead).
	 * 
	 * @param positive
	 * 	if true, positive look-ahead is used; if false, negative look-ahead is used.
	 * @return a Regex that uses look-ahead.
	 */
	public Regex lookAhead(boolean positive)
	{
		StringBuilder builder = new StringBuilder();
		builder.append('(').append('?');
		if (positive)
			builder.append('=');
		else
			builder.append('!');
		builder.append(toString()).append(')');
		return new Regex(builder.toString());
	}
	
	

	
	/**
	 * Constructs a Regex representing this Regex as a non-capturing group
	 * that uses look-behind.
	 * 
	 * Equivalent to the regular expression phrase
	 * <code>(?&lt;=</code> (for positive look-behind) or
	 * <code>(?&lt;!</code> (for negative look-behind).
	 * 
	 * @param positive
	 * 	if true, positive look-behind is used; if false, negative look-behind is used.
	 * @return a Regex that uses look-behind.
	 */
	public Regex lookBehind(boolean positive)
	{
		StringBuilder builder = new StringBuilder();
		builder.append('(').append('?').append('<');
		if (positive)
			builder.append('=');
		else
			builder.append('!');
		builder.append(toString()).append(')');
		return new Regex(builder.toString());
	}
	
	
	
	
	/**
	 * Constructs a Regex that wraps this Regex as an independent, 
	 * non-capturing group.
	 * 
	 * Equivalent to the regular expression phrase
	 * <code>(?&gt;</code>
	 * 
	 * @return a Regex that represents an independent, non-capturing group.
	 */
	public Regex independent()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("(?>").append(toString()).append(')');
		return new Regex(builder.toString());
	}
	
	
	
	
	private static void validateFlag(char flag)
	{
		switch (flag)
		{
			case 'i':
			case 'd':
			case 'm':
			case 's':
			case 'u':
			case 'x':
				break;
			default:
				throw new IllegalArgumentException("flag");
		}
	}
	 
	
	/**
	 * Constructs a Regex that represents this Regex with the specified flag 
	 * enabled or disabled.
	 * 
	 * @param flag
	 * 	the flag to enable or disable; 
	 * 	must be one of 'i', 'd', 'm', 's', 'u', or 'x'.
	 * @param on
	 * 	if true, the flag will be enabled; if false, the flag will be disabled.
	 * @return a Regex that enables or disables the specified flag.
	 * @throws IllegalArgumentException
	 * 	if <code>flag</code> is not one of 'i', 'd', 'm', 's', 'u', or 'x'.
	 */
	public Regex setFlag(char flag, boolean on)
	{
		validateFlag(flag);
		StringBuilder builder = new StringBuilder();
		builder.append('(').append('?');
		if (!on)
			builder.append('-');
		builder.append(flag)
			.append(':')
			.append(toString())
			.append(')');
		return new Regex(builder.toString());
	}
	
	
	
	/**
	 * Constructs an empty Regex that enables or disables the specified flag.
	 * 
	 * @param flag
	 * 	the flag to enable or disable; 
	 * 	must be one of 'i', 'd', 'm', 's', 'u', or 'x'.
	 * @param on
	 * 	if true, the flag will be enabled; if false, the flag will be disabled.
	 * @return a Regex that enables or disables the specified flag.
	 * @throws IllegalArgumentException
	 * 	if <code>flag</code> is not one of 'i', 'd', 'm', 's', 'u', or 'x'.
	 */
	public static Regex flagDirective(char flag, boolean on)
	{
		validateFlag(flag);
		StringBuilder builder = new StringBuilder();
		builder.append('(').append('?');
		if (!on)
			builder.append('-');
		builder.append(flag).append(')');
		return new Regex(builder.toString());
	}
	
	
	/**
	 * Matches the beginning of a line.
	 */
	public static final Regex LINE_BEGIN = new Regex("^");
	
	/**
	 * Matches the end of a line.
	 */
	public static final Regex LINE_END = new Regex("$");
	
	/**
	 * Matches a word boundary.
	 */
	public static final Regex WORD_BOUNDARY = new Regex("\\b");
	
	/**
	 * Matches a non-word boundary.
	 */
	public static final Regex NONWORD_BOUNDARY = new Regex("\\B");
	
	/**
	 * Matches the beginning of input.
	 */
	public static final Regex INPUT_BEGIN = new Regex("\\A");
	
	/**
	 * Matches the end of the previous match.
	 */
	public static final Regex MATCH_END = new Regex("\\G");
	
	/**
	 * Matches the end of input, ignoring the last line terminator.
	 */
	public static final Regex INPUT_END_SKIP_EOL = new Regex("\\Z");
	
	/**
	 * Matches the absolute end of input.
	 */
	public static final Regex INPUT_END = new Regex("\\z");
}
