package io.github.reggert.reb4s.test

import scala.util.Try

import org.scalacheck.{Prop, Properties}
import Prop.forAll
import io.github.reggert.reb4s.Expression

object ExpressionProps extends Properties("Expression") with ExpressionGenerators {
	property("toPattern") = forAll {e : Expression =>
		Try(e.toPattern).isSuccess
	}
}