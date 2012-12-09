package net.sourceforge.reb4j.scala

/**
 * Raw regular expression.
 */
@SerialVersionUID(1L)
sealed abstract class Raw private[scala] (rawExpression : => String)
	extends Expression 
	with Alternation.Alternative 
	with Sequence.Sequenceable
{
	override lazy val expression = rawExpression
	
	/**
	 * Concatenates this expression with the argument.
	 */
	def + (right : Raw) = CompoundRaw(List(this, right))
	
	/**
	 * Concatenates this expression with the escaped form of the argument.
	 */
	def + (right : Literal) : CompoundRaw = this + EscapedLiteral(right)
	
	def + (right : CompoundRaw) = CompoundRaw(this::right.components)
	
	/**
	 * Concatenates this expression with the argument.
	 */
	final def then (right : Raw) = this + right
	
	
	/**
	 * Concatenates this expression with the argument.
	 */
	final def then (right : CompoundRaw) = this + right
	
	
	/**
	 * Concatenates this expression with the escaped form of the argument.
	 */
	final def then (right : Literal) = this + right
	
	
}


final case class CompoundRaw(components : List[Raw]) extends Raw(components mkString)
{
	override def + (right : Raw) = CompoundRaw(components :+ right)
	override def + (right : Literal) = this + EscapedLiteral(right)
	override def + (right : CompoundRaw) = CompoundRaw(this.components ++ right.components)
}


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