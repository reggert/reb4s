package io.github.reggert.reb4s

import Quantified._

/**
 * Interface implemented by regular expression builders that can accept 
 * quantifiers.
 * 
 * In order to quantifier an expression that does not include this trait, it
 * should first be wrapped in a [[Group]] of some sort.
 */
trait Quantifiable extends Expression
{
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times.
	 */
	final def anyTimes(mode : Mode = Greedy) = AnyTimes(this, mode)
	
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using greedy matching.
	 * 
	 * @see [[Quantifiable#anyTimes]]
	 */
	final def * = anyTimes()
	
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using reluctant matching.
	 * 
	 * @see [[Quantifiable#anyTimes]]
	 */
	final def *? = anyTimes(Reluctant)
	
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using possessive matching.
	 * 
	 * @see [[Quantifiable#anyTimes]]
	 */
	final def *+ = anyTimes(Possessive)
	
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once.
	 * 
	 * @see [[Quantifiable#+]]
	 */
	final def atLeastOnce(mode : Mode = Greedy) = AtLeastOnce(this, mode)
	
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using greedy matching.
	 * 
	 * @see [[Quantifiable#atLeastOnce]]
	 */
	final def + = atLeastOnce()
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using reluctant matching.
	 * 
	 * @see [[Quantifiable#atLeast]]
	 */
	final def +? = atLeastOnce(Reluctant)
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using possessive matching.
	 * 
	 * @see [[Quantifiable#atLeastOnce]]
	 */
	final def ++ = atLeastOnce(Possessive)
	
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all.
	 * 
	 * @see [[Quantifiable#?]]
	 */
	final def optional(mode : Mode = Greedy) = Optional(this, mode)
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using greedy matching.
	 * 
	 * @see [[Quantifiable#optional]]
	 */
	final def ? = optional()
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using reluctant matching.
	 * 
	 * @see [[Quantifiable#optional]]
	 */
	final def ?? = optional(Reluctant)
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using possessive matching.
	 * 
	 * @see [[Quantifiable#optional]]
	 */
	final def ?+ = optional(Possessive)
	
	
	/**
	 * Returns an expression that matches the receiver repeated the specified 
	 * number of times.
	 * 
	 * @param n
	 * 	the exact number of times the pattern must be repeated for the
	 * 	quantified expression to match; must be &gt;= 0.
	 * @throws IllegalArgumentException if n &lt; 0.
	 */
	final def repeat (n : Int, mode : Mode = Greedy) = 
		if (n < 0) throw new IllegalArgumentException("n <= 0")
		else RepeatExactly(this, n, mode)
	
	/**
	 * Returns an expression that matches the receiver repeated a number of 
	 * times within the specified range.
	 * 
	 * @param min 
	 * 	the minimum number of times that the pattern may be repeated;
	 * 	must be &gt;= 0.
	 * @param max the maximum number of times that the pattern may be repeated;
	 * 	must be &gt;= <var>min</var>.
	 * @throws IllegalArgumentException
	 * 	if <var>min</var> &lt; 0 or <var>max</var> &lt; <var>min</var>.
	 */
	final def repeatRange (min : Int, max : Int, mode : Mode = Greedy) =
		if (min < 0) throw new IllegalArgumentException("min < 0")
		else if (max < min) throw new IllegalArgumentException("max < min")
		else RepeatRange(this, min, Some(max), mode)
	
	
	/**
	 * Returns an expression that matches the receiver repeated at least the 
	 * specified minimum number of times.
	 * 
	 * @param n
	 * 	the minimum number of times that the pattern may be repeated;
	 * 	must be be &gt;= 0;
	 * @throws IllegalArgumentException
	 * 	if <var>n</var> &lt; 0.
	 */
	final def atLeast (n : Int, mode : Mode = Greedy) =
		if (n < 0) throw new IllegalArgumentException("n < 0")
		else RepeatRange(this, n, None, mode)
}
