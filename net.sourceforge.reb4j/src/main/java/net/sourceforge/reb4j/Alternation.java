package net.sourceforge.reb4j;

import fj.F2;
import fj.data.List;

public final class Alternation extends AbstractExpression
	implements AlternationOps
{
	private static final long serialVersionUID = 1L;
	private final List<Alternative> alternatives;
	
	
	private Alternation(final List<Alternative> alternatives)
	{
		assert alternatives != null;
		this.alternatives = alternatives;
	}
	
	@Override
	public String expression() 
	{
		return alternatives.tail().foldLeft(
				new F2<StringBuilder, Alternative, StringBuilder>() 
				{
					@Override
					public StringBuilder f(final StringBuilder a, final Alternative b)
					{return a.append('|').append(b.expression());}
				}, 
				new StringBuilder(alternatives.head().expression())
			).toString();
	}


	@Override
	public Alternation or(final Alternation right) 
	{
		return new Alternation(this.alternatives.append(right.alternatives));
	}


	@Override
	public Alternation or(final Alternative right) 
	{
		return new Alternation(this.alternatives.append(List.single(right)));
	}
	
	
	public static interface Alternative extends Expression, AlternationOps
	{
		
		
	}
}


interface AlternationOps
{
	/**
	 * Constructs an expression matching either the receiver or the 
	 * any of the alternatives contained within the specified argument 
	 * expression.
	 */
	Alternation or(Alternation right);
	
	/**
	 * Constructs an expression matching either the receiver or the 
	 * specified argument expression.
	 */
	Alternation or(Alternation.Alternative right);
}
