package net.sourceforge.reb4j.scala

@SerialVersionUID(1L)
final class Group private[scala] (val nested : Expression, private val opening : String) 
	extends Expression 
	with Alternation.Alternative
	with Sequence.Sequenceable
	with Quantifiable
{
	lazy val expression = opening + nested + ")"
	override def toString = expression
	private[scala] def this(nested : Expression, opening : Seq[Char]) =
		this(nested, opening.toString())
}


object Capture
{
	def apply(nested : Expression) = new Group(nested, "(")
}


object Group
{
	def apply(nested : Expression) = new Group(nested, "(?:")
}

object Independent
{
	def apply(nested : Expression) = new Group(nested, "(?>")
}

object PositiveLookAhead
{
	def apply(nested : Expression) = new Group(nested, "(?=")
}

object NegativeLookAhead
{
	def apply(nested : Expression) = new Group(nested, "(?!")
}

object PositiveLookBehind
{
	def apply(nested : Expression) = new Group(nested, "(?<=")
}

object NegativeLookBehind
{
	def apply(nested : Expression) = new Group(nested, "(?<!")
}
