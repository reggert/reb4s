package io.github.reggert.reb4s.test

import org.scalacheck.{Arbitrary,Gen}
import Arbitrary.arbitrary
import io.github.reggert.reb4s.Quantified
import io.github.reggert.reb4s.Group
import io.github.reggert.reb4s.Expression

trait DegenerateExpressionGenerators extends ExpressionGenerators 
{
	def genIndefinite(size : Int) : Gen[Quantified] = {
		require(size > 0)
		for {
			mode <- Gen.oneOf(Quantified.Greedy, Quantified.Reluctant, Quantified.Possessive)
			quantifiableGen = genQuantifiable(size)
			quantified <- Gen.oneOf(
					quantifiableGen map (_.anyTimes(mode)),
					quantifiableGen map (_.atLeastOnce(mode)),
					(for {
						n <- arbitrary[Int] if n > 0
						quantifiable <- quantifiableGen
					} yield quantifiable.repeat(n, mode)),
					(for {
						n <- arbitrary[Int] if n > 0
						quantifiable <- quantifiableGen	
					} yield quantifiable.atLeast(n, mode))
				)
		} yield quantified
	}
	
	def genUnboundedGroup[GroupType <: Group](size : Int)(constructor : Expression => GroupType) : Gen[GroupType] =
		for {nested <- genIndefinite(size)} yield constructor(nested)
}