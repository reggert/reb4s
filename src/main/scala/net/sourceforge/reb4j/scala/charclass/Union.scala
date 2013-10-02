package net.sourceforge.reb4j.scala.charclass

import scala.language.postfixOps

final class Union private[charclass] (val subsets : List[CharClass]) 
	extends CharClass 
	with BracketsRequired 
	with WrappedNegation[Union]
{
	override lazy val unitableForm = subsets map (_.unitableForm) mkString 
		
	override def || (right : Union) = new Union(subsets ++ right.subsets)
	override def || (right : CharClass) = new Union(subsets :+ right)
	override def equals (other : Any) = other match
	{
		case that : Union => this.subsets == that.subsets
		case _ => false
	}
	override lazy val hashCode = 31 + subsets.hashCode
}