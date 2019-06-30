package io.github.reggert.reb4s.test

import io.github.reggert.reb4s._
import org.scalacheck.{Arbitrary, Gen}


trait RawGenerators extends UtilGenerators with LiteralGenerators {
	implicit val arbRawQuantifiable: Arbitrary[Raw with Quantifiable] = Arbitrary(genRawQuantifiable)
	implicit val arbCompoundRaw : Arbitrary[CompoundRaw] = 
		Arbitrary(Gen.sized {size => if (size < 2) Gen.fail else Gen.choose(2, size) flatMap genCompoundRaw })
	implicit val arbEscapedLiteral: Arbitrary[EscapedLiteral] =
		Arbitrary(Gen.sized {size => if (size < 1) Gen.fail else Gen.choose(1, size) flatMap genEscapedLiteral })
	implicit val arbRaw: Arbitrary[Raw] =
		Arbitrary(Gen.sized { size => if (size < 1) Gen.fail else Gen.choose(1, size) flatMap genRaw })
	

	def genCompoundRaw(size : Int) : Gen[CompoundRaw] = {
		require(size >= 2, s"size=$size < 2")
		val sizesGen = size match
		{
			case 2 => Gen.const(1::1::Nil)
			case _ => genSizes(size) filter {_.length >= 2}
		}
		for {
			sizes <- sizesGen
			subtreeGens = for {s <- sizes} yield genRaw(s) 
			subtreesGen = (Gen.const(Nil : List[Raw]) /: subtreeGens) {(ssGen, sGen) => 
				for {
					ss <- ssGen
					s <- sGen
				} yield s::ss
			}
			subtrees <- subtreesGen
		} yield CompoundRaw(subtrees)
	}
	
	def genRaw(size : Int) : Gen[Raw] = {
		require (size > 0, s"size=$size <= 0")
		size match {
			case 1 => Gen.oneOf(genEscapedLiteral(1), genRawQuantifiable)
			case _ => Gen.oneOf(genEscapedLiteral(size), Gen.lzy(genCompoundRaw(size)))
		}
	} 
		
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
		
	def genEscapedLiteral(size : Int) : Gen[EscapedLiteral] = 
		genLiteral(size) map EscapedLiteral
}


object RawGenerators extends RawGenerators