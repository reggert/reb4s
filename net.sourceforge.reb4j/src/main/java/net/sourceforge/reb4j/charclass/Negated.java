package net.sourceforge.reb4j.charclass;

import fj.data.LazyString;

/**
 * Character class representing the negation of another character class.
 */
public final class Negated<T extends CharClass> extends AbstractUnitableIntersectable
{
	private static final long serialVersionUID = 1L;
	public final T positive;
	
	Negated(final T positive)
	{
		if (positive == null) throw new NullPointerException("positive");
		this.positive = positive;
	}

	@Override
	public T negated()
	{
		return positive;
	}

	@Override
	public LazyString unitableForm()
	{
		return LazyString.str("[^").append(positive.unitableForm()).append("]");
	}

	@Override
	public LazyString independentForm()
	{
		return unitableForm();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + positive.hashCode();
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
		final Negated<?> other = (Negated<?>) obj;
		return positive.equals(other.positive);
	}
}
