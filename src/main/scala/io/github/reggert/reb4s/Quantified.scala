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
		
		override def boundedLength = 
			if (base.repetitionInvalidatesBounds)
				None
			else
				for {
					baseLength <- base.boundedLength
					sum = baseLength.toLong * repetitions
					if sum <= MaximumLegalBoundedLength
				} yield sum.toInt
				
		// For some reason, Pattern allows this.
		override def repetitionInvalidatesBounds : Boolean = false
		override def possiblyZeroLength : Boolean = repetitions == 0 || base.possiblyZeroLength
	}
	
	
	final case class RepeatRange(
			base : Quantifiable, 
			minRepetitions : Int, 
			maxRepetitions : Option[Int], 
			mode : Mode = Greedy
		) extends Quantified
	{
		override def quantifier = s"{$minRepetitions,${maxRepetitions.getOrElse("")}}${mode.symbol}"
		
		override def boundedLength = 
			if (base.repetitionInvalidatesBounds)
				None
			else
				for {
					baseLength <- base.boundedLength
					multiplier <- maxRepetitions
					sum = baseLength.toLong * multiplier
					if sum <= MaximumLegalBoundedLength
				} yield sum.toInt
				
		override def repetitionInvalidatesBounds : Boolean = minRepetitions == 0 || base.repetitionInvalidatesBounds
		override def possiblyZeroLength : Boolean = minRepetitions == 0 || base.possiblyZeroLength
	}
	
	
	final case class AtLeastOnce(
			base : Quantifiable,
			mode : Mode = Greedy
		) extends Quantified
	{
		override def quantifier = s"+${mode.symbol}"
		override def boundedLength = None
		override def repetitionInvalidatesBounds : Boolean = false
		override def possiblyZeroLength : Boolean = true
	}
	
	
	final case class AnyTimes(
			base : Quantifiable,
			mode : Mode = Greedy
		) extends Quantified
	{
		override def quantifier = s"*${mode.symbol}"
		override def boundedLength = None
		override def repetitionInvalidatesBounds : Boolean = true
		override def possiblyZeroLength : Boolean = true
	}
	
	
	final case class Optional(
			base : Quantifiable,
			mode : Mode = Greedy
		) extends Quantified
	{
		override def quantifier = s"?${mode.symbol}"
		override def boundedLength = base.boundedLength
		override def repetitionInvalidatesBounds : Boolean = true
		override def possiblyZeroLength : Boolean = true
	}
}