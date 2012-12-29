package net.sourceforge.reb4j;

/**
 * Expression that has been grouped in parentheses.
 */
public final class Group extends AbstractSequenceableAlternative 
	implements Quantifiable
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The expression that is enclosed in parentheses.
	 */
	public final Expression nested;
	
	private final String expression;

	private Group(final Expression nested, final String opening)
	{
		if (nested == null) throw new NullPointerException("nested");
		assert opening != null;
		this.nested = nested;
		this.expression = opening + nested.expression() + ")";
	}
	
	@Override
	public String expression()
	{
		return expression;
	}
	
	/**
	 * Constructs a capturing group.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static Group capture(final Expression nested)
	{
		return new Group(nested, "(");
	}
	
	/**
	 * Constructs a non-capturing group.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static Group nonCapturing(final Expression nested)
	{
		return new Group(nested, "(?:");
	}
	
	/**
	 * Constructs an independent group.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static Group independent(final Expression nested) 
	{
		return new Group(nested, "(?>");
	}
	
	/**
	 * Constructs a group that uses positive look-ahead.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static Group positiveLookAhead(final Expression nested) 
	{
		return new Group(nested, "(?=");
	}
	
	/**
	 * Constructs a group that uses negative look-ahead.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static Group negativeLookAhead(final Expression nested) 
	{
		return new Group(nested, "(?!");
	}
	
	/**
	 * Constructs a group that uses positive look-behind.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static Group positiveLookBehind(final Expression nested) 
	{
		return new Group(nested, "(?<=");
	}
	
	/**
	 * Constructs a group that uses negative look-behind.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static Group negativeLookBehind(final Expression nested) 
	{
		return new Group(nested, "(?<!");
	}
	
	/**
	 * Constructs a group that enables the specified matcher flags.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @param flags
	 *  the flags to enable.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static Group enableFlags(final Expression nested, final Flag... flags) 
	{
		return new Group(nested, "(?" + Flag.toString(flags) + ":");
	}
	
	/**
	 * Constructs a group that disables the specified matcher flags.
	 * 
	 * @param nested
	 * 	the expression to enclose; must not be <code>null</code>.
	 * @param flags
	 *  the flags to disable.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public static Group disableFlags(final Expression nested, final Flag... flags) 
	{
		return new Group(nested, "(?-" + Flag.toString(flags) + ":");
	}
	

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

}
