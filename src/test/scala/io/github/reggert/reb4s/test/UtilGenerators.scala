package io.github.reggert.reb4s.test

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

trait UtilGenerators {
	implicit def arbitraryRecursiveList[T : Arbitrary] = Arbitrary(genRecursiveList[T])
	
	def genNonEmptyRecursiveList[T : Arbitrary] : Gen[List[T]] = for {
		e <- arbitrary[T]
		l <- Gen.oneOf(Gen.const(Nil), genNonEmptyRecursiveList[T])
	} yield e::l
	
	def genRecursiveList[T : Arbitrary] : Gen[List[T]] = 
		Gen.oneOf(Gen.const(Nil), genNonEmptyRecursiveList[T])
}


object UtilGenerators extends UtilGenerators