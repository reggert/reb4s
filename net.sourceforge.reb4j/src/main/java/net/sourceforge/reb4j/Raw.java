package net.sourceforge.reb4j;

import fj.F2;
import fj.data.LazyString;
import fj.data.List;


/**
 * Base class for expressions that are readily combined with other similar expressions.
 * 
 * This encompasses special predefined expressions, processed literals, 
 * and combinations thereof. 
 * This class mainly exists so that long sequences of such expressions can be flattened rather
 * than causing the construction of deep trees.
 */
public class Raw extends AbstractSequenceableAlternative
{
	private static final long serialVersionUID = 1L;
	private final LazyString rawExpression;
	
	Raw(final LazyString rawExpression)
	{
		assert rawExpression != null;
		this.rawExpression = rawExpression;
	}

	@Override
	public final LazyString expression()
	{return rawExpression;}
	
	/**
	 * Overloaded version of {@link Sequence.Sequenceable#then(net.sourceforge.reb4j.Sequence.Sequenceable)} 
	 * for when the argument is an instance of {@link Raw}.
	 * 
	 * @param right an instance of {@link Raw}; must not be <code>null</code>.
	 * @return an instance of {@link Compound}.
	 * @throws NullPointerException
	 * 	if <var>right</var> is <code>null</code>.
	 */
	public Compound then(final Raw right)
	{
		if (right == null) throw new NullPointerException("right");
		return new Compound(List.list(this, right));
	}

	/**
	 * Overloaded version of {@link Sequence.Sequenceable#then(net.sourceforge.reb4j.Sequence.Sequenceable)} 
	 * for when the argument is an instance of {@link Compound}.
	 * 
	 * @param right an instance of {@link Compound}; must not be <code>null</code>.
	 * @return an instance of {@link Compound}.
	 * @throws NullPointerException
	 * 	if <var>right</var> is <code>null</code>.
	 */
	public Compound then(final Compound right)
	{
		if (right == null) throw new NullPointerException("right");
		return new Compound(right.components.cons(this));
	}

	/**
	 * Overloaded version of {@link Sequence.Sequenceable#then(net.sourceforge.reb4j.Sequence.Sequenceable)} 
	 * for when the argument is an instance of {@link Literal}.
	 * 
	 * @param right an instance of {@link Literal}; must not be <code>null</code>.
	 * @return an instance of {@link Compound}.
	 * @throws NullPointerException
	 * 	if <var>right</var> is <code>null</code>.
	 */
	public Compound then(final Literal right)
	{
		if (right == null) throw new NullPointerException("right");
		return this.then(new EscapedLiteral(right));
	}

	/**
	 * Expression consisting of multiple 
	 * @author Rich
	 *
	 */
	public static final class Compound extends Raw
	{
		private static final long serialVersionUID = 1L;
		public final List<Raw> components;
		
		private Compound(final List<Raw> components)
		{
			super(compoundExpression(components));
			this.components = components;
		}
		
		private static LazyString compoundExpression(final List<Raw> components)
		{
			return components.foldLeft(
					new F2<LazyString, Raw, LazyString>()
					{
						@Override
						public LazyString f(final LazyString a, final Raw b)
						{return a.append(b.rawExpression);}
					},
					LazyString.empty
				);
		}
		
		@Override
		public Compound then(final Raw right)
		{
			return new Compound(components.append(List.single(right)));
		}
		
		@Override
		public Compound then(final Compound right)
		{
			return new Compound(components.append(right.components));
		}
		
	}
	
	
	/**
	 * Adapter from {@link Literal} to {@link Raw} to facilitate
	 * merging literals.
	 */
	public static final class EscapedLiteral extends Raw
	{
		private static final long serialVersionUID = 1L;
		public final Literal literal;
		
		private EscapedLiteral(final Literal literal)
		{
			super(literal.escaped());
			this.literal = literal;
		}
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + rawExpression.hashCode();
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
		final Raw other = (Raw) obj;
		return rawExpression.equals(other.rawExpression);
	}
	
}



