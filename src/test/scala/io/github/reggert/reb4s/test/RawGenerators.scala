package io.github.reggert.reb4s.test

import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

import io.github.reggert.reb4s.{AnyChar, CompoundRaw, EscapedLiteral, InputBegin, InputEnd, InputEndSkipEOL, LineBegin, LineEnd, Literal, MatchEnd, NonwordBoundary, Quantifiable, Raw, WordBoundary}

trait RawGenerators extends UtilGenerators with LiteralGenerators {
	implicit val arbRawQuantifiable = Arbitrary(genRawQuantifiable())
	implicit val arbCompoundRaw : Arbitrary[CompoundRaw] = Arbitrary(genCompoundRaw())
	implicit val arbEscapedLiteral = Arbitrary(genEscapedLiteral())
	implicit val arbRaw = Arbitrary(genRaw())
	

	def genCompoundRaw(rawGen : => Gen[Raw] = arbitrary[Raw]) : Gen[CompoundRaw] = for {
		first <- rawGen
		otherComponents <- genNonEmptyRecursiveList(rawGen)
	} yield CompoundRaw(first::otherComponents)
	
	def genRaw(
			escapedLiteralGen : => Gen[EscapedLiteral] = arbitrary[EscapedLiteral], 
			rawQuantifiableGen : => Gen[Raw with Quantifiable] = arbitrary[Raw with Quantifiable], 
			compoundRawGen : => Gen[CompoundRaw] = Gen.lzy(arbitrary[CompoundRaw])
		) : Gen[Raw] = 
		Gen.frequency(2 -> escapedLiteralGen, 2 -> rawQuantifiableGen, 1 -> compoundRawGen)
	
	def genRawQuantifiable() : Gen[Raw with Quantifiable] = Gen.oneOf(
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
		
	def genEscapedLiteral(literalGen : => Gen[Literal] = arbitrary[Literal]) : Gen[EscapedLiteral] = 
		literalGen map {EscapedLiteral}
}


object RawGenerators extends RawGenerators