package net.sourceforge.reb4j.charclass;

import net.sourceforge.reb4j.Literal;
import fj.F2;
import fj.data.LazyString;
import fj.data.Set;

public final class MultiChar extends AbstractUnitableIntersectable
{
	private static final long serialVersionUID = 1L;
	public final Set<Character> characters;
	
	MultiChar(final Set<Character> characters)
	{
		if (characters == null) throw new NullPointerException("characters");
		this.characters = characters;
	}

	@Override
	public Negated<MultiChar> negated()
	{
		return new Negated<MultiChar>(this);
	}

	@Override
	public LazyString unitableForm()
	{
		return characters.toStream().foldLeft(
				new F2<LazyString, Character, LazyString>()
				{
					@Override
					public LazyString f(final LazyString a, final Character b)
					{return a.append(Literal.escapeChar(b));}
				},
				LazyString.empty
			);
	}

	@Override
	public LazyString independentForm()
	{
		return LazyString.str("[").append(unitableForm()).append("]");
	}
	
	public MultiChar union(final MultiChar right)
	{
		return new MultiChar(characters.union(right.characters));
	}
	
	public MultiChar union(final SingleChar right)
	{
		return new MultiChar(characters.insert(right.character));
	}

}
