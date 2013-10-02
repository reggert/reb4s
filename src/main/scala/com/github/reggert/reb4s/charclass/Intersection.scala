package com.github.reggert.reb4s.charclass

/**
 * Character class representing the intersection of two or more character 
 * classes.
 */
final class Intersection private[charclass] (val supersets : List[CharClass]) 
	extends CharClass 
	with BracketsRequired 
	with WrappedNegation[Intersection]
{
	override def unitableForm() = 
		(supersets map ((superset : CharClass) => superset.independentForm)).mkString("&&")
	
	override def && (right : Intersection) = 
		new Intersection(supersets ++ right.supersets)
	override def && (right : CharClass) =
		new Intersection(supersets :+ right)
	override def equals (other : Any) = other match
	{
		case that : Intersection =>
			this.supersets == that.supersets
		case _ => false
	}
	override lazy val hashCode = 31 * supersets.hashCode
}
