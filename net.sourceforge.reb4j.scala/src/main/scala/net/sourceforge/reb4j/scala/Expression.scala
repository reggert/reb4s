package net.sourceforge.reb4j.scala
import java.util.regex.Pattern
import scala.util.matching.Regex


trait Expression extends Serializable with NotNull
{
	final def toPattern : Pattern = Pattern.compile(toString)
	final def toRegex(groupNames : String*) : Regex = 
		new Regex(toString, groupNames.toList : _*)
}