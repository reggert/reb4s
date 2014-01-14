package io.github.reggert.reb4s.test

import org.scalacheck.{Arbitrary, Gen}
import Arbitrary.arbitrary
import io.github.reggert.reb4s.Adopted
import java.util.regex.Pattern


trait AdoptedGenerators {
	implicit val arbAdopted = Arbitrary(Gen.sized {size => Gen.choose(1, size) flatMap (genAdopted)})
	
	
	// This generator only generates patterns for quoted strings
	private def genPattern(size : Int) : Gen[Pattern] = for {
		s <- Gen.listOfN(size, arbitrary[Char])
	} yield Pattern.compile(Pattern.quote(s.mkString))
	
	def genAdopted(size : Int) : Gen[Adopted] = for {
		p <- genPattern(size)
	} yield Adopted.fromPattern(p)
}