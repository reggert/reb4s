package net.sourceforge.reb4j;

import fj.F2;
import fj.Ord;
import fj.data.LazyString;
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
	
	public static LazyString escapeChar(final Character c)
	{return (NEEDS_ESCAPE.member(c) ? LazyString.str("\\") : LazyString.empty).append(c.toString());}
	
	public static LazyString escape(final LazyString unescaped)
	{
		return unescaped.toStream().foldLeft(
				new F2<LazyString, Character, LazyString>()
				{
					@Override
					public LazyString f(final LazyString a, final Character b)
					{return a.append(escapeChar(b));}
				},
				LazyString.empty
			);
	}
	
	public static StringLiteral literal(final String unescaped)
	{return new StringLiteral(unescaped);}
	
	public static CharLiteral literal(final char unescapedChar)
	{return new CharLiteral(unescapedChar);}
	
	Literal()
	{}
	
	public abstract String unescaped();
	
	public final LazyString escaped()
	{
		return escape(LazyString.str(unescaped()));
	}
	
	@Override
	public final LazyString expression()
	{
		return escaped();
	}
}
