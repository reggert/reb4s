package net.sourceforge.reb4j;


public class Raw extends AbstractSequenceableAlternative
{
	private static final long serialVersionUID = 1L;
	private final String rawExpression;
	
	private Raw(final String rawExpression)
	{
		assert rawExpression != null;
		this.rawExpression = rawExpression;
	}

	@Override
	public final String expression()
	{return rawExpression;}
}


