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
	
	/**
	 * Returns the union of this character class with the specified
	 * character classes.
	 */
	Union union(Union right);
	
	/**
	 * Returns the union of this character class with the specified
	 * character class.
	 */
	Union union(CharacterClass right);
	
	/**
	 * Returns the intersection of this character class with the 
	 * specified character class.
	 */
	Intersection intersect(CharacterClass right);
	
	/**
	 * Returns the intersection of this character class with the 
	 * specified character classes.
	 */
	Intersection intersect(Intersection right);
}
