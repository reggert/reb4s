package io.github.reggert.reb4s.test

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import scala.annotation.tailrec

trait UtilGenerators {
	
	def genSizes(totalSize : Int) : Gen[List[Int]] = {
		require(totalSize >= 0)
		def recurse(remainingSize : Int) : Gen[List[Int]] =
			remainingSize match {
				case 0 => Nil
				case _ => for {
					nextSize <- Gen.choose(1, remainingSize)
					more <- recurse(remainingSize - nextSize)
				} yield nextSize::more
			}
		for {
		  originalList <- recurse(totalSize)
		  randomizedList <- Gen.parameterized {p =>
		    for {r <- p.rng.shuffle(originalList)} yield r
		  }
		} yield randomizedList
	}
}


object UtilGenerators extends UtilGenerators