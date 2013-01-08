package net.sourceforge.reb4j;

import java.util.regex.Pattern;

public abstract class AbstractExpression implements Expression 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Always returns the same value as {@link net.sourceforge.reb4j.Expression#expression}.
	 */
	@Override
	public final String toString()
	{return expression().toString();}

	@Override
	public final Pattern toPattern()
	{return Pattern.compile(expression().toString());}
	

}
