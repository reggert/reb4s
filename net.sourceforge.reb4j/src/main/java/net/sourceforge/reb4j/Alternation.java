package net.sourceforge.reb4j;

import fj.F2;
import fj.data.List;

public final class Alternation extends AbstractExpression
	implements AlternationOps
{
	private static final long serialVersionUID = 1L;
	private final List<Alternative> alternatives;
	
	public Alternation(final Alternation left, final Alternation right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.alternatives = left.alternatives.append(right.alternatives);
	}
	
	public Alternation(final Alternation left, final Alternative right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.alternatives = left.alternatives.append(List.single(right));
	}
	
	public Alternation(final Alternative left, final Alternation right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.alternatives = right.alternatives.cons(left);
	}
	
	public Alternation(final Alternative left, final Alternative right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.alternatives = List.list(left, right);
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
	{return new Alternation(this, right);}

	@Override
	public Alternation or(final Alternative right) 
	{return new Alternation(this, right);}
	
	public static interface Alternative extends Expression, AlternationOps
	{}
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
