package net.sourceforge.reb4j.scala.experimental

trait Atomic extends Expression
{
	def | (right : Alternation) = new Alternation(this +: right.alternatives)
	def | (right : Atomic) = new Alternation(List(this, right))
}