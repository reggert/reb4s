package net.sourceforge.reb4j.scala.experimental

@SerialVersionUID(1L)
trait Quantifiable extends Expression
{
	def * = new Quantified(this, "*")
	def *? = new Quantified(this, "*?")
	def *+ = new Quantified(this, "*+")
	def anyTimes = *
	def anyTimesReluctantly = *?
	def anyTimesPossessively = *+
	
	def + = new Quantified(this, "+")
	def +? = new Quantified(this, "+?")
	def ++ = new Quantified(this, "++")
	def atLeastOnce = this +
	def atLeastOnceReluctantly = +?
	def atLeastOncePossessively = ++
	
	def ? = new Quantified(this, "?")
	def ?? = new Quantified(this, "??")
	def ?+ = new Quantified(this, "?+")
	def optional = ?
	def optionalReluctantly = ??
	def optionalPossessively = ?+
	
	def ## (n : Int) = new Quantified(this, "{" + n + "}")
	def ##? (n : Int) = new Quantified(this, "{" + n + "}?")
	def ##+ (n : Int) = new Quantified(this, "{" + n + "}+")
	def repeat (n : Int) = ## (n)
	
}