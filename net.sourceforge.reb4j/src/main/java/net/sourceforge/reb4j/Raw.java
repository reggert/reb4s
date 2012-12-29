package net.sourceforge.reb4j;

import fj.F2;
import fj.data.LazyString;
import fj.data.List;


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
	
	public Compound then(final Raw right)
	{
		return new Compound(List.list(this, right));
	}

	public Compound then(final Compound right)
	{
		return new Compound(right.components.cons(this));
	}

	public Compound then(final Literal right)
	{
		return this.then(new EscapedLiteral(right));
	}

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



