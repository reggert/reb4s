package net.sourceforge.reb4j;

public final class Quantified extends AbstractSequenceableAlternative
{
	private static final long serialVersionUID = 1L;
	public final Quantifiable base;
	public final String quantifier;
	
	private Quantified(final Quantifiable base, final String quantifier)
	{
		if (base == null) throw new NullPointerException("base");
		if (quantifier == null) throw new NullPointerException("quantifier");
		this.base = base;
		this.quantifier = quantifier;
	}
	
	public static Quantified anyTimes(final Quantifiable base)
	{
		return new Quantified(base, "*");
	}
	
	public static Quantified anyTimesReluctantly(final Quantifiable base)
	{
		return new Quantified(base, "*?");
	}
	
	public static Quantified anyTimesPossessively(final Quantifiable base)
	{
		return new Quantified(base, "*+");
	}
	
	public static Quantified atLeastOnce(final Quantifiable base)
	{
		return new Quantified(base, "+");
	}
	
	public static Quantified atLeastOnceReluctantly(final Quantifiable base)
	{
		return new Quantified(base, "+?");
	}
	
	public static Quantified atLeastOncePossessively(final Quantifiable base)
	{
		return new Quantified(base, "++");
	}
	
	public static Quantified optional(final Quantifiable base)
	{
		return new Quantified(base, "?");
	}
	
	public static Quantified optionalReluctantly(final Quantifiable base)
	{
		return new Quantified(base, "??");
	}
	
	public static Quantified optionalPossessively(final Quantifiable base)
	{
		return new Quantified(base, "?+");
	}
	
	public static Quantified repeat(final Quantifiable base, final int n)
	{
		return new Quantified(base, "{" + n + "}");
	}
	
	public static Quantified repeatReluctantly(final Quantifiable base, final int n)
	{
		return new Quantified(base, "{" + n + "}?");
	}
	
	public static Quantified repeatPossessively(final Quantifiable base, final int n)
	{
		return new Quantified(base, "{" + n + "}+");
	}
	
	public static Quantified repeat(final Quantifiable base, final int min, final int max)
	{
		return new Quantified(base, new StringBuilder().append('{').append(min).append(',').append(max).append('}').toString());
	}
	
	public static Quantified repeatReluctantly(final Quantifiable base, final int min, final int max)
	{
		return new Quantified(base, new StringBuilder().append('{').append(min).append(',').append(max).append("}?").toString());
	}
	
	public static Quantified repeatPossessively(final Quantifiable base, final int min, final int max)
	{
		return new Quantified(base, new StringBuilder().append('{').append(min).append(',').append(max).append("}+").toString());
	}
	
	public static Quantified atLeast(final Quantifiable base, final int n)
	{
		return new Quantified(base, "{" + n + ",}");
	}
	
	public static Quantified atLeastReluctantly(final Quantifiable base, final int n)
	{
		return new Quantified(base, "{" + n + ",}?");
	}
	
	public static Quantified atLeastPossessively(final Quantifiable base, final int n)
	{
		return new Quantified(base, "{" + n + ",}+");
	}

	@Override
	public String expression()
	{
		return base.expression() + quantifier;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + base.hashCode();
		result = prime * result + quantifier.hashCode();
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
		final Quantified other = (Quantified) obj;
		return base.equals(other.base) && quantifier.equals(other.quantifier);
	}

}
