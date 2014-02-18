package io.github.reggert.reb4s

final case class UnboundedLookBehindException(unboundedExpression : Expression) 
	extends Exception(s"Look-behind cannot be applied to the following expression because its size is potentially unbounded: $unboundedExpression") 
