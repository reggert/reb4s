package net.sourceforge.reb4j.scala

trait Quantifiable extends Expression
{
	def #* = new Quantified(this, "*")
	def #*? = new Quantified(this, "*?")
	def #*+ = new Quantified(this, "*+")
	def anyTimes = #*
	def anyTimesReluctantly = #*?
	def anyTimesPossessively = #*+
	
	def #+ = new Quantified(this, "+")
	def #+? = new Quantified(this, "+?")
	def #++ = new Quantified(this, "++")
	def atLeastOnce = this #+
	def atLeastOnceReluctantly = #+?
	def atLeastOncePossessively = #++
	
	def #? = new Quantified(this, "?")
	def #?? = new Quantified(this, "??")
	def #?+ = new Quantified(this, "?+")
	def optional = #?
	def optionalReluctantly = #??
	def optionalPossessively = #?+
	
	def ## (n : Int) = new Quantified(this, "{" + n + "}")
	def ##? (n : Int) = new Quantified(this, "{" + n + "}?")
	def ##+ (n : Int) = new Quantified(this, "{" + n + "}+")
	def repeat (n : Int) = ## (n)
	def repeatReluctantly (n : Int) = ##? (n)
	def repeatPossessively (n : Int) = ##+ (n)
	
	def ## (min : Int, max : Int) = new Quantified(this, "{" + min + "," + max + "}")
	def ##? (min : Int, max : Int) = new Quantified(this, "{" + min + "," + max + "}?")
	def ##+ (min : Int, max : Int) = new Quantified(this, "{" + min + "," + max + "}+")
	def repeat (min : Int, max : Int) = ## (min, max)
	def repeatReluctantly (min : Int, max : Int) = ##? (min, max)
	def repeatPossessively (min : Int, max : Int) = ##+ (min, max)
	
	def ##> (n : Int) = new Quantified(this, "{" + n + ",}")
	def ##>? (n : Int) = new Quantified(this, "{" + n + ",}?")
	def ##>+ (n : Int) = new Quantified(this, "{" + n + ",}+")
	def atLeast (n : Int) = ##> (n)
	def atLeastReluctantly (n : Int) = ##>? (n)
	def atLeastPossessively (n : Int) = ##>+ (n)
	
}