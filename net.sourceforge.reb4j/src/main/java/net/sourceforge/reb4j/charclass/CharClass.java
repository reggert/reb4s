package net.sourceforge.reb4j.charclass;

import net.sourceforge.reb4j.AbstractQuantifiableSequenceableAlternative;

/**
 * Base class representing an expression that matches a single character 
 * within a class of characters.
 */
public abstract class CharClass extends AbstractQuantifiableSequenceableAlternative
{
	private static final long serialVersionUID = 1L;

	/**
	 * Returns an expressing matching a single character that is not within
	 * the class of characters matched by this expression.
	 */
	public abstract CharClass negated();
	
	/**
	 * The regular expression string that can be used within square brackets
	 * to merge with other character classes.
	 */
	protected abstract String unitableForm();
	
	/**
	 * The regular expression string that can be used independently of square 
	 * brackets.
	 */
	protected abstract String independentForm();

	
	@Override
	public final String expression()
	{return independentForm();}
}
