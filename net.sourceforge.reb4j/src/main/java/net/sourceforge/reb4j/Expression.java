package net.sourceforge.reb4j;

import java.io.Serializable;
import java.util.regex.Pattern;

public abstract class Expression implements Serializable 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Returns the regular expression represented by this object, in a form 
	 * suitable to passing to the {@link java.util.regex.Pattern} class.
	 */
	public abstract String expression();
	
	/**
	 * Always returns the same value as {@link net.sourceforge.reb4j.Expression#expression}.
	 */
	@Override
	public final String toString()
	{return expression();}
	
	/**
	 * Passes the regular expression represented by this object to 
	 * {@link java.util.regex.Pattern} and returns the result.
	 */
	public final Pattern toPattern()
	{return Pattern.compile(expression());}
	
	/**
	 * Adopts the regular expression represented by the specified
	 * {@link java.util.regex.Pattern}.
	 * 
	 * @param pattern
	 * 	the regular expression to adopt; must not be <code>null</code>.
	 * @throws NullPointerException
	 * 	if <var>pattern</var> is <code>null</code>.
	 */
	public static Adopted adopt(final Pattern pattern)
	{
		if (pattern == null)
			throw new NullPointerException("pattern");
		return new Adopted(pattern.pattern());
	}
}
