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
 * Factory object for groups.  Import the members of this object to
 * enable automatic grouping.
 */
object Group
{
	/**
	 * Constructs a capturing group.
	 */
	def capture(nested : Expression) = new Group(nested, "(")
	
	/**
	 * Constructs a non-capturing group.
	 */
	implicit def nonCapturing(nested : Expression) = new Group(nested, "(?:")
	
	/**
	 * Constructs an independent group.
	 */
	def independent(nested : Expression) = new Group(nested, "(?>")
	
	/**
	 * Constructs a group that uses positive look-ahead.
	 */
	def positiveLookAhead(nested : Expression) = new Group(nested, "(?=")
	
	/**
	 * Constructs a group that uses negative look-ahead.
	 */
	def negativeLookAhead(nested : Expression) = new Group(nested, "(?!")
	
	/**
	 * Constructs a group that uses positive look-behind.
	 */
	def positiveLookBehind(nested : Expression) = new Group(nested, "(?<=")
	
	/**
	 * Constructs a group that uses negative look-behind.
	 */
	def negativeLookBehind(nested : Expression) = new Group(nested, "(?<!")
}



