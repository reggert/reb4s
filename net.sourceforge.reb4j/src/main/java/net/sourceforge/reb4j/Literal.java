package net.sourceforge.reb4j;

import fj.Ord;
import fj.data.List;
import fj.data.Set;


public class Literal extends AbstractSequenceableAlternative
{
	private static final long serialVersionUID = 1L;
	public final String unescaped;
	public final String escaped;

	public static final Set<Character> NEEDS_ESCAPE = 
		Set.set(
				Ord.charOrd, 
				List.fromString("()[]{}.,-\\|+*?$^&:!<>=").array(Character[].class)
			);
	
	public static String escapeChar(final char c)
	{return NEEDS_ESCAPE.member(c) ? "\\" + c : String.valueOf(c);}
	
	public static String escape(final String unescaped)
	{
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < unescaped.length(); i++)
			builder.append(escapeChar(unescaped.charAt(i)));
		return builder.toString();
	}
	
	public Literal(final String unescaped)
	{
		if (unescaped == null) throw new NullPointerException("unescaped");
		this.unescaped = unescaped;
		this.escaped = escape(unescaped);
	}
	
	@Override
	public String expression()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
