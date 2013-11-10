package io.github.reggert.reb4s.test

import scala.util.Try
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties
import io.github.reggert.reb4s.Expression
import java.util.regex.PatternSyntaxException

object ExpressionProps extends Properties("Expression") with ExpressionGenerators {
	property("toPattern") = forAll {e : Expression =>
		try {e.toPattern; true} catch {case e : PatternSyntaxException => false}
	}
}