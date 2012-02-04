package net.sourceforge.reb4j.scala

trait Quantifiable extends Expression
{
	final def anyTimes = new Quantified(this, "*")
	final def anyTimesReluctantly = new Quantified(this, "*?")
	final def anyTimesPossessively = new Quantified(this, "*+")
	final def * = anyTimes
	final def *? = anyTimesReluctantly
	final def *+ = anyTimesPossessively
	
	final def atLeastOnce = new Quantified(this, "+")
	final def atLeastOnceReluctantly = new Quantified(this, "+?")
	final def atLeastOncePossessively = new Quantified(this, "++")
	final def + = atLeastOnce
	final def +? = atLeastOnceReluctantly
	final def ++ = atLeastOncePossessively
	
	final def optional = new Quantified(this, "?")
	final def optionalReluctantly = new Quantified(this, "??")
	final def optionalPossessively = new Quantified(this, "?+")
	final def ? = optional
	final def ?? = optionalReluctantly
	final def ?+ = optionalPossessively
	
	final def repeat (n : Int) = new Quantified(this, "{" + n + "}")
	final def repeatReluctantly (n : Int) = new Quantified(this, "{" + n + "}?")
	final def repeatPossessively (n : Int) = new Quantified(this, "{" + n + "}+")
	
	final def repeat (min : Int, max : Int) = new Quantified(this, "{" + min + "," + max + "}")
	final def repeatReluctantly (min : Int, max : Int) = new Quantified(this, "{" + min + "," + max + "}?")
	final def repeatPossessively (min : Int, max : Int) = new Quantified(this, "{" + min + "," + max + "}+")
	
	final def atLeast (n : Int) = new Quantified(this, "{" + n + ",}")
	final def atLeastReluctantly (n : Int) = new Quantified(this, "{" + n + ",}?")
	final def atLeastPossessively (n : Int) = new Quantified(this, "{" + n + ",}+")
}