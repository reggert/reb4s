package net.sourceforge.reb4j.charclass;


abstract class AbstractUnitableIntersectable extends CharClass 
	implements Intersectable
{
	private static final long serialVersionUID = 1L;

	@Override
	public Intersection intersect(final Intersectable right)
	{return Intersection.intersect(this, right);}

	@Override
	public Intersection intersect(final Intersection right)
	{return Intersection.intersect(this, right);}
}
