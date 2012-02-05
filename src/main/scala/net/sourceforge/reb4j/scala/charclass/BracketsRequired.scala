package net.sourceforge.reb4j.scala.charclass

trait BracketsRequired extends CharClass
{
	final override lazy val independentForm = "[" + unitableForm + "]"
}