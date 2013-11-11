package io.github.reggert.reb4s.test

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Properties
import io.github.reggert.reb4s.{CompoundRaw, Quantifiable, Raw}
import io.github.reggert.reb4s.EscapedLiteral

object RawProps extends Properties("Raw")
	with ExpressionProperties[Raw] with RawGenerators
{
	property("toPattern") = toPattern(arbitrary[Raw])
}


object CompoundRawProps extends Properties("CompoundRaw")
	with ExpressionProperties[CompoundRaw] with RawGenerators
{
	property("toPattern") = toPattern(arbitrary[CompoundRaw])
}


object RawQuantifiableProps extends Properties("RawQuantifiable")
	with ExpressionProperties[Raw with Quantifiable] with RawGenerators
{
	property("toPattern") = toPattern(arbitrary[Raw with Quantifiable])
}


object EscapedLiteralProps extends Properties("EscapedLiteral")
	with ExpressionProperties[EscapedLiteral] with RawGenerators
{
	property("toPattern") = toPattern(arbitrary[EscapedLiteral])
}