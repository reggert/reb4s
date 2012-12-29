package net.sourceforge.reb4j;

public interface Quantifiable extends Expression
{
	Quantified anyTimes();
	
	Quantified anyTimesReluctantly();
	
	Quantified anyTimesPossessively();
	
	Quantified atLeastOnce();
	
	Quantified atLeastOnceReluctantly();
	
	Quantified atLeastOncePossessively();
	
	Quantified optional();
	
	Quantified optionalReluctantly();
	
	Quantified optionalPossessively();
	
	Quantified repeat(final int n);
	
	Quantified repeatReluctantly(final int n);
	
	Quantified repeatPossessively(final int n);
	
	Quantified repeat(final int min, final int max);
	
	Quantified repeatReluctantly(final int min, final int max);
	
	Quantified repeatPossessively(final int min, final int max);
	
	Quantified atLeast(final int n);
	
	Quantified atLeastReluctantly(final int n);
	
	Quantified atLeastPossessively(final int n);
}
