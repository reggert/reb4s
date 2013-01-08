package net.sourceforge.reb4j;

public abstract class AbstractQuantifiableSequenceableAlternative 
	extends AbstractSequenceableAlternative implements Quantifiable
{
	private static final long serialVersionUID = 1L;

	@Override
	public final Quantified anyTimes()
	{return Quantified.anyTimes(this);}

	@Override
	public final Quantified anyTimesReluctantly()
	{return Quantified.anyTimesReluctantly(this);}

	@Override
	public final Quantified anyTimesPossessively()
	{return Quantified.anyTimesPossessively(this);}

	@Override
	public final Quantified atLeastOnce()
	{return Quantified.atLeastOnce(this);}

	@Override
	public final Quantified atLeastOnceReluctantly()
	{return Quantified.atLeastOnceReluctantly(this);}

	@Override
	public final Quantified atLeastOncePossessively()
	{return Quantified.atLeastOncePossessively(this);}

	@Override
	public final Quantified optional()
	{return Quantified.optional(this);}

	@Override
	public final Quantified optionalReluctantly()
	{return Quantified.optionalReluctantly(this);}

	@Override
	public final Quantified optionalPossessively()
	{return Quantified.optionalPossessively(this);}

	@Override
	public final Quantified repeat(final int n)
	{return Quantified.repeat(this, n);}

	@Override
	public final Quantified repeatReluctantly(final int n)
	{return Quantified.repeatReluctantly(this, n);}

	@Override
	public final Quantified repeatPossessively(final int n)
	{return Quantified.repeatPossessively(this, n);}

	@Override
	public final Quantified repeat(final int min, final int max)
	{return Quantified.repeat(this, min, max);}

	@Override
	public final Quantified repeatReluctantly(final int min, final int max)
	{return Quantified.repeatReluctantly(this, min, max);}

	@Override
	public final Quantified repeatPossessively(final int min, final int max)
	{return Quantified.repeatPossessively(this, min, max);}

	@Override
	public final Quantified atLeast(final int n)
	{return Quantified.atLeast(this, n);}

	@Override
	public final Quantified atLeastReluctantly(final int n)
	{return Quantified.atLeastReluctantly(this, n);}

	@Override
	public final Quantified atLeastPossessively(final int n)
	{return Quantified.atLeastPossessively(this, n);}

}
