package io.github.reggert.reb4s.test

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

trait UtilGenerators {
	implicit def arbitraryRecursiveList[T : Arbitrary] = Arbitrary(genRecursiveList(arbitrary[T]))
	
	def genNonEmptyRecursiveList[T](g : => Gen[T]) : Gen[List[T]] = for {
		e <- g
		l <- Gen.oneOf(Gen.const(Nil), genNonEmptyRecursiveList(g))
	} yield e::l
	
	def genRecursiveList[T](g : => Gen[T]) : Gen[List[T]] = 
		Gen.oneOf(Gen.const(Nil), genNonEmptyRecursiveList(g))
}


object UtilGenerators extends UtilGenerators