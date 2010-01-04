package net.sourceforge.reb4j;

/**
 * This enumeration represents flags that may be embedded into regular 
 * expressions.
 * 
 * @author Richard W. Eggert II
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
