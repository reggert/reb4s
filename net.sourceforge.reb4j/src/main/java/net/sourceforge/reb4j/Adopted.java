package net.sourceforge.reb4j;

import java.util.regex.Pattern;

import fj.data.LazyString;

/**
 * Expression constructed by "adopting" the expression string from
 * an instance of {@link Pattern}.
 *
 * Note that adopted expressions must be constructed from instances of
 * {@link Pattern} and not raw {@link String}s, because the
 * {@link Pattern} class ensures that the expressions parse properly.
 */
public final class Adopted extends AbstractExpression 
{
	private static final long serialVersionUID = 1L;
	public final LazyString expression;
	
	private Adopted(final String expression)
	{this.expression = LazyString.str(expression);}
	
	@Override
	public LazyString expression() 
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
	public static Adopted fromPattern(final Pattern pattern)
	{
		if (pattern == null)
			throw new NullPointerException("pattern");
		return new Adopted(pattern.pattern());
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (expression.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Adopted other = (Adopted) obj;
		return expression.equals(other.expression);
	}
}
