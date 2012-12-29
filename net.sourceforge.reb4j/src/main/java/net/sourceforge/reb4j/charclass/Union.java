package net.sourceforge.reb4j.charclass;

import fj.F2;
import fj.data.LazyString;
import fj.data.List;

public final class Union extends CharClass
	implements Unitable, Intersectable
{
	private static final long serialVersionUID = 1L;
	public final List<Unitable> subsets;
	
	Union(final List<Unitable> subsets)
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
				new F2<LazyString, Unitable, LazyString>()
				{
					@Override
					public LazyString f(final LazyString a, final Unitable b)
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
	public Union union(final Unitable right)
	{
		return new Union(subsets.append(List.single(right)));
	}
	
	@Override
	public Intersection intersect(final Intersectable right)
	{return Intersection.intersect(this, right);}

	@Override
	public Intersection intersect(final Intersection right)
	{return Intersection.intersect(this, right);}
	
	static Union union(final Unitable left, final Union right)
	{
		return new Union(right.subsets.cons(left));
	}
	
	static Union union(final Unitable left, final Unitable right)
	{
		return new Union(List.list(left, right));
	}
	
}


