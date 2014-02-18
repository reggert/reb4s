package io.github.reggert.reb4s.test

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Properties
import io.github.reggert.reb4s.{Expression,Group}
import Group.{PositiveLookBehind,NegativeLookBehind}
import org.scalacheck.{Gen,Prop}
import Prop.forAll
import scala.util.Try
import io.github.reggert.reb4s.UnboundedLookBehindException



sealed abstract class UnboundedGroupProps[GroupType <: Group](name : String)(constructor : Expression => GroupType) 
	extends Properties(name)
	with DegenerateExpressionGenerators with ExpressionShrinkers
{
	property("UnboundedLookBehindException") = forAll(Gen.sized(n => genIndefinite(n + 1))){nested =>
		Prop.throws(classOf[UnboundedLookBehindException])(constructor(nested))
	}
}

object UnboundedPositiveLookBehindProps extends UnboundedGroupProps("UnboundedPositiveLookBehind")(PositiveLookBehind)

object UnboundedNegativeLookBehindProps extends UnboundedGroupProps("UnboundedNegativeLookBehind")(NegativeLookBehind) 



