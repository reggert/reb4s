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
	
	/**
	 * Concatenates this expression with the argument.
	 */
	def ~~ (right : Raw) : CompoundRaw = right match {
		case compound : CompoundRaw => this ~~ compound
		case _ => CompoundRaw(List(this, right))
	}
	
	/**
	 * Concatenates this expression with the escaped form of the argument.
	 */
	def ~~ (right : Literal) : CompoundRaw = this ~~ EscapedLiteral(right)
	
	/**
	 * Concatenates this expression with the escaped form of the argument.
	 */
	def ~~ (right : CompoundRaw) = CompoundRaw(this::right.components)
	
	/**
	 * Concatenates this expression with the argument.
	 */
	def andThen (right : Raw) = this ~~ right
	
	/**
	 * Concatenates this expression with the escaped form of the argument.
	 */
	def andThen (right : Literal) : CompoundRaw = this ~~ right
	
	/**
	 * Concatenates this expression with the escaped form of the argument.
	 */
	def andThen (right : CompoundRaw) = this ~~ right
	
}


/**
 * Expression consisting of a sequence of raw expressions.
 */
final case class CompoundRaw(components : List[Raw]) extends Raw(components mkString)
{
	require(components.length >= 2, s"components.length=${components.length} < 2")
	override def ~~ (right : Raw) = right match {
		case compound : CompoundRaw => this ~~ compound
		case _ => CompoundRaw(components :+ right)
	}
	override def ~~ (right : Literal) = this ~~ EscapedLiteral(right)
	override def ~~ (right : CompoundRaw) = CompoundRaw(this.components ++ right.components)
	override def boundedLength = (Option(0) /: components) {(computedLength, component) =>
		for {
			prev <- computedLength
			next <- component.boundedLength
			sum = prev.toLong + next.toLong
			if sum <= MaximumLegalBoundedLength
		} yield sum.toInt
	}
}


/**
 * Adapter from [[Literal]] to [[Raw]].
 */
final case class EscapedLiteral(literal : Literal) extends Raw(literal.escaped)
{
	override def boundedLength = literal.boundedLength
}

sealed abstract class Entity private[reb4s] (rawExpression : => String) 
	extends Raw(rawExpression)
	with Quantifiable
{
	override final def boundedLength = Some(1)
}

/**
 * Matches any single character.
 */
case object AnyChar extends Entity(".") 

/**
 * Matches the beginning of a line.
 */
case object LineBegin extends Entity("^")

/**
 * Matches the end of a line.
 */
case object LineEnd extends Entity("$") 

/**
 * Matches a word boundary.
 */
case object WordBoundary extends Entity("\\b") 

/**
 * Matches a non-word-boundary.
 */
case object NonwordBoundary extends Entity("\\B")

/**
 * Matches the beginning of input.
 */
case object InputBegin extends Entity("\\A") 

/**
 * Matches the end of the previous match.
 */
case object MatchEnd extends Entity("\\G") 

/**
 * Matches the end of input, skipping end-of-line markers.
 */
case object InputEndSkipEOL extends Entity("\\Z") 

/**
 * Matches the end of input.
 */
case object InputEnd extends Entity("\\z") 
