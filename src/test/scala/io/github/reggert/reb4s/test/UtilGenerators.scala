package io.github.reggert.reb4s.test

import org.scalacheck.Gen


trait UtilGenerators {

	/**
		* Generates a list of sizes that sum to the specified total size.
		*
		* This is useful for generating collections of collections that do not exceed the total generation size.
		*
		* @param totalSize total size target; must not be negative.
		* @return a generator of lists of list of sizes.
		*/
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
		recurse(totalSize)
			.flatMap(originalList => Gen.pick(originalList.length, originalList))
			.map(_.toList)
	}
}


object UtilGenerators extends UtilGenerators