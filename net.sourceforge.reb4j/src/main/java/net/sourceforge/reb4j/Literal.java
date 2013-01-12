package net.sourceforge.reb4j;

import fj.F2;
import fj.Ord;
import fj.data.LazyString;
import fj.data.List;
import fj.data.Set;


/**
 * Expression that exactly matches a specific string or character.
 */
public abstract class Literal extends AbstractSequenceableAlternative
{
	private static final long serialVersionUID = 1L;

	/**
	 * The set of characters that must be escaped in expressions.
	 */
	public static final Set<Character> NEEDS_ESCAPE = 
		Set.set(
				Ord.charOrd, 
				List.fromString("()[]{}.,-\\|+*?$^&:!<>=").array(Character[].class)
			);
	
	/**
	 * Helper function that escapes the specified character.
	 * 
	 * @param c the character to escape; must not be <code>null</code>.
	 * @return a {@link LazyString} containing the escaped (if necessary) character.
	 * @throws NullPointerException
	 * 	if <var>c</var> is <code>null</code>. 
	 */
	public static LazyString escapeChar(final Character c)
	{return (NEEDS_ESCAPE.member(c) ? LazyString.str("\\") : LazyString.empty).append(c.toString());}
	
	/**
	 * Helper function that escapes the specified string.
	 * 
	 * @param unescaped
	 * 	the string to escape; must not be <code>null</code>.
	 * @return a {@link LazyString} containing the escaped (if necessary) string.
	 * @throws NullPointerException
	 * 	if <var>unescaped</var> is <code>null</code>.
	 */
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
	
	/**
	 * Constructs a new {@link StringLiteral} from the specified string, 
	 * escaping it as necessary.
	 * 
	 * @param unescaped
	 * 	the unescaped string to represent as a {@link StringLiteral};
	 * 	must not be null.
	 * @return a new instance of {@link StringLiteral}.
	 * @throws NullPointerException
	 * 	if <var>unescaped</var> is <code>null</code>.
	 */
	public static StringLiteral literal(final String unescaped)
	{return new StringLiteral(unescaped);}
	
	/**
	 * Constructs a new {@link CharLiteral} from the specified character,
	 * escaping it as necessary.
	 * 
	 * @param unescapedChar
	 * 	the unescaped character to represent as a {@link CharLiteral}.
	 * @return a new instance fo {@link CharLiteral}.
	 */
	public static CharLiteral literal(final char unescapedChar)
	{return new CharLiteral(unescapedChar);}
	
	Literal()
	{}
	
	/**
	 * Returns the original unescaped string (or character as a string) 
	 * matched by this expression.
	 */
	public abstract String unescaped();

	/**
	 * Returns the escaped form of the string or character matched by
	 * this expression.
	 */
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
