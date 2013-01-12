package net.sourceforge.reb4j.scala.charclass
import net.sourceforge.reb4j.scala.Literal


/**
 * Character class that matches any of the specified characters.
 */
final class MultiChar private[charclass] (val chars : Set[Char]) 
	extends CharClass
	with BracketsRequired 
	with WrappedNegation[MultiChar]
	with Union.Subset
{
	override def unitableForm() = 
		Literal.escape(chars.toSeq)
		
	/**
	 * Specialization of the union operator for use with instances of
	 * [[net.sourceforge.reb4j.scala.charclass.SingleChar]], returning
	 * an instance of this class rather than an instance of
	 * [[net.sourceforge.reb4j.scala.charclass.Union]].
	 */
	def || (right : SingleChar) = new MultiChar(chars + right.char)
	
	/**
	 * Specialization of the union operator for use with instances of
	 * [[net.sourceforge.reb4j.scala.charclass.MultiChar]], returning
	 * an instance of this class rather than an instance of
	 * [[net.sourceforge.reb4j.scala.charclass.Union]].
	 */
	def || (right : MultiChar) = new MultiChar(chars ++ right.chars)
	
	/**
	 * Specialization of the union operator for use with instances of
	 * [[net.sourceforge.reb4j.scala.charclass.SingleChar]], returning
	 * an instance of this class rather than an instance of
	 * [[net.sourceforge.reb4j.scala.charclass.Union]].
	 */
	def union (right : SingleChar) = this || right
	
	/**
	 * Specialization of the union operator for use with instances of
	 * [[net.sourceforge.reb4j.scala.charclass.MultiChar]], returning
	 * an instance of this class rather than an instance of
	 * [[net.sourceforge.reb4j.scala.charclass.Union]].
	 */
	def union (right : MultiChar) = this || right
	
	/**
	 * Specialization of the union operator for use with instances of
	 * [[net.sourceforge.reb4j.scala.charclass.SingleChar]], returning
	 * an instance of this class rather than an instance of
	 * [[net.sourceforge.reb4j.scala.charclass.Union]].
	 */
	def or (right : SingleChar) = this || right
	
	/**
	 * Specialization of the union operator for use with instances of
	 * [[net.sourceforge.reb4j.scala.charclass.MultiChar]], returning
	 * an instance of this class rather than an instance of
	 * [[net.sourceforge.reb4j.scala.charclass.Union]].
	 */
	def or (right : MultiChar) = this || right
	
	override def equals(other : Any) = other match
	{
		case that : MultiChar => this.chars == that.chars
		case _ => false
	}
	override lazy val hashCode = 31 * chars.hashCode
}