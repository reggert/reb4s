package net.sourceforge.reb4j;

import java.io.Serializable;
import java.util.regex.Pattern;

import fj.data.LazyString;

/**
 * Basic abstraction of a regular expression.
 * 
 * All implementations of this can be converted directly into a
 * {@link Pattern} object without risk of throwing an exception.
 * 
 * This interface is not intended to be implemented by clients,
 * and doing so will result in unpredictable behavior.
 */
public interface Expression extends Serializable 
{
	/**
	 * Returns the regular expression represented by this object, in a form 
	 * suitable to passing to the {@link java.util.regex.Pattern} class.
	 */
	LazyString expression();
	
	/**
	 * Passes the regular expression represented by this object to 
	 * {@link java.util.regex.Pattern} and returns the result.
	 */
	Pattern toPattern();
}
