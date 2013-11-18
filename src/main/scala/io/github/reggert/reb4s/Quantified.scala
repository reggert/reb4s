package io.github.reggert.reb4s

/**
 * An expression to which a quantifier has been attached.
 * No further quantifier may be attached without first wrapping this expression
 * in a [[Group]] or other container.
 */
@SerialVersionUID(2L)
sealed abstract class Quantified 
	extends Expression 
	with Alternative
	with Sequenceable
{
	def base : Quantifiable
	def quantifier : String
	override lazy val expression = base.expression + quantifier
	override def equals (other : Any) = other match
	{
		case that : Quantified => 
			this.quantifier == that.quantifier &&
			this.base == that.base
		case _ => false
	}
	override lazy val hashCode = 
		31 * quantifier.hashCode + base.hashCode;
}


object Quantified 
{
	sealed abstract class Mode(val symbol : String)
	final case object Greedy extends Mode("")
	final case object Reluctant extends Mode("?")
	final case object Possessive extends Mode("+")
	
	final case class RepeatExactly(
			base : Quantifiable, 
			repetitions : Int, 
			mode : Mode = Greedy
		) extends Quantified
	{
		override def quantifier = s"{$repetitions}${mode.symbol}"
	}
	
	
	final case class RepeatRange(
			base : Quantifiable, 
			minRepetitions : Int, 
			maxRepetitions : Option[Int], 
			mode : Mode = Greedy
		) extends Quantified
	{
		override def quantifier = s"{$minRepetitions,${maxRepetitions.getOrElse("")}}${mode.symbol}"
	}
	
	
	final case class AtLeastOnce(
			base : Quantifiable,
			mode : Mode = Greedy
		) extends Quantified
	{
		override def quantifier = s"+${mode.symbol}"
	}
	
	
	final case class AnyTimes(
			base : Quantifiable,
			mode : Mode = Greedy
		) extends Quantified
	{
		override def quantifier = s"*${mode.symbol}"
	}
	
	
	final case class Optional(
			base : Quantifiable,
			mode : Mode = Greedy
		) extends Quantified
	{
		override def quantifier = s"?${mode.symbol}"
	}
}