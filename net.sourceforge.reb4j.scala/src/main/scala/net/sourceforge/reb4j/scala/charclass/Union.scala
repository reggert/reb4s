package net.sourceforge.reb4j.scala.charclass

final case class Union(val subsets : Set[CharClass]) 
	extends BracketsRequired with WrappedNegation
{
	override def unitableForm() = (
			subsets.foldLeft(new StringBuilder)
				((builder : StringBuilder, subset : CharClass) => {builder append subset.unitableForm()})
		).toString()
	override def || (right : Union) = new Union(subsets ++ right.subsets)
	override def union (right : Union) = this || right
	override def || (right : CharClass) = right match
	{
		case rhs : Union => this || rhs
		case _ => super.|| (right)
	}
}