package io.github.reggert.reb4s.test

import org.scalacheck.{Arbitrary, Properties}
import Arbitrary.arbitrary
import io.github.reggert.reb4s.Literal
import Literal.{CharLiteral, StringLiteral}

object LiteralProps extends Properties("Literal")
	with ExpressionProperties[Literal] 
	with LiteralGenerators with LiteralShrinkers
{
	property("toPattern") = toPattern(arbitrary[Literal])
}

object CharLiteralProps extends Properties("CharLiteral")
	with ExpressionProperties[CharLiteral] 
	with LiteralGenerators
{
	property("toPattern") = toPattern(arbitrary[CharLiteral])
}


object StringLiteralProps extends Properties("StringLiteral")
	with ExpressionProperties[StringLiteral] 
	with LiteralGenerators with LiteralShrinkers
{
	property("toPattern") = toPattern(arbitrary[StringLiteral])
}
