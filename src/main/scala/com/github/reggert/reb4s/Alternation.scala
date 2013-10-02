package net.sourceforge.reb4j.scala

/**
 * Expression representing a set of alternatives that may be matched.
 */
@SerialVersionUID(1L)
final class Alternation private[scala] (val alternatives : List[Alternative]) 
	extends Expression
	with Alternative
{
	override lazy val expression = alternatives.mkString("|")
	
	override def || (right : Alternation) = new Alternation(alternatives ++ right.alternatives)
	override def || (right : Alternative) = new Alternation(alternatives :+ right)
	
	override def equals (other : Any) = other match
	{
		case that : Alternation => this.alternatives == that.alternatives
		case _ => false
	}
	
	override lazy val hashCode = 31 * alternatives.hashCode
}
