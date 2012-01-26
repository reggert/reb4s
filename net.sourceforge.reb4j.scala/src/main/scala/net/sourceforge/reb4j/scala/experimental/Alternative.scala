package net.sourceforge.reb4j.scala.experimental

trait Alternative extends Expression
{
	def || (right : Alternation) = new Alternation(this +: right.alternatives)
	def || (right : Alternative) = new Alternation(List(this, right))
	def + (right : Alternative) : Alternative = right match
	{
		case Sequence(rhs) => new Sequence(this::rhs)
		case _ => new Sequence(List(this, right))
	}
	def + (right : Sequence) = new Sequence(this::right.components)
}