package io.github.reggert.reb4s.charclass
import io.github.reggert.reb4s.Literal


/**
 * Character class that matches any of the specified characters.
 * 
 * @throws IllegalArgumentException 
 * 	if there are not at least 2 characters specified.
 */
final class MultiChar private[charclass] (val chars : Set[Char]) 
	extends CharClass
	with BracketsRequired 
	with WrappedNegation[MultiChar]
{
	require(chars.size >= 2)
	
	override def unitableForm() = 
		Literal.escape(chars.toSeq)
		
	/**
	 * Specialization of the union operator for use with instances of
	 * [[SingleChar]], returning
	 * an instance of this class rather than an instance of
	 * [[Union]].
	 */
	def || (right : SingleChar) = new MultiChar(chars + right.char)
	
	/**
	 * Specialization of the union operator for use with instances of
	 * [[MultiChar]], returning
	 * an instance of this class rather than an instance of
	 * [[Union]].
	 */
	def || (right : MultiChar) = new MultiChar(chars ++ right.chars)
	
	/**
	 * Specialization of the union operator for use with instances of
	 * [[SingleChar]], returning
	 * an instance of this class rather than an instance of
	 * [[Union]].
	 */
	def union (right : SingleChar) = this || right
	
	/**
	 * Specialization of the union operator for use with instances of
	 * [[MultiChar]], returning
	 * an instance of this class rather than an instance of
	 * [[Union]].
	 */
	def union (right : MultiChar) = this || right
	
	/**
	 * Specialization of the union operator for use with instances of
	 * [[SingleChar]], returning
	 * an instance of this class rather than an instance of
	 * [[Union]].
	 */
	def or (right : SingleChar) = this || right
	
	/**
	 * Specialization of the union operator for use with instances of
	 * [[MultiChar]], returning
	 * an instance of this class rather than an instance of
	 * [[Union]].
	 */
	def or (right : MultiChar) = this || right
	
	override def equals(other : Any) = other match
	{
		case that : MultiChar => this.chars == that.chars
		case _ => false
	}
	override lazy val hashCode = 31 * chars.hashCode
}
