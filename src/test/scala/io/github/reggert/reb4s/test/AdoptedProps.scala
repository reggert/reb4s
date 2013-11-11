package io.github.reggert.reb4s.test

import org.scalacheck.{Arbitrary, Properties}
import Arbitrary.arbitrary
import io.github.reggert.reb4s.Adopted

object AdoptedProps extends Properties("Adopted") 
	with ExpressionProperties[Adopted] with AdoptedGenerators 
{
	property("toPattern") = toPattern(arbitrary[Adopted])
}