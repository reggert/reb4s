package io.github.reggert.reb4s.test

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Properties
import io.github.reggert.reb4s.{Expression,Group}
import Group.{PositiveLookBehind,NegativeLookBehind}
import org.scalacheck.{Gen,Prop}
import Prop.forAll
import scala.util.Try
import io.github.reggert.reb4s.UnboundedLookBehindException
import io.github.reggert.reb4s.Quantified



sealed abstract class LookBehindProps[GroupType <: Group](name : String)(constructor : Expression => GroupType) 
	extends Properties(name)
	with ExpressionGenerators with ExpressionShrinkers
{
	def genIndefinite(size : Int) : Gen[Quantified] = {
		require(size > 0, s"size=$size <= 0")
		for {
			mode <- Gen.oneOf(Quantified.Greedy, Quantified.Reluctant, Quantified.Possessive)
			quantifiableGen = genQuantifiable(size)
			quantified <- Gen.oneOf(
					quantifiableGen map (_.anyTimes(mode)),
					quantifiableGen map (_.atLeastOnce(mode)),
					(for {
						n <- arbitrary[Int] if n > 0
						quantifiable <- quantifiableGen	
					} yield quantifiable.atLeast(n, mode))
				)
		} yield quantified
	}
	
	property("UnboundedLookBehindException") = forAll(Gen.sized(n => genIndefinite(n + 1))){nested =>
		Prop.throws(classOf[UnboundedLookBehindException])(constructor(nested))
	}
}

object UnboundedPositiveLookBehindProps extends LookBehindProps("UnboundedPositiveLookBehind")(PositiveLookBehind)

object UnboundedNegativeLookBehindProps extends LookBehindProps("UnboundedNegativeLookBehind")(NegativeLookBehind) 



