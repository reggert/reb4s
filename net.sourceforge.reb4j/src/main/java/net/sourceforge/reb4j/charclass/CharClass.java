package net.sourceforge.reb4j.charclass;

import static fj.Ord.charOrd;
import net.sourceforge.reb4j.AbstractQuantifiableSequenceableAlternative;
import fj.data.LazyString;
import fj.data.Set;

/**
 * Base class representing an expression that matches a single character 
 * within a class of characters.
 */
public abstract class CharClass extends AbstractQuantifiableSequenceableAlternative
	implements CharacterClass
{
	private static final long serialVersionUID = 1L;

	@Override
	public final LazyString expression()
	{return independentForm();}
	
	public static SingleChar character(final char c)
	{return new SingleChar(c);}
	
	public static MultiChar characters(final char... cs)
	{
		Set<Character> set = Set.empty(charOrd);
		for (final char c : cs)
			set = set.insert(c);
		return new MultiChar(set);
	}
}
