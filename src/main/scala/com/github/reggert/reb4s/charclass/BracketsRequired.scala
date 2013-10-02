package net.sourceforge.reb4j.scala.charclass

/**
 * Trait indicating that a character class expression must be enclosed
 * in square brackets when used as a regular expression.
 */
private[charclass] trait BracketsRequired extends CharClass
{
	final override lazy val independentForm = "[" + unitableForm + "]"
}