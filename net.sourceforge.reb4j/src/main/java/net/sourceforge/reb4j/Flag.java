package net.sourceforge.reb4j;

import fj.F2;
import fj.data.Array;
import fj.data.LazyString;

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
	
	static LazyString toString(final Flag... flags)
	{
		return Array.array(flags).foldLeft(
				new F2<LazyString, Flag, LazyString>()
				{
					@Override
					public LazyString f(final LazyString a, final Flag b)
					{return a.append(Character.toString(b.c));}
				},
				LazyString.empty
			);
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
