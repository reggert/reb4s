package net.sourceforge.reb4j.scala.experimental
import java.util.regex.Pattern
import scala.util.matching.Regex

@SerialVersionUID(1L)
class Adopted private[experimental] (val expression : String) extends Expression 
{
	override def toString = expression
}


object Adopt
{
	def apply(pattern : Pattern) = new Adopted(pattern.pattern())
	def apply(regex : Regex) = new Adopted(regex.toString())
}

