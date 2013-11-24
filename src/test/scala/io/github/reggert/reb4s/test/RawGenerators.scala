package io.github.reggert.reb4s.test

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

import io.github.reggert.reb4s.{AnyChar, CompoundRaw, EscapedLiteral, InputBegin, InputEnd, InputEndSkipEOL, LineBegin, LineEnd, Literal, MatchEnd, NonwordBoundary, Quantifiable, Raw, WordBoundary}

trait RawGenerators extends UtilGenerators with LiteralGenerators {
	implicit val arbRawQuantifiable = Arbitrary(genRawQuantifiable)
	implicit val arbCompoundRaw : Arbitrary[CompoundRaw] = Arbitrary(Gen.sized {depth => genCompoundRaw(depth)})
	implicit val arbEscapedLiteral = Arbitrary(genEscapedLiteral)
	implicit val arbRaw = Arbitrary(Gen.sized {depth => genRaw(depth)})
	

	def genCompoundRaw(depth : Int) : Gen[CompoundRaw] = for {
		first <- genRaw(depth)
		otherComponents <- genNonEmptyRecursiveList(genRaw(depth))
	} yield CompoundRaw(first::otherComponents)
	
	def genRaw(depth : Int) : Gen[Raw] = 
		if (depth == 0)
			Gen.oneOf(genEscapedLiteral, genRawQuantifiable)
		else
			genCompoundRaw(depth - 1)
	
	def genRawQuantifiable : Gen[Raw with Quantifiable] = Gen.oneOf(
			AnyChar,
			LineBegin,
			LineEnd,
			WordBoundary,
			NonwordBoundary,
			InputBegin,
			MatchEnd,
			InputEndSkipEOL,
			InputEnd
		)
		
	def genEscapedLiteral : Gen[EscapedLiteral] = 
		genLiteral map {EscapedLiteral}
}


object RawGenerators extends RawGenerators