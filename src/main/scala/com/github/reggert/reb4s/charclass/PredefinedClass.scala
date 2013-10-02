package com.github.reggert.reb4s.charclass

/**
 * Class representing a predefined character class.
 */
class PredefinedClass private[charclass] (val nameChar : Char) 
	extends CharClass
	with SelfContained
{
	protected final def invertedNameChar = 
		if (nameChar.isUpper) 
			nameChar.toLower
		else
			nameChar.toUpper
	override def negated = new PredefinedClass(invertedNameChar)
	override def unitableForm = "\\" + nameChar
	override def equals(other : Any) = other match
	{
		case that : NamedPredefinedClass => false
		case that : PredefinedClass => this.nameChar == that.nameChar
		case _ => false
	}
	override lazy val hashCode = 31 * nameChar.hashCode
}


/**
 * Class representing a predefined character class referenced by name.
 */
final class NamedPredefinedClass private[charclass] (
		nameChar : Char, 
		val className : String
	) extends PredefinedClass(nameChar)
{
	def this (className : String) = this('p', className)
	override def negated = new NamedPredefinedClass(invertedNameChar, className)
	override lazy val unitableForm = super.unitableForm + "{" + className + "}"
	override def equals(other : Any) = other match
	{
		case that : NamedPredefinedClass =>
			this.nameChar == that.nameChar &&
			this.className == that.className
		case _ => false
	}
	override lazy val hashCode = 
		31 * nameChar.hashCode + className.hashCode
}


