package net.sourceforge.reb4j.scala

@SerialVersionUID(1L)
final class Alternation private[scala] (val alternatives : List[Alternation.Alternative]) 
	extends Expression
	with Alternation.Ops
{
	type Alternative = Alternation.Alternative
	override lazy val expression = 
		(alternatives addString (new StringBuilder, "|")).toString
	
	override def || (right : Alternation) = new Alternation(alternatives ++ right.alternatives)
	override def || (right : Alternative) = new Alternation(alternatives :+ right)
	
	override def equals (other : Any) = other match
	{
		case that : Alternation => this.alternatives == that.alternatives
		case _ => false
	}
	
	override lazy val hashCode = 31 * alternatives.hashCode
}


object Alternation
{
	trait Ops
	{
		def || (right : Alternation) : Alternation
		def || (right : Alternative) : Alternation
		final def or (right : Alternation) = this || right
		final def or (right : Alternative) = this || right
	}
	
	trait Alternative extends Expression with Alternation.Ops
	{
		override final def || (right : Alternation) = 
				new Alternation(this +: right.alternatives)
		override final def || (right : Alternative) = 
			new Alternation(List(this, right))
	}
}