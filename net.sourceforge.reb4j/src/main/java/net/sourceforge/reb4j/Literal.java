package net.sourceforge.reb4j;

import fj.Ord;
import fj.data.List;
import fj.data.Set;


public abstract class Literal extends AbstractSequenceableAlternative
{
	private static final long serialVersionUID = 1L;

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
	
	public static StringLiteral string(final String unescaped)
	{return new StringLiteral(unescaped);}
	
	public static CharLiteral character(final char unescapedChar)
	{return new CharLiteral(unescapedChar);}
	
	Literal()
	{}
	
	public abstract String unescaped();
	
	public final String escaped()
	{
		return escape(unescaped());
	}
	
	@Override
	public final String expression()
	{
		return escaped();
	}
}
