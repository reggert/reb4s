package net.sourceforge.reb4j.charclass;

import fj.data.LazyString;
import net.sourceforge.reb4j.AbstractQuantifiableSequenceableAlternative;

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
}
