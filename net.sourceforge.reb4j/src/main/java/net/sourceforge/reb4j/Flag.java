package net.sourceforge.reb4j;

/**
 * Flag that can be passed to the {@link java.util.regex.Pattern} 
 * regular expression engine.
 */
public enum Flag
{
	CASE_INSENSITIVE('i'),
	UNIX_LINES('d'),
	MULTILINE('m'),
	DOT_ALL('s'),
	UNICODE_CASE('u'), 
	COMMENTS('x');
	
	/**
	 * The character that represents this flag in the expression.
	 */
	public final char c;
	
	private Flag(final char c)
	{
		this.c = c;
	}
	
	static String toString(final Flag... flags)
	{
		final StringBuilder builder = new StringBuilder();
		for (final Flag flag : flags)
			builder.append(flag.c);
		return builder.toString();
	}
	
	/**
	 * Wraps the specified expression in a group that enables this flag.
	 * 
	 * @param nested
	 * 	the expression to wrap; must not be <code>null</code>.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public Group enable(final Expression nested)
	{
		return Group.enableFlags(nested, this);
	}
	
	/**
	 * Wraps the specified expression in a group that disables this flag.
	 * 
	 * @param nested
	 * 	the expression to wrap; must not be <code>null</code>.
	 * @return a new Group.
	 * @throws NullPointerException
	 * 	if <var>nested</var> is <code>null</code>.
	 */
	public Group disable(final Expression nested)
	{
		return Group.disableFlags(nested, this);
	}
}
