package net.sourceforge.reb4j;

public final class Adopted extends Expression 
{
	private static final long serialVersionUID = 1L;
	private final String expression;
	
	Adopted(final String expression)
	{this.expression = expression;}
	
	@Override
	public String expression() 
	{return expression;}
}
