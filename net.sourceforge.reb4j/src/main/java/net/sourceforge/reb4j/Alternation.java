package net.sourceforge.reb4j;

import fj.F2;
import fj.data.LazyString;
import fj.data.List;

/**
 * Expression representing a set of alternatives that may be matched.
 */
public final class Alternation extends AbstractExpression
	implements AlternationOps
{
	private static final long serialVersionUID = 1L;
	public final List<Alternative> alternatives;
	
	private Alternation(final List<Alternative> alternatives)
	{
		if (alternatives == null) throw new NullPointerException("alternatives");
		this.alternatives = alternatives;
	}
	
	/**
	 * Constructs a new alternation representing the union of two existing
	 * alternations.
	 * 
	 * @param left
	 * 	an alternation; must not be <code>null</code>.
	 * @param right
	 *  an alternation; must not be <code>null</code>.
	 * @throws NullPointerException
	 * 	if either argument is null.
	 */
	Alternation(final Alternation left, final Alternation right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.alternatives = left.alternatives.append(right.alternatives);
	}
	
	/**
	 * Constructs a new alternation representing the union of an existing
	 * alternation and an alternative to be appended to the end.
	 * 
	 * @param left
	 * 	an alternation; must not be <code>null</code>.
	 * @param right
	 *  an alternative to be appended; must not be <code>null</code>.
	 * @throws NullPointerException
	 * 	if either argument is null.
	 */
	Alternation(final Alternation left, final Alternative right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.alternatives = left.alternatives.append(List.single(right));
	}
	
	/**
	 * Constructs a new alternation representing the union of an existing
	 * alternation and an alternative to be prepended to the beginning.
	 * 
	 * @param left
	 * 	an alternative to be prepended; must not be <code>null</code>.
	 * @param right
	 *  an alternation; must not be <code>null</code>.
	 * @throws NullPointerException
	 * 	if either argument is null.
	 */
	Alternation(final Alternative left, final Alternation right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.alternatives = right.alternatives.cons(left);
	}
	
	/**
	 * Constructs a new alternation that may match either of two alternatives.
	 * 
	 * @param left
	 * 	the first alternative; must not be <code>null</code>.
	 * @param right
	 *  the second alternative; must not be <code>null</code>.
	 * @throws NullPointerException
	 * 	if either argument is null.
	 */
	Alternation(final Alternative left, final Alternative right)
	{
		if (left == null) throw new NullPointerException("left");
		if (right == null) throw new NullPointerException("right");
		this.alternatives = List.list(left, right);
	}
	
	public static Alternation alternatives(
			final Alternative first, 
			final Alternative second, 
			final Alternative... rest
		)
	{
		if (first == null) throw new NullPointerException("first");
		if (second == null) throw new NullPointerException("second");
		if (rest == null) throw new NullPointerException("rest");
		return new Alternation(List.list(rest).cons(second).cons(first));
	}
	
	@Override
	public LazyString expression() 
	{
		return alternatives.tail().foldLeft(
				new F2<LazyString, Alternative, LazyString>() 
				{
					@Override
					public LazyString f(final LazyString a, final Alternative b)
					{return a.append("|").append(b.expression());}
				}, 
				alternatives.head().expression()
			);
	}

	@Override
	public Alternation or(final Alternation right) 
	{return new Alternation(this, right);}

	@Override
	public Alternation or(final Alternative right) 
	{return new Alternation(this, right);}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + alternatives.hashCode();
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
		final Alternation other = (Alternation) obj;
		return alternatives.equals(other.alternatives);
	}
	
	/**
	 * Interface indicating that an expression can be used as an alternative in an alternation.
	 * 
	 * This interface is not intended to be implemented by clients.
	 */
	public static interface Alternative extends Expression, AlternationOps
	{}
}


/**
 * Operations that can be performed to create alternations.
 * 
 * This interface is not intended to be implemented by clients.
 */
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
