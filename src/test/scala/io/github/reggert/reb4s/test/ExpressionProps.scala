package io.github.reggert.reb4s.test

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Properties

import io.github.reggert.reb4s.Expression



object ExpressionProps extends Properties("Expression") 
	with ExpressionProperties[Expression] 
	with ExpressionGenerators with ExpressionShrinkers
{
	property("toPattern") = toPattern(arbitrary[Expression])
}


