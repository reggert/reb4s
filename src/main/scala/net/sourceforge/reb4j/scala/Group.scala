package net.sourceforge.reb4j.scala

/**
 * Expression that has been grouped in parentheses.
 */
@SerialVersionUID(1L)
final class Group private[scala] (val nested : Expression, private val opening : String) 
	extends Expression 
	with Alternation.Alternative
	with Sequence.Sequenceable
	with Quantifiable
{
	override lazy val expression = opening + nested + ")"
	private[scala] def this(nested : Expression, opening : Seq[Char]) =
		this(nested, opening.toString)
	override def equals (other : Any) = other match
	{
		case that : Group => 
			this.opening == that.opening &&
			this.nested == that.nested
		case _ => false
	}
	override lazy val hashCode = 
		31 * opening.hashCode + nested.hashCode
}

/**
 * Constructs a capturing group.
 */
object Capture
{
	def apply(nested : Expression) = new Group(nested, "(")
}


/**
 * Constructs a non-capturing group.
 */
object Group
{
	def apply(nested : Expression) = new Group(nested, "(?:")
}


/**
 * Constructs an independent group.
 */
object Independent
{
	def apply(nested : Expression) = new Group(nested, "(?>")
}


/**
 * Constructs a group that uses positive look-ahead.
 */
object PositiveLookAhead
{
	def apply(nested : Expression) = new Group(nested, "(?=")
}


/**
 * Constructs a group that uses negative look-ahead.
 */
object NegativeLookAhead
{
	def apply(nested : Expression) = new Group(nested, "(?!")
}


/**
 * Constructs a group that uses positive look-behind.
 */
object PositiveLookBehind
{
	def apply(nested : Expression) = new Group(nested, "(?<=")
}


/**
 * Constructs a group that uses negative look-behind.
 */
object NegativeLookBehind
{
	def apply(nested : Expression) = new Group(nested, "(?<!")
}

