package net.sourceforge.reb4j.scala.charclass

final class Union private[charclass] (val subsets : List[Union.Subset]) 
	extends CharClass 
	with BracketsRequired 
	with WrappedNegation
	with Intersection.Superset
	with Union.Ops
{
	type Subset = Union.Subset
	override def unitableForm() = (
			subsets.foldLeft(new StringBuilder)
				((builder : StringBuilder, subset : Subset) => {builder append subset.unitableForm()})
		).toString()
		
	override def || (right : Union) = new Union(subsets ++ right.subsets)
	override def || (right : Subset) = new Union(subsets :+ right)
	override def equals (other : Any) = other match
	{
		case that : Union => this.subsets == that.subsets
		case _ => false
	}
	override def hashCode() = 31 + subsets.hashCode()
}


object Union
{
	trait Ops
	{
		def || (right : Union) : Union
		def || (right : Subset) : Union
		final def union (right : Union) : Union = this || right
		final def union (right : Subset) : CharClass = this || right
		final def or (right : Union) : Union = this || right
		final def or (right : Subset) : CharClass = this || right
	}
	
	trait Subset extends CharClass with Ops
	{
		/* 
		 * We could do an override def || (right : Alternative) here, but it's
		 * unlikely to be useful.
		 */
		final override def || (right : Union) : Union = 
			new Union(this::right.subsets)
		final override def || (right : Subset) : Union = 
			new Union(List(this, right))
	}
}