package net.sourceforge.reb4j.scala.charclass

final class Intersection private[charclass] (val supersets : List[Intersection.Superset]) 
	extends CharClass 
	with BracketsRequired 
	with WrappedNegation 
	with Intersection.Ops
	with Union.Subset
{
	type Superset = Intersection.Superset
	override def unitableForm() = 
		(supersets map ((superset : Superset) => superset.independentForm)).addString(new StringBuilder, "&&").toString
	
	override def && (right : Intersection) = 
		new Intersection(supersets ++ right.supersets)
	override def && (right : Superset) =
		new Intersection(supersets :+ right)
	override def equals (other : Any) = other match
	{
		case that : Intersection =>
			this.supersets == that.supersets
		case _ => false
	}
	override lazy val hashCode = 31 * supersets.hashCode
}


object Intersection
{
	trait Ops
	{
		def && (right : Superset) : Intersection
		def && (right : Intersection) : Intersection
		final def intersect (right : Superset) = this && right
		final def intersect (right : Intersection) = this && right
	}
	
	trait Superset extends CharClass with Ops
	{
		final override def && (right : Superset) = 
			new Intersection(List(this, right))
		final override def && (right : Intersection) = 
			new Intersection(this::right.supersets)
	}
}