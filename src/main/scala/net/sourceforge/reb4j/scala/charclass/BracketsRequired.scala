package net.sourceforge.reb4j.scala.charclass

trait BracketsRequired extends CharClass
{
	final override def independentForm() = "[" + unitableForm + "]"
}