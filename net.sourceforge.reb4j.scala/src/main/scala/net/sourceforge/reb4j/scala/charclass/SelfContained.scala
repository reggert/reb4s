package net.sourceforge.reb4j.scala.charclass

trait SelfContained extends CharClass
{
	final override def independentForm() = unitableForm()
}