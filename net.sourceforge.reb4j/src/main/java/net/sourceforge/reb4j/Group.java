package net.sourceforge.reb4j;

/**
 * Expression that has been grouped in parentheses.
 */
public final class Group extends AbstractQuantifiableSequenceableAlternative 
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

}
