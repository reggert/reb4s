package net.sourceforge.reb4j.charclass;

public interface Intersectable extends CharacterClass
{
	/**
	 * Returns the intersection of this character class with the 
	 * specified character class.
	 */
	Intersection intersect(Intersectable right);
	
	/**
	 * Returns the intersection of this character class with the 
	 * specified character classes.
	 */
	Intersection intersect(Intersection right);
}