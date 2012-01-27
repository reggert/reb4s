package net.sourceforge.reb4j.scala

trait Alternative extends Expression
{
	def || (right : Alternation) = new Alternation(this +: right.alternatives)
	def || (right : Alternative) = new Alternation(List(this, right))
	def or (right : Alternation) = this || right
	def or (right : Alternative) = this || right
	
	def + (right : Alternative) : Alternative = right match
	{
		case Sequence(rhs) => new Sequence(this::rhs)
		case _ => new Sequence(List(this, right))
	}
	def + (right : Sequence) = new Sequence(this::right.components)
	def then (right : Alternative) = this + right
	def then (right : Sequence) = this + right
}