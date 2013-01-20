package net.sourceforge.reb4j;


/**
 * Abstract class providing the canonical implementation of {@link Alternative}.
 */
public abstract class AbstractAlternative extends AbstractExpression 
	implements Alternative
{
	private static final long serialVersionUID = 1L;

	@Override
	public final Alternation or(final Alternation right) 
	{return new Alternation(this, right);}

	@Override
	public final Alternation or(final Alternative right) 
	{return new Alternation(this, right);}

}
