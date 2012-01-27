package net.sourceforge.reb4j.scala
import java.util.regex.Pattern
import scala.util.matching.Regex


trait Expression extends Serializable
{
	def toPattern : Pattern = Pattern.compile(toString)
	def toRegex(groupNames : String*) : Regex = 
		new Regex(toString, groupNames.toList : _*)
}