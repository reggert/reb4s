package net.sourceforge.reb4j;

public enum Flag
{
	CASE_INSENSITIVE('i'),
	UNIX_LINES('d'),
	MULTILINE('m'),
	DOT_ALL('s'),
	UNICODE_CASE('u'), 
	COMMENTS('x');
	
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
}
