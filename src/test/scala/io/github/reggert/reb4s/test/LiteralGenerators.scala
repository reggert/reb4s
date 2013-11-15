package io.github.reggert.reb4s.test

import org.scalacheck.{Arbitrary, Gen}
import Arbitrary.arbitrary
import io.github.reggert.reb4s.Literal
import Literal.{CharLiteral, StringLiteral}

trait LiteralGenerators {
	implicit val arbCharLiteral = Arbitrary(genCharLiteral())
	implicit val arbStringLiteral = Arbitrary(genStringLiteral())
	implicit val arbLiteral = Arbitrary(genLiteral())
	
	
	def genCharLiteral(charGen : => Gen[Char] = arbitrary[Char]) : Gen[CharLiteral] = 
		for {c <- charGen} yield CharLiteral(c)
	
	def genStringLiteral(stringGen : => Gen[String] = arbitrary[String]) : Gen[StringLiteral] = 
		for {s <- stringGen} yield StringLiteral(s)
	
	def genLiteral(
			charLiteralGen : => Gen[CharLiteral] = arbitrary[CharLiteral], 
			stringLiteralGen : => Gen[StringLiteral] = arbitrary[StringLiteral]
		) : Gen[Literal] = 
		Gen.oneOf(charLiteralGen, stringLiteralGen)
}