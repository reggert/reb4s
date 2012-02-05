package net.sourceforge.reb4j.scala
import java.util.regex.Pattern
import scala.util.matching.Regex


trait Expression extends Serializable with NotNull
{
	def expression : String
	final override def toString = expression
	final def toPattern : Pattern = Pattern.compile(expression)
	final def toRegex(groupNames : String*) : Regex = 
		new Regex(toString, groupNames.toList : _*)
}