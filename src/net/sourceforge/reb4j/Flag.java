package net.sourceforge.reb4j;

/**
 * This enumeration represents flags that may be embedded into regular 
 * expressions.
 * 
 * @author Richard W. Eggert II
 * @see {@link Regex#enable(Flag...)}
 * @see {@link Regex#disable(Flag...)}
 * @see {@link Regex#enableDirective(Flag...)}
 * @see {@link Regex#disableDirective(Flag...)}
 *
 */
public enum Flag
{
	CASE_INSENSITIVE, // 'i'
	UNIX_LINES, // 'd'
	MULTILINE, // 'm'
	DOTALL, // 's'
	UNICODE_CASE, // 'u' 
	COMMENTS // 'x'
}
