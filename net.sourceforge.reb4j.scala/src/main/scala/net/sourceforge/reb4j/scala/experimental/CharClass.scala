package net.sourceforge.reb4j.scala.experimental

trait CharClass extends Expression with Alternative with Quantifiable
{
	def ^ : CharClass
	def negate = ^
	def & (right : CharClass) = right match
	{
		case Union(rhs, negated) if (!negated) => new Union(this::rhs, false)
		case _ => new Union(List(this, right), false)
	}
	def & (right : Union) = new Union(List(this, right), false)
}



case class Union private[experimental] (
		val subclasses : List[CharClass], 
		val negated : Boolean
	) extends CharClass
{
	override def toString = 
		(subclasses addString new StringBuilder("[") append "]").toString()
	override def ^ = new Union(subclasses, !negated)
}


class PredefinedClass private[experimental] (val nameChar : Char) extends CharClass
{
	protected def invertedNameChar = 
		if (nameChar.isUpperCase) 
			nameChar.toLowerCase
		else
			nameChar.toUpperCase
	override def ^ = new PredefinedClass(invertedNameChar)
	override def toString = "\\" + nameChar
}

class ParameterizedPredefinedClass private[experimental] (
		nameChar : Char, 
		val className : String
	) extends PredefinedClass(nameChar)
{
	override def ^ = new ParameterizedPredefinedClass(invertedNameChar, className)
	override def toString = super.toString() + "{" + className + "}"
}

// TODO: Unicode
// TODO: Constants


