package net.sourceforge.reb4j.charclass;

import static fj.Ord.charOrd;
import net.sourceforge.reb4j.Literal;
import fj.data.LazyString;
import fj.data.Set;

public final class SingleChar extends AbstractUnitableIntersectable
{
	private static final long serialVersionUID = 1L;
	public final char character;
	
	SingleChar(final char character)
	{
		this.character = character;
	}

	@Override
	public Negated<SingleChar> negated()
	{
		return new Negated<SingleChar>(this);
	}

	@Override
	public LazyString unitableForm()
	{
		return Literal.escapeChar(character);
	}

	@Override
	public LazyString independentForm()
	{
		return unitableForm();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + character;
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SingleChar other = (SingleChar) obj;
		return character == other.character;
	}
	
	public MultiChar union(final SingleChar right)
	{
		return new MultiChar(Set.set(charOrd, character, right.character));
	}
	
	public MultiChar union(final MultiChar right)
	{
		return new MultiChar(right.characters.insert(character));
	}
}
