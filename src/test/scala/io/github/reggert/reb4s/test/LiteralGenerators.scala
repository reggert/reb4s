package io.github.reggert.reb4s.test

import org.scalacheck.{Arbitrary, Gen}
import Arbitrary.arbitrary
import io.github.reggert.reb4s.Literal
import Literal.{CharLiteral, StringLiteral}

trait LiteralGenerators {
	implicit val arbCharLiteral = Arbitrary(genCharLiteral)
	implicit val arbStringLiteral = Arbitrary(genStringLiteral)
	implicit val arbLiteral = Arbitrary(genLiteral)
	
	
	def genCharLiteral : Gen[CharLiteral] = for {
		c <- arbitrary[Char]
	} yield CharLiteral(c)
	
	def genStringLiteral : Gen[StringLiteral] = for {
		s <- arbitrary[String]
	} yield StringLiteral(s)
	
	def genLiteral : Gen[Literal] = Gen.oneOf(arbitrary[CharLiteral], arbitrary[StringLiteral])
}