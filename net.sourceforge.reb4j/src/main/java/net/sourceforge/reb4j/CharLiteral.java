package net.sourceforge.reb4j;

/**
 * Expression that matches a single specific character. 
 * 
 * @see Literal#literal(char)
 */
public final class CharLiteral extends Literal implements Quantifiable
{
	private static final long serialVersionUID = 1L;
	public final char unescapedChar;
	
	CharLiteral(final char unescapedChar)
	{this.unescapedChar = unescapedChar;}

	@Override
	public Quantified anyTimes()
	{return Quantified.anyTimes(this);}

	@Override
	public Quantified anyTimesReluctantly()
	{return Quantified.anyTimesReluctantly(this);}

	@Override
	public Quantified anyTimesPossessively()
	{return Quantified.anyTimesPossessively(this);}

	@Override
	public Quantified atLeastOnce()
	{return Quantified.atLeastOnce(this);}

	@Override
	public Quantified atLeastOnceReluctantly()
	{return Quantified.atLeastOnceReluctantly(this);}

	@Override
	public Quantified atLeastOncePossessively()
	{return Quantified.atLeastOncePossessively(this);}

	@Override
	public Quantified optional()
	{return Quantified.optional(this);}

	@Override
	public Quantified optionalReluctantly()
	{return Quantified.optionalReluctantly(this);}

	@Override
	public Quantified optionalPossessively()
	{return Quantified.optionalPossessively(this);}

	@Override
	public Quantified repeat(final int n)
	{return Quantified.repeat(this, n);}

	@Override
	public Quantified repeatReluctantly(final int n)
	{return Quantified.repeatReluctantly(this, n);}

	@Override
	public Quantified repeatPossessively(final int n)
	{return Quantified.repeatPossessively(this, n);}

	@Override
	public Quantified repeat(final int min, final int max)
	{return Quantified.repeat(this, min, max);}

	@Override
	public Quantified repeatReluctantly(final int min, final int max)
	{return Quantified.repeatReluctantly(this, min, max);}

	@Override
	public Quantified repeatPossessively(final int min, final int max)
	{return Quantified.repeatPossessively(this, min, max);}

	@Override
	public Quantified atLeast(final int n)
	{return Quantified.atLeast(this, n);}

	@Override
	public Quantified atLeastReluctantly(final int n)
	{return Quantified.atLeastReluctantly(this, n);}

	@Override
	public Quantified atLeastPossessively(final int n)
	{return Quantified.atLeastPossessively(this, n);}

	@Override
	public String unescaped()
	{return Character.toString(unescapedChar);}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + unescapedChar;
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
		final CharLiteral other = (CharLiteral) obj;
		return unescapedChar == other.unescapedChar;
	}
}
