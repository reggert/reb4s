package net.sourceforge.reb4j.charclass;

import fj.data.LazyString;

public final class CharRange extends CharClass
{
	private static final long serialVersionUID = 1L;
	public final char first, last;
	
	CharRange(final char first, final char last)
	{
		if (first >= last) throw new IllegalArgumentException("first must be < last");
		this.first = first;
		this.last = last;
	}

	@Override
	public Negated<CharRange> negated()
	{
		return new Negated<CharRange>(this);
	}

	@Override
	public LazyString unitableForm()
	{
		return LazyString.str(Character.toString(first)).append("-").append(Character.toString(last));
	}

	@Override
	public LazyString independentForm()
	{
		return LazyString.str("[").append(unitableForm()).append("]");
	}

}
