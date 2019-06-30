package io.github.reggert.reb4s.test

import org.scalacheck.{Arbitrary, Gen}
import Arbitrary.arbitrary
import io.github.reggert.reb4s.Literal
import Literal.{CharLiteral, StringLiteral}

trait LiteralGenerators {
	implicit val arbCharLiteral: Arbitrary[CharLiteral] = Arbitrary(genCharLiteral)
	implicit val arbStringLiteral: Arbitrary[StringLiteral] =
		Arbitrary(Gen.sized {size => if (size < 2) Gen.fail else Gen.choose(2, size) flatMap genStringLiteral })
	implicit val arbLiteral: Arbitrary[Literal] =
		Arbitrary(Gen.sized {size => if (size < 1) Gen.fail else Gen.choose(1, size) flatMap genLiteral })
	
	
	def genCharLiteral : Gen[CharLiteral] = 
		for {c <- arbitrary[Char]} yield CharLiteral(c)
	
	def genStringLiteral(size : Int) : Gen[StringLiteral] = {
		require(size > 0, s"size=$size <= 0")
		for {s <- Gen.listOfN(size, arbitrary[Char])} yield StringLiteral(s.mkString)
	}
	
	def genLiteral(size : Int) : Gen[Literal] = size match {
			case 1 => genCharLiteral
			case _ => genStringLiteral(size)
		} 
}