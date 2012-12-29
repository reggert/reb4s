package net.sourceforge.reb4j.charclass;


abstract class AbstractUnitableIntersectable extends CharClass 
	implements Unitable, Intersectable
{
	private static final long serialVersionUID = 1L;

	@Override
	public Union union(final Union right)
	{return Union.union(this, right);}

	@Override
	public Union union(final Unitable right)
	{return Union.union(this, right);}

	@Override
	public Intersection intersect(final Intersectable right)
	{return Intersection.intersect(this, right);}

	@Override
	public Intersection intersect(final Intersection right)
	{return Intersection.intersect(this, right);}
}
