package net.sourceforge.reb4j.scala.charclass
import java.lang.Character.UnicodeBlock
import net.sourceforge.reb4j.scala.{Expression, Alternative, Quantifiable}

trait CharClass extends Expression with Alternative with Quantifiable
{
	def ^ : CharClass
	def negate = ^
	
	/* 
	 * We could do an override def || (right : Alternative) here, but it's
	 * unlikely to be useful.
	 */
	def || (right : CharClass) = new Union(this, right)
	def union (right : CharClass) = this || right
	
	protected def withoutBrackets = toString
}




