package net.sourceforge.reb4j.charclass;

import fj.F2;
import fj.data.LazyString;
import fj.data.List;

public final class Intersection extends CharClass
{
	private static final long serialVersionUID = 1L;
	
	public final List<CharacterClass> supersets;
	
	private Intersection(final List<CharacterClass> supersets)
	{
		if (supersets == null) throw new NullPointerException("supersets");
		this.supersets = supersets;
	}

	@Override
	public Negated<Intersection> negated()
	{
		return new Negated<Intersection>(this);
	}

	@Override
	public LazyString unitableForm()
	{
		return supersets.tail().foldLeft(
				new F2<LazyString, CharacterClass, LazyString>()
				{
					@Override
					public LazyString f(final LazyString a, final CharacterClass b)
					{return a.append("&&").append(b.independentForm());}
				},
				supersets.head().independentForm()
			);
	}

	@Override
	public LazyString independentForm()
	{
		return LazyString.str("[").append(unitableForm()).append("]");
	}
	
	@Override
	public Intersection intersect(final CharacterClass right)
	{
		return new Intersection(supersets.append(List.single(right))); 
	}

	@Override
	public Intersection intersect(final Intersection right)
	{
		return new Intersection(supersets.append(right.supersets));
	}
	
	static Intersection intersect(final CharacterClass left, final CharacterClass right)
	{
		return new Intersection(List.list(left, right));
	}
	
	static Intersection intersect(final CharacterClass left, final Intersection right)
	{
		return new Intersection(right.supersets.cons(left));
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + supersets.hashCode();
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
		final Intersection other = (Intersection) obj;
		return supersets.equals(other.supersets);
	}
}

