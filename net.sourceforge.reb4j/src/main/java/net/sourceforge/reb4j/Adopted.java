package net.sourceforge.reb4j;

import java.util.regex.Pattern;

public final class Adopted extends AbstractExpression 
{
	private static final long serialVersionUID = 1L;
	public final String expression;
	
	private Adopted(final String expression)
	{this.expression = expression;}
	
	@Override
	public String expression() 
	{return expression;}
	
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
