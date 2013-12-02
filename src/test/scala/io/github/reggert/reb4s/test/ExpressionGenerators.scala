package io.github.reggert.reb4s.test

import scala.language.postfixOps
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import io.github.reggert.reb4s.{Adopted, Alternation, Alternative, AnyChar, Expression, Flag, Group, InputBegin, InputEnd, InputEndSkipEOL, LineBegin, LineEnd, Literal}
import io.github.reggert.reb4s.{MatchEnd, NonwordBoundary, Quantifiable, Quantified, Raw, Sequence, Sequenceable, WordBoundary}
import io.github.reggert.reb4s.Literal.{CharLiteral, StringLiteral}
import io.github.reggert.reb4s.charclass.{CharClass, CharRange, Intersection, MultiChar, PredefinedClass, Union}
import io.github.reggert.reb4s.EscapedLiteral
import io.github.reggert.reb4s.CompoundRaw
import io.github.reggert.reb4s.charclass.SingleChar
import java.util.regex.Pattern

trait ExpressionGenerators extends CharClassGenerators 
	with UtilGenerators with LiteralGenerators with RawGenerators with AdoptedGenerators
{
	implicit val arbExpression = Arbitrary(Gen.sized {depth => genExpression(depth)})
	implicit val arbAlternation = Arbitrary(Gen.sized {depth => genAlternation(depth)})
	implicit val arbAlternative = Arbitrary(Gen.sized {depth => genAlternative(depth)})
	implicit val arbFlag = Arbitrary(genFlag)
	implicit val arbGroup = Arbitrary(Gen.sized {depth => genGroup(depth)})
	implicit val arbQuantifiable = Arbitrary(Gen.sized {depth => genQuantifiable(depth)})
	implicit val arbQuantified = Arbitrary(Gen.sized {depth => genQuantified(depth)})
	implicit val arbSequence = Arbitrary(Gen.sized {depth => genSequence(depth)})
	implicit val arbSequenceable = Arbitrary(Gen.sized {depth => genSequenceable(depth)})
	
	
	def genExpression(depth : Int) : Gen[Expression] = {
		require (depth >= 0)
		def nonRecursiveGen = Gen.oneOf(genAdopted, genLiteral, genRawQuantifiable, Gen.sized {d => genCharClass(d)})
		def recursiveGen = Gen.oneOf(
				genAlternation(depth - 1),
				genGroup(depth - 1),
				genQuantified(depth - 1),
				genSequence(depth - 1)
			)
		if (depth == 0) nonRecursiveGen else recursiveGen	
	} 
		
	def genAlternation(depth : Int) : Gen[Alternation] = 
		for {
			alternatives <- genRecursiveList(minLength=2){
				for {
					d <- Gen.choose(0, depth)
					alternative <- genAlternative(d)
				} yield alternative
			}
		} yield ((alternatives(0) || alternatives(1)) /: alternatives.drop(2)) {_ || _}
	
	def genAlternative(depth : Int) : Gen[Alternative] = {
		require (depth >= 0)
		def nonRecursiveGen = Gen.oneOf(Gen.sized {d => genCharClass(d)}, genLiteral, Gen.sized {d => genRaw(d)})
		def recursiveGen = Gen.oneOf(
				genAlternation(depth - 1), 
				genGroup(depth - 1), 
				genQuantified(depth - 1), 
				genSequence(depth - 1)
			)
		if (depth == 0) nonRecursiveGen else recursiveGen
	}
	
	def genFlag : Gen[Flag] = Gen.oneOf(
			Flag.CaseInsensitive,
			Flag.UnixLines,
			Flag.Multiline,
			Flag.DotAll,
			Flag.UnicodeCase, 
			Flag.Comments
		)
	
	def genGroup(depth : Int) : Gen[Group] = {
		val expressionGen = genExpression(depth)
		for {
			g <- Gen.oneOf(
					expressionGen.map(Group.Capture),
					expressionGen.map(Group.Independent),
					expressionGen.map(Group.NegativeLookAhead),
					expressionGen.map(Group.NegativeLookBehind),
					expressionGen.map(Group.NonCapturing),
					expressionGen.map(Group.PositiveLookAhead),
					expressionGen.map(Group.PositiveLookBehind),
					(for {
						flags <- Gen.listOf(genFlag)
						genEnabled = Gen.lzy(expressionGen map {e : Expression => Group.EnableFlags(e, flags : _*)})
						genDisabled = Gen.lzy(expressionGen map {e : Expression => Group.DisableFlags(e, flags : _*)})
						g <- Gen.oneOf(genEnabled, genDisabled)
					} yield g)
				)
		} yield g
	}
	
	def genQuantifiable(depth : Int) : Gen[Quantifiable] = {
		require (depth >= 0)
		def nonRecursiveGen = Gen.oneOf(genRawQuantifiable, genCharLiteral)
		def recursiveGen = genGroup(depth - 1)
		if (depth == 0) nonRecursiveGen else recursiveGen
	}
	
	def genQuantified(depth : Int) : Gen[Quantified] = for {
		mode <- Gen.oneOf(Quantified.Greedy, Quantified.Reluctant, Quantified.Possessive)
		quantifiableGen = genQuantifiable(depth)
		quantified <- Gen.oneOf(
				quantifiableGen map (_.anyTimes(mode)),
				quantifiableGen map (_.atLeastOnce(mode)),
				quantifiableGen map (_.optional(mode)),
				(for {
					n <- arbitrary[Int] if n > 0
					quantifiable <- quantifiableGen
				} yield quantifiable.repeat(n, mode)),
				(for {
					List(n, m) <- Gen.listOfN(2, arbitrary[Int]) if (n >= 0 && m > n)
					quantifiable <- quantifiableGen
				} yield quantifiable.repeatRange(n, m, mode)),
				(for {
					n <- arbitrary[Int] if n > 0
					quantifiable <- quantifiableGen	
				} yield quantifiable.atLeast(n, mode))
			)
	} yield quantified
	
	def genSequence(depth : Int) : Gen[Sequence] = for {
		terms <- genRecursiveList(minLength = 2) {
			for {
				d <- Gen.choose(0, depth)
				term <- genSequenceable(d)
			} yield term
		}
	} yield ((terms(0) ~~ terms(1)) /: terms.drop(2)) {_ ~~ _}
	
	def genSequenceable(depth : Int) : Gen[Sequenceable] = {
		require (depth >= 0)
		def nonRecursiveGen = Gen.oneOf(Gen.sized {d => genCharClass(d)}, genLiteral)
		def recursiveGen = Gen.oneOf(genRaw(depth - 1), genGroup(depth - 1))
		if (depth == 0) nonRecursiveGen else recursiveGen
	}
}



object ExpressionGenerators extends ExpressionGenerators