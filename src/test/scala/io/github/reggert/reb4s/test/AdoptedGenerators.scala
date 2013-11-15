package io.github.reggert.reb4s.test

import org.scalacheck.{Arbitrary, Gen}
import Arbitrary.arbitrary
import io.github.reggert.reb4s.Adopted
import java.util.regex.Pattern


trait AdoptedGenerators {
	implicit val arbAdopted = Arbitrary(genAdopted())
	
	
	// This generator only generates patterns for quoted strings
	private def genPattern : Gen[Pattern] = for {
		s <- arbitrary[String]
	} yield Pattern.compile(Pattern.quote(s))
	
	def genAdopted(patternGen : => Gen[Pattern] = genPattern) : Gen[Adopted] = for {
		p <- patternGen
	} yield Adopted.fromPattern(p)
}