package io.github.reggert.reb4s

/**
 * Expression that has been grouped in parentheses.
 */
@SerialVersionUID(1L)
sealed abstract class Group private[reb4s] (private val opening : String) 
	extends Expression 
	with Alternative
	with Sequenceable
	with Quantifiable
{
	val nested : Expression
	override lazy val expression = opening + nested + ")"
	override final def boundedLength = nested.boundedLength
	override final def repetitionInvalidatesBounds : Boolean = nested.repetitionInvalidatesBounds
	override final def possiblyZeroLength : Boolean = nested.possiblyZeroLength
}

/**
 * Factory object for groups.  Import the members of this object to
 * enable automatic grouping.
 */
object Group
{
	/**
	 * Constructs a capturing group.
	 */
	final case class Capture(nested : Expression) extends Group("(")
	
	/**
	 * Constructs a non-capturing group.
	 */
	final case class NonCapturing(nested : Expression) extends Group("(?:")
	
	/**
	 * Constructs an independent group.
	 */
	final case class Independent(nested : Expression) extends Group("(?>")
	
	/**
	 * Constructs a group that uses positive look-ahead.
	 */
	final case class PositiveLookAhead(nested : Expression) extends Group("(?=")
	
	/**
	 * Constructs a group that uses negative look-ahead.
	 */
	final case class NegativeLookAhead(nested : Expression) extends Group("(?!")
	
	/**
	 * Constructs a group that uses positive look-behind.
	 * 
	 * @throws UnboundedLookBehindException if the argument is unbounded.
	 */
	@throws(classOf[UnboundedLookBehindException])
	final case class PositiveLookBehind(nested : Expression) extends Group("(?<=")
	{
		if (nested.boundedLength.isEmpty) throw new UnboundedLookBehindException(nested)
	}
	
	/**
	 * Constructs a group that uses negative look-behind.
	 * 
	 * @throws UnboundedLookBehindException if the argument is unbounded.
	 */
	@throws(classOf[UnboundedLookBehindException])
	final case class NegativeLookBehind(nested : Expression) extends Group("(?<!")
	{
		if (nested.boundedLength.isEmpty) throw new UnboundedLookBehindException(nested)
	}
	
	/**
	 * Constructs a group that enables the specified matcher flags.
	 */
	final case class EnableFlags(nested : Expression, flags : Flag*) 
		extends Group(flags.mkString("(?", "", ":"))
	
	/**
	 * Constructs a group that disables the specified matcher flags.
	 */
	final case class DisableFlags(nested : Expression, flags : Flag*) 
		extends Group(flags.mkString("(?-", "", ":"))
}



