package net.sourceforge.reb4j.scala.charclass

trait BracketsRequired extends CharClass
{
	def withoutBrackets : String
	override def toString() = "[" + withoutBrackets + "]"
}