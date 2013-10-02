package com.github.reggert.reb4s

/**
 * Interface implemented by regular expression builders that can accept 
 * quantifiers.
 * 
 * In order to quantifier an expression that does not include this trait, it
 * should first be wrapped in a [[net.sourceforge.reb4j.scala.Group]] of some sort.
 */
trait Quantifiable extends Expression
{
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using greedy matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#*]]
	 */
	final def anyTimes = new Quantified(this, "*")
	
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using reluctant matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#*?]]
	 */
	final def anyTimesReluctantly = new Quantified(this, "*?")
	
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using possessive matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#*+]]
	 */
	final def anyTimesPossessively = new Quantified(this, "*+")
	
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using greedy matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#anyTimes]]
	 */
	final def * = anyTimes
	
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using reluctant matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#anyTimesReluctantly]]
	 */
	final def *? = anyTimesReluctantly
	
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using possessive matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#anyTimesPossessively]]
	 */
	final def *+ = anyTimesPossessively
	
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using greedy matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#+]]
	 */
	final def atLeastOnce = new Quantified(this, "+")
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using reluctant matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#+?]]
	 */
	final def atLeastOnceReluctantly = new Quantified(this, "+?")
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using possessive matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#++]]
	 */
	final def atLeastOncePossessively = new Quantified(this, "++")
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using greedy matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#atLeastOnce]]
	 */
	final def + = atLeastOnce
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using reluctant matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#atLeastOnceReluctantly]]
	 */
	final def +? = atLeastOnceReluctantly
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using possessive matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#atLeastOncePossessively]]
	 */
	final def ++ = atLeastOncePossessively
	
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using greedy matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#?]]
	 */
	final def optional = new Quantified(this, "?")
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using reluctant matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#??]]
	 */
	final def optionalReluctantly = new Quantified(this, "??")
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using possessive matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#?+]]
	 */
	final def optionalPossessively = new Quantified(this, "?+")
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using greedy matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#optional]]
	 */
	final def ? = optional
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using reluctant matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#optionalReluctantly]]
	 */
	final def ?? = optionalReluctantly
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using possessive matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable#optionalPossessively]]
	 */
	final def ?+ = optionalPossessively
	
	
	/**
	 * Returns an expression that matches the receiver repeated the specified 
	 * number of times, using greedy matching.
	 * 
	 * @param n
	 * 	the exact number of times the pattern must be repeated for the
	 * 	quantified expression to match; must be &gt;= 0.
	 * @throws IllegalArgumentException if n &lt; 0.
	 */
	final def repeat (n : Int) = 
		if (n < 0) throw new IllegalArgumentException("n <= 0")
		else new Quantified(this, "{" + n + "}")
	
	/**
	 * Returns an expression that matches the receiver repeated the specified 
	 * number of times, using reluctant matching.
	 * 
	 * @param n
	 * 	the exact number of times the pattern must be repeated for the
	 * 	quantified expression to match; must be &gt;= 0.
	 * @throws IllegalArgumentException if n &lt; 0.
	 */
	final def repeatReluctantly (n : Int) = 
		if (n < 0) throw new IllegalArgumentException("n <= 0")
		else new Quantified(this, "{" + n + "}?")
	
	/**
	 * Returns an expression that matches the receiver repeated the specified 
	 * number of times, using possessive matching.
	 * 
	 * @param n
	 * 	the exact number of times the pattern must be repeated for the
	 * 	quantified expression to match; must be &gt;= 0.
	 * @throws IllegalArgumentException if n &lt; 0.
	 */
	final def repeatPossessively (n : Int) = 
		if (n < 0) throw new IllegalArgumentException("n <= 0")
		else new Quantified(this, "{" + n + "}+")
	
	
	/**
	 * Returns an expression that matches the receiver repeated a number of 
	 * times within the specified range, using greedy matching.
	 * 
	 * @param min 
	 * 	the minimum number of times that the pattern may be repeated;
	 * 	must be &gt;= 0.
	 * @param max the maximum number of times that the pattern may be repeated;
	 * 	must be &gt;= <var>min</var>.
	 * @throws IllegalArgumentException
	 * 	if <var>min</var> &lt; 0 or <var>max</var> &lt; <var>min</var>.
	 */
	final def repeat (min : Int, max : Int) =
		if (min < 0) throw new IllegalArgumentException("min < 0")
		else if (max < min) throw new IllegalArgumentException("max < min")
		else new Quantified(this, "{" + min + "," + max + "}")
	
	/**
	 * Returns an expression that matches the receiver repeated a number of 
	 * times within the specified range, using reluctant matching.
	 * 
	 * @param min 
	 * 	the minimum number of times that the pattern may be repeated;
	 * 	must be &gt;= 0.
	 * @param max the maximum number of times that the pattern may be repeated;
	 * 	must be &gt; 0.
	 * @throws IllegalArgumentException
	 * 	if <var>min</var> &lt; 0 or <var>max</var> &lt; <var>min</var>.
	 */
	final def repeatReluctantly (min : Int, max : Int) =
		if (min < 0) throw new IllegalArgumentException("min < 0")
		else if (max < min) throw new IllegalArgumentException("max < min")
		else new Quantified(this, "{" + min + "," + max + "}?")
	
	/**
	 * Returns an expression that matches the receiver repeated a number of 
	 * times within the specified range, using possessive matching.
	 * 
	 * @param min 
	 * 	the minimum number of times that the pattern may be repeated;
	 * 	must be &gt;= 0.
	 * @param max the maximum number of times that the pattern may be repeated;
	 * 	must be &gt; 0.
	 * @throws IllegalArgumentException
	 * 	if <var>min</var> &lt; 0 or <var>max</var> &lt; <var>min</var>.
	 */
	final def repeatPossessively (min : Int, max : Int) = 
		if (min < 0) throw new IllegalArgumentException("min < 0")
		else if (max < min) throw new IllegalArgumentException("max < min")
		else new Quantified(this, "{" + min + "," + max + "}+")
	
	
	/**
	 * Returns an expression that matches the receiver repeated at least the 
	 * specified minimum number of times, using greedy matching.
	 * 
	 * @param n
	 * 	the minimum number of times that the pattern may be repeated;
	 * 	must be be &gt;= 0;
	 * @throws IllegalArgumentException
	 * 	if <var>n</var> &lt; 0.
	 */
	final def atLeast (n : Int) =
		if (n < 0) throw new IllegalArgumentException("n < 0")
		else new Quantified(this, "{" + n + ",}")
	
	/**
	 * Returns an expression that matches the receiver repeated at least the 
	 * specified minimum number of times, using reluctant matching.
	 * 
	 * @param n
	 * 	the minimum number of times that the pattern may be repeated;
	 * 	must be be &gt;= 0;
	 * @throws IllegalArgumentException
	 * 	if <var>n</var> &lt; 0.
	 */
	final def atLeastReluctantly (n : Int) = 
		if (n < 0) throw new IllegalArgumentException("n < 0")
		else new Quantified(this, "{" + n + ",}?")
	
	/**
	 * Returns an expression that matches the receiver repeated at least the 
	 * specified minimum number of times, using possessive matching.
	 * 
	 * @param n
	 * 	the minimum number of times that the pattern may be repeated;
	 * 	must be be &gt;= 0;
	 * @throws IllegalArgumentException
	 * 	if <var>n</var> &lt; 0.
	 */
	final def atLeastPossessively (n : Int) = 
		if (n < 0) throw new IllegalArgumentException("n < 0")
		else new Quantified(this, "{" + n + ",}+")
}
