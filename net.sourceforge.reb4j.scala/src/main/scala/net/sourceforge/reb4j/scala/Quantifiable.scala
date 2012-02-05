package net.sourceforge.reb4j.scala

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
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.*]]
	 */
	final def anyTimes = new Quantified(this, "*")
	
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using reluctant matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.*?]]
	 */
	final def anyTimesReluctantly = new Quantified(this, "*?")
	
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using possessive matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.*+]]
	 */
	final def anyTimesPossessively = new Quantified(this, "*+")
	
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using greedy matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.anyTimes]]
	 */
	final def * = anyTimes
	
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using reluctant matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.anyTimesReluctantly]]
	 */
	final def *? = anyTimesReluctantly
	
	/**
	 * Returns an expression that matches the receiver repeated any number 
	 * of times, using possessive matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.anyTimesPossessively]]
	 */
	final def *+ = anyTimesPossessively
	
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using greedy matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.+]]
	 */
	final def atLeastOnce = new Quantified(this, "+")
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using reluctant matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.+?]]
	 */
	final def atLeastOnceReluctantly = new Quantified(this, "+?")
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using possessive matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.++]]
	 */
	final def atLeastOncePossessively = new Quantified(this, "++")
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using greedy matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.atLeastOnce]]
	 */
	final def + = atLeastOnce
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using reluctant matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.atLeastOnceReluctantly]]
	 */
	final def +? = atLeastOnceReluctantly
	
	/**
	 * Returns an expression that matches the receiver repeated at least 
	 * once, using possessive matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.atLeastOncePossessively]]
	 */
	final def ++ = atLeastOncePossessively
	
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using greedy matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.?]]
	 */
	final def optional = new Quantified(this, "?")
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using reluctant matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.??]]
	 */
	final def optionalReluctantly = new Quantified(this, "??")
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using possessive matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.?+]]
	 */
	final def optionalPossessively = new Quantified(this, "?+")
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using greedy matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.optional]]
	 */
	final def ? = optional
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using reluctant matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.optionalReluctantly]]
	 */
	final def ?? = optionalReluctantly
	
	/**
	 * Returns an expression that matches the receiver appearing once or not 
	 * at all, using possessive matching.
	 * 
	 * @see [[net.sourceforge.reb4j.scala.Quantifiable.optionalPossessively]]
	 */
	final def ?+ = optionalPossessively
	
	
	/**
	 * Returns an expression that matches the receiver repeated the specified 
	 * number of times, using greedy matching.
	 */
	final def repeat (n : Int) = new Quantified(this, "{" + n + "}")
	
	/**
	 * Returns an expression that matches the receiver repeated the specified 
	 * number of times, using reluctant matching.
	 */
	final def repeatReluctantly (n : Int) = new Quantified(this, "{" + n + "}?")
	
	/**
	 * Returns an expression that matches the receiver repeated the specified 
	 * number of times, using possessive matching.
	 */
	final def repeatPossessively (n : Int) = new Quantified(this, "{" + n + "}+")
	
	
	/**
	 * Returns an expression that matches the receiver repeated a number of 
	 * times within the specified range, using greedy matching.
	 * 
	 * @param min the minimum number of times that may be repeated.
	 * @param max the maximum number of times that may be repeated.
	 */
	final def repeat (min : Int, max : Int) = new Quantified(this, "{" + min + "," + max + "}")
	
	/**
	 * Returns an expression that matches the receiver repeated a number of 
	 * times within the specified range, using reluctant matching.
	 * 
	 * @param min the minimum number of times that may be repeated.
	 * @param max the maximum number of times that may be repeated.
	 */
	final def repeatReluctantly (min : Int, max : Int) = new Quantified(this, "{" + min + "," + max + "}?")
	
	/**
	 * Returns an expression that matches the receiver repeated a number of 
	 * times within the specified range, using possessive matching.
	 * 
	 * @param min the minimum number of times that may be repeated.
	 * @param max the maximum number of times that may be repeated.
	 */
	final def repeatPossessively (min : Int, max : Int) = new Quantified(this, "{" + min + "," + max + "}+")
	
	
	/**
	 * Returns an expression that matches the receiver repeated at least the 
	 * specified minimum number of times, using greedy matching.
	 */
	final def atLeast (n : Int) = new Quantified(this, "{" + n + ",}")
	
	/**
	 * Returns an expression that matches the receiver repeated at least the 
	 * specified minimum number of times, using reluctant matching.
	 */
	final def atLeastReluctantly (n : Int) = new Quantified(this, "{" + n + ",}?")
	
	/**
	 * Returns an expression that matches the receiver repeated at least the 
	 * specified minimum number of times, using possessive matching.
	 */
	final def atLeastPossessively (n : Int) = new Quantified(this, "{" + n + ",}+")
}