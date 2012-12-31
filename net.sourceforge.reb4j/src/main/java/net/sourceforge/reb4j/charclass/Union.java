package net.sourceforge.reb4j.charclass;

import fj.F2;
import fj.data.LazyString;
import fj.data.List;

public final class Union extends CharClass
	implements Intersectable
{
	private static final long serialVersionUID = 1L;
	public final List<CharacterClass> subsets;
	
	Union(final List<CharacterClass> subsets)
	{
		if (subsets == null) throw new NullPointerException("subsets");
		this.subsets = subsets;
	}

	@Override
	public Negated<Union> negated()
	{
		return new Negated<Union>(this);
	}

	@Override
	public LazyString unitableForm()
	{
		return subsets.foldLeft(
				new F2<LazyString, CharacterClass, LazyString>()
				{
					@Override
					public LazyString f(final LazyString a, final CharacterClass b)
					{return a.append(b.unitableForm());}
				},
				LazyString.empty
			);
	}

	@Override
	public LazyString independentForm()
	{
		return LazyString.str("[").append(unitableForm()).append("]");
	}

	@Override
	public Union union(final Union right)
	{
		return new Union(subsets.append(right.subsets));
	}

	@Override
	public Union union(final CharacterClass right)
	{
		return new Union(subsets.append(List.single(right)));
	}
	
	@Override
	public Intersection intersect(final Intersectable right)
	{return Intersection.intersect(this, right);}

	@Override
	public Intersection intersect(final Intersection right)
	{return Intersection.intersect(this, right);}
	
	static Union union(final CharacterClass left, final Union right)
	{
		return new Union(right.subsets.cons(left));
	}
	
	static Union union(final CharacterClass left, final CharacterClass right)
	{
		return new Union(List.list(left, right));
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + subsets.hashCode();
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
		final Union other = (Union) obj;
		return subsets.equals(other.subsets);
	}
	
}


