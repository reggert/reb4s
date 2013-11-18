package io.github.reggert.reb4s

import scala.language.postfixOps

/**
 * Raw regular expression.
 */
@SerialVersionUID(1L)
sealed abstract class Raw private[reb4s] (rawExpression : => String)
	extends Expression 
	with Alternative 
	with Sequenceable
{
	override lazy val expression = rawExpression
	
	@deprecated(message="Not consistent with API of Sequence; use ~~ instead", since="2.1.0")
	final def + (right : Raw) = CompoundRaw(List(this, right))
	
	@deprecated(message="Not consistent with API of Sequence; use ~~ instead", since="2.1.0")
	final def + (right : Literal) : CompoundRaw = this + EscapedLiteral(right)
	
	@deprecated(message="Not consistent with API of Sequence; use ~~ instead", since="2.1.0")
	final def + (right : CompoundRaw) = CompoundRaw(this::right.components)
	
	
	/**
	 * Concatenates this expression with the argument.
	 */
	def ~~ (right : Raw) = CompoundRaw(List(this, right))
	
	/**
	 * Concatenates this expression with the escaped form of the argument.
	 */
	def ~~ (right : Literal) : CompoundRaw = this ~~ EscapedLiteral(right)
	
	/**
	 * Concatenates this expression with the escaped form of the argument.
	 */
	def ~~ (right : CompoundRaw) = CompoundRaw(this::right.components)
	
	@deprecated(message="then is now a reserved word in Scala 2.10; use + instead", since="2.1.0")
	final def `then` (right : Raw) = this + right
	
	@deprecated(message="then is now a reserved word in Scala 2.10; use + instead", since="2.1.0")
	final def `then` (right : CompoundRaw) = this + right
	
	@deprecated(message="then is now a reserved word in Scala 2.10; use + instead", since="2.1.0")
	final def `then` (right : Literal) = this + right
	
	
}


/**
 * Expression consisting of a sequence of raw expressions.
 */
final case class CompoundRaw(components : List[Raw]) extends Raw(components mkString)
{
	require(components.length >= 2)
	override def ~~ (right : Raw) = CompoundRaw(components :+ right)
	override def ~~ (right : Literal) = this ~~ EscapedLiteral(right)
	override def ~~ (right : CompoundRaw) = CompoundRaw(this.components ++ right.components)
}


/**
 * Adapter from [[Literal]] to [[Raw]].
 */
final case class EscapedLiteral(literal : Literal) extends Raw(literal.escaped)

/**
 * Matches any single character.
 */
case object AnyChar extends Raw(".") with Quantifiable

/**
 * Matches the beginning of a line.
 */
case object LineBegin extends Raw("^") with Quantifiable

/**
 * Matches the end of a line.
 */
case object LineEnd extends Raw("$") with Quantifiable

/**
 * Matches a word boundary.
 */
case object WordBoundary extends Raw("\\b") with Quantifiable

/**
 * Matches a non-word-boundary.
 */
case object NonwordBoundary extends Raw("\\B") with Quantifiable

/**
 * Matches the beginning of input.
 */
case object InputBegin extends Raw("\\A") with Quantifiable

/**
 * Matches the end of the previous match.
 */
case object MatchEnd extends Raw("\\G") with Quantifiable

/**
 * Matches the end of input, skipping end-of-line markers.
 */
case object InputEndSkipEOL extends Raw("\\Z") with Quantifiable

/**
 * Matches the end of input.
 */
case object InputEnd extends Raw("\\z") with Quantifiable
