package io.github.reggert.reb4s.test

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

trait UtilGenerators {
	implicit def arbitraryRecursiveList[T : Arbitrary] = Arbitrary(genRecursiveList(0)(arbitrary[T]))
	
	def genNonEmptyRecursiveList[T](g : => Gen[T]) : Gen[List[T]] = Gen.sized {size =>
		def recurse(maxSize : Int) : Gen[List[T]] = for {
			e <- g
			l <- if (maxSize > 1) Gen.oneOf(Gen.const(Nil), recurse(maxSize-1)) else Gen.const(Nil)
		} yield e::l
		recurse(size)
	}
	
	def genRecursiveList[T](minLength : Int = 0)(g : => Gen[T]) : Gen[List[T]] = minLength match {
		case 0 => Gen.oneOf(Gen.const(Nil), genNonEmptyRecursiveList(g))
		case 1 => genNonEmptyRecursiveList(g)
		case n => require(n > 0);
			for {
				e <- g
				l <- genRecursiveList(minLength - 1)(g)
			} yield e::l
	}
}


object UtilGenerators extends UtilGenerators