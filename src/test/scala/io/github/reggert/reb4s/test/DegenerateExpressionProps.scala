package io.github.reggert.reb4s.test

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Properties
import io.github.reggert.reb4s.{Expression,Group}
import Group.{PositiveLookBehind,NegativeLookBehind}
import org.scalacheck.Gen



sealed abstract class UnboundedGroupProps[GroupType <: Group](name : String)(constructor : Expression => Group) 
	extends Properties(name)
	with ExpressionProperties[Expression] 
	with DegenerateExpressionGenerators with ExpressionShrinkers
{
	property("toPattern") = toPattern(Gen.sized(n => genUnboundedGroup(n + 1)(constructor)))
}

object UnboundedPositiveLookBehindProps extends UnboundedGroupProps("UnboundedPositiveLookBehind")(PositiveLookBehind)

object UnboundedNegativeLookBehindProps extends UnboundedGroupProps("UnboundedNegativeLookBehind")(NegativeLookBehind) 



