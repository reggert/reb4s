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
	def || (right : Union) : Union = new Union(right.subsets + right)
	def union (right : Union) : Union = this || right
	
	def || (right : CharClass) : CharClass = right match
	{
		case rhs : Union => this || rhs
		case _ => new Union(Set(this, right))
	}
	def union (right : CharClass) : CharClass = this || right
	
	protected[charclass] def unitableForm() : String
	protected[charclass] def independentForm() : String
	final override def toString() = independentForm()
}




