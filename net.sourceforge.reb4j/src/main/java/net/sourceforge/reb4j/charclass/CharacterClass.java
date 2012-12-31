package net.sourceforge.reb4j.charclass;

import fj.data.LazyString;
import net.sourceforge.reb4j.Alternation.Alternative;
import net.sourceforge.reb4j.Expression;
import net.sourceforge.reb4j.Quantifiable;
import net.sourceforge.reb4j.Sequence.Sequenceable;

public interface CharacterClass 
	extends Expression, Quantifiable, Sequenceable, Alternative
{
	/**
	 * Returns an expressing matching a single character that is not within
	 * the class of characters matched by this expression.
	 */
	CharacterClass negated();
	
	/**
	 * The regular expression string that can be used within square brackets
	 * to merge with other character classes.
	 */
	LazyString unitableForm();
	
	/**
	 * The regular expression string that can be used independently of square 
	 * brackets.
	 */
	LazyString independentForm();
	
	Union union(Union right);
	Union union(CharacterClass right);
}
