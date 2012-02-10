package net.sourceforge.reb4j.scala.charclass

/**
 * Character class representing the intersection of two or more character 
 * classes.
 */
final class Intersection private[charclass] (val supersets : List[Intersection.Superset]) 
	extends CharClass 
	with BracketsRequired 
	with WrappedNegation[Intersection]
	with Intersection.Ops
	with Union.Subset
{
	import Intersection.Superset
	override def unitableForm() = 
		(supersets map ((superset : Superset) => superset.independentForm)).mkString("&&")
	
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


/**
 * Module containing traits related to character class intersection.
 */
object Intersection
{
	/**
	 * Operations specific to character class intersection.
	 */
	trait Ops extends CharClass
	{
		/**
		 * Returns the intersection of this character class with the 
		 * specified character class.
		 */
		def && (right : Superset) : Intersection
		
		/**
		 * Returns the intersection of this character class with the 
		 * specified character classes.
		 */
		def && (right : Intersection) : Intersection
		
		/**
		 * Returns the intersection of this character class with the 
		 * specified character class.
		 */
		final def intersect (right : Superset) = this && right
		
		/**
		 * Returns the intersection of this character class with the 
		 * specified character classes.
		 */
		final def intersect (right : Intersection) = this && right
	}
	
	/**
	 * Trait indicating that a character class may be used in intersection 
	 * operations.
	 */
	trait Superset extends CharClass with Ops
	{
		final override def && (right : Superset) = 
			new Intersection(List(this, right))
		final override def && (right : Intersection) = 
			new Intersection(this::right.supersets)
	}
}