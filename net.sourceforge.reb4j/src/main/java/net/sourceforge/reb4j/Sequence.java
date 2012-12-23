package net.sourceforge.reb4j;

import fj.F2;
import fj.data.List;
import net.sourceforge.reb4j.Alternation.Alternative;

public final class Sequence extends AbstractExpression
	implements Alternation.Alternative, SequenceOps
{
	private static final long serialVersionUID = 1L;
	public final List<Sequenceable> components;
	
	public Sequence(final Sequence left, final Sequence right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.components = left.components.append(right.components);
	}
	
	public Sequence(final Sequence left, final Sequenceable right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.components = left.components.append(List.single(right));
	}
	
	public Sequence(final Sequenceable left, final Sequence right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.components = right.components.cons(left);
	}
	
	public Sequence(final Sequenceable left, final Sequenceable right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.components = List.list(left, right);
	}

	@Override
	public String expression()
	{
		return components.tail().foldLeft(
				new F2<StringBuilder, Sequenceable, StringBuilder>() 
				{
					@Override
					public StringBuilder f(final StringBuilder a, final Sequenceable b)
					{return a.append(b.expression());}
				}, 
				new StringBuilder(components.head().expression())
			).toString();
	}

	@Override
	public Alternation or(final Alternation right) 
	{return new Alternation(this, right);}

	@Override
	public Alternation or(final Alternative right) 
	{return new Alternation(this, right);}
	
	@Override
	public Sequence then(final Sequenceable right)
	{return new Sequence(this, right);}

	@Override
	public Sequence then(final Sequence right)
	{return new Sequence(this, right);}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + components.hashCode();
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
		final Sequence other = (Sequence) obj;
		return components.equals(other.components);
	}

	public static interface Sequenceable extends Expression, SequenceOps
	{}
}



interface SequenceOps
{
	Sequence then(Sequence.Sequenceable right);
	Sequence then(Sequence right);
}