package io.github.reggert.reb4s.test

import org.scalacheck.{Arbitrary, Gen}
import Arbitrary.arbitrary
import io.github.reggert.reb4s.Adopted
import java.util.regex.Pattern


trait AdoptedGenerators {
	implicit val arbPattern = Arbitrary(genPattern)
	implicit val arbAdopted = Arbitrary(genAdopted)
	
	
	// This generator only generates patterns for quoted strings
	def genPattern : Gen[Pattern] = for {
		s <- arbitrary[String]
	} yield Pattern.compile(Pattern.quote(s))
	
	def genAdopted : Gen[Adopted] = for {
		p <- arbitrary[Pattern]
	} yield Adopted.fromPattern(p)
}