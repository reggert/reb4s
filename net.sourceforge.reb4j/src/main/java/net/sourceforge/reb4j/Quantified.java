package net.sourceforge.reb4j;

import fj.data.LazyString;

/**
 * Expression that has a quantifier attached to it.
 */
public final class Quantified extends AbstractSequenceableAlternative
{
	private static final long serialVersionUID = 1L;
	public final Quantifiable base;
	private final LazyString quantifier;
	
	private Quantified(final Quantifiable base, final LazyString quantifier)
	{
		if (base == null) throw new NullPointerException("base");
		if (quantifier == null) throw new NullPointerException("quantifier");
		this.base = base;
		this.quantifier = quantifier;
	}
	
	public String quantifier()
	{return quantifier.toString();}
	
	/**
	 * @see Quantifiable#anyTimes()
	 */
	public static Quantified anyTimes(final Quantifiable base)
	{
		return new Quantified(base, LazyString.str("*"));
	}
	
	/**
	 * @see Quantifiable#anyTimesReluctantly()
	 */
	public static Quantified anyTimesReluctantly(final Quantifiable base)
	{
		return new Quantified(base, LazyString.str("*?"));
	}
	
	/**
	 * @see Quantifiable#anyTimesPossessively()
	 */
	public static Quantified anyTimesPossessively(final Quantifiable base)
	{
		return new Quantified(base, LazyString.str("*+"));
	}
	
	/**
	 * @see Quantifiable#atLeastOnce()
	 */
	public static Quantified atLeastOnce(final Quantifiable base)
	{
		return new Quantified(base, LazyString.str("+"));
	}
	
	/**
	 * @see Quantifiable#atLeastOnceReluctantly()
	 */
	public static Quantified atLeastOnceReluctantly(final Quantifiable base)
	{
		return new Quantified(base, LazyString.str("+?"));
	}
	
	/**
	 * @see Quantifiable#atLeastOncePossessively()
	 */
	public static Quantified atLeastOncePossessively(final Quantifiable base)
	{
		return new Quantified(base, LazyString.str("++"));
	}
	
	/**
	 * @see Quantifiable#optional()
	 */
	public static Quantified optional(final Quantifiable base)
	{
		return new Quantified(base, LazyString.str("?"));
	}
	
	/**
	 * @see Quantifiable#optionalReluctantly()
	 */
	public static Quantified optionalReluctantly(final Quantifiable base)
	{
		return new Quantified(base, LazyString.str("??"));
	}
	
	/**
	 * @see Quantifiable#optionalPossessively()
	 */
	public static Quantified optionalPossessively(final Quantifiable base)
	{
		return new Quantified(base, LazyString.str("?+"));
	}
	
	/**
	 * @see Quantifiable#repeat(int)
	 */
	public static Quantified repeat(final Quantifiable base, final int n)
	{
		return new Quantified(base, LazyString.str("{").append(Integer.toString(n)).append("}"));
	}
	
	/**
	 * @see Quantifiable#repeatReluctantly(int)
	 */
	public static Quantified repeatReluctantly(final Quantifiable base, final int n)
	{
		return new Quantified(base, LazyString.str("{").append(Integer.toString(n)).append("}?"));
	}
	
	/**
	 * @see Quantifiable#repeatPossessively(int)
	 */
	public static Quantified repeatPossessively(final Quantifiable base, final int n)
	{
		return new Quantified(base, LazyString.str("{").append(Integer.toString(n)).append("}+"));
	}
	
	/**
	 * @see Quantifiable#repeat(int, int)
	 */
	public static Quantified repeat(final Quantifiable base, final int min, final int max)
	{
		return new Quantified(base, LazyString.str("{").append(Integer.toString(min)).append(",").append(Integer.toString(max)).append("}")); 
	}
	
	/**
	 * @see Quantifiable#repeatReluctantly(int, int)
	 */
	public static Quantified repeatReluctantly(final Quantifiable base, final int min, final int max)
	{
		return new Quantified(base, LazyString.str("{").append(Integer.toString(min)).append(",").append(Integer.toString(max)).append("}?"));
	}
	
	/**
	 * @see Quantifiable#repeatPossessively(int, int)
	 */
	public static Quantified repeatPossessively(final Quantifiable base, final int min, final int max)
	{
		return new Quantified(base, LazyString.str("{").append(Integer.toString(min)).append(",").append(Integer.toString(max)).append("}+"));
	}
	
	/**
	 * @see Quantifiable#atLeast(int)
	 */
	public static Quantified atLeast(final Quantifiable base, final int n)
	{
		return new Quantified(base, LazyString.str("{").append(Integer.toString(n)).append(",}"));
	}
	
	/**
	 * @see Quantifiable#atLeastReluctantly(int)
	 */
	public static Quantified atLeastReluctantly(final Quantifiable base, final int n)
	{
		return new Quantified(base, LazyString.str("{").append(Integer.toString(n)).append(",}?"));
	}
	
	/**
	 * @see Quantifiable#atLeastPossessively(int)
	 */
	public static Quantified atLeastPossessively(final Quantifiable base, final int n)
	{
		return new Quantified(base, LazyString.str("{").append(Integer.toString(n)).append(",}+"));
	}

	@Override
	public LazyString expression()
	{
		return base.expression().append(quantifier);
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
