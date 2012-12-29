package net.sourceforge.reb4j.charclass;

import fj.F2;
import fj.data.LazyString;
import fj.data.List;

public final class Intersection extends CharClass
	implements Unitable, Intersectable
{
	private static final long serialVersionUID = 1L;
	
	public final List<Intersectable> supersets;
	
	private Intersection(final List<Intersectable> supersets)
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
				new F2<LazyString, Intersectable, LazyString>()
				{
					@Override
					public LazyString f(final LazyString a, final Intersectable b)
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
	public Union union(final Union right)
	{return Union.union(this, right);}

	@Override
	public Union union(final Unitable right)
	{return Union.union(this, right);}
	
	@Override
	public Intersection intersect(final Intersectable right)
	{
		return new Intersection(supersets.append(List.single(right))); 
	}

	@Override
	public Intersection intersect(final Intersection right)
	{
		return new Intersection(supersets.append(right.supersets));
	}
	
	static Intersection intersect(final Intersectable left, final Intersectable right)
	{
		return new Intersection(List.list(left, right));
	}
	
	static Intersection intersect(final Intersectable left, final Intersection right)
	{
		return new Intersection(right.supersets.cons(left));
	}
}

