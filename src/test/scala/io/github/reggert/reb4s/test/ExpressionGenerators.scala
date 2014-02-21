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
	implicit val arbExpression = 
		Arbitrary(Gen.sized {size => Gen.choose(1, size) flatMap (genExpression)})
	implicit val arbAlternation = 
		Arbitrary(Gen.sized {size => Gen.choose(2, size) flatMap (genAlternation)})
	implicit val arbAlternative = 
		Arbitrary(Gen.sized {size => Gen.choose(1, size) flatMap (genAlternative)})
	implicit val arbFlag = Arbitrary(genFlag)
	implicit val arbGroup = 
		Arbitrary(Gen.sized {size => Gen.choose(1, size) flatMap (genGroup)})
	implicit val arbQuantifiable = 
		Arbitrary(Gen.sized {size => Gen.choose(1, size) flatMap (genQuantifiable)})
	implicit val arbQuantified = 
		Arbitrary(Gen.sized {size => Gen.choose(1, size) flatMap (genQuantified)})
	implicit val arbSequence = 
		Arbitrary(Gen.sized {size => Gen.choose(2, size) flatMap (genSequence)})
	implicit val arbSequenceable = 
		Arbitrary(Gen.sized {size => Gen.choose(1, size) flatMap (genSequenceable)})
	
	
	def genExpression(size : Int) : Gen[Expression] = {
		require (size > 0, s"size=$size <= 0")
		size match {
			case 1 => Gen.oneOf(
					genAdopted(1), 
					genLiteral(1), 
					genRawQuantifiable, 
					genCharClass(1), 
					genQuantified(1)
				)
			case _ => Gen.oneOf(
					genAdopted(size),
					genLiteral(size),
					genCharClass(size),
					Gen.lzy(genAlternation(size)),
					Gen.lzy(genSequence(size)),
					// Subtract 1 to avoid recursing indefinitely
					Gen.lzy(genGroup(size - 1)), 
					Gen.lzy(genQuantified(size - 1))
				)
		}
	} 
	
	private def genCombined[CombinedType <: Expression, SubtreeType <: Expression](size : Int)
		(genSubtree : Int => Gen[SubtreeType])
		(combine : (SubtreeType, SubtreeType) => CombinedType)
		(fold : (CombinedType, SubtreeType) => CombinedType) : Gen[CombinedType] = 
	{
		require(size >= 2, s"size=$size < 2")
		val sizesGen = size match
		{
			case 2 => Gen.const(1::1::Nil)
			case _ => genSizes(size) filter {_.length >= 2}
		}
		for {
			sizes <- sizesGen
			subtreeGens = for {s <- sizes} yield genSubtree(s) 
			subtreesGen = (Gen.const(Nil : List[SubtreeType]) /: subtreeGens) {(ssGen, sGen) => 
				for {
					ss <- ssGen
					s <- sGen
				} yield s::ss
			}
			subtrees <- subtreesGen
		} yield (combine(subtrees(0), subtrees(1)) /: subtrees.drop(2)) (fold)
	}
		
	
	def genAlternation(size : Int) : Gen[Alternation] = 
		genCombined(size)(genAlternative)(_ || _)(_ || _)
		
	
	def genAlternative(size : Int) : Gen[Alternative] = {
		require (size > 0, s"size=$size <= 0")
		size match {
			case 1 => Gen.oneOf(genLiteral(1), genRaw(1), genCharClass(1))
			case _ => Gen.lzy(Gen.oneOf(
					genCharClass(size),
					genAlternation(size),
					genSequence(size),
					genGroup(size - 1),
					genQuantified(size - 1)
				))
		}
	}
	
	def genFlag : Gen[Flag] = Gen.oneOf(
			Flag.CaseInsensitive,
			Flag.UnixLines,
			Flag.Multiline,
			Flag.DotAll,
			Flag.UnicodeCase, 
			Flag.Comments
		)
	
	def genGroup(size : Int) : Gen[Group] = {
		val expressionGen = genExpression(size)
		for {
			g <- Gen.oneOf(
					expressionGen.map(Group.Capture),
					expressionGen.map(Group.Independent),
					expressionGen.map(Group.NegativeLookAhead),
					expressionGen.filter(_.boundedLength.isDefined).map(Group.NegativeLookBehind),
					expressionGen.map(Group.NonCapturing),
					expressionGen.map(Group.PositiveLookAhead),
					expressionGen.filter(_.boundedLength.isDefined).map(Group.PositiveLookBehind),
					(for {
						flags <- Gen.choose(0, 6) flatMap {flagCount => Gen.listOfN(flagCount, genFlag)}
						genEnabled = Gen.lzy(expressionGen map {e : Expression => Group.EnableFlags(e, flags : _*)})
						genDisabled = Gen.lzy(expressionGen map {e : Expression => Group.DisableFlags(e, flags : _*)})
						g <- Gen.oneOf(genEnabled, genDisabled)
					} yield g)
				)
		} yield g
	}
	
	def genQuantifiable(size : Int) : Gen[Quantifiable] = {
		require (size > 0, s"size=$size <= 0")
		size match {
			case 1 =>
				Gen.oneOf(
						genRawQuantifiable,
						genCharLiteral
					)
			case _ => Gen.lzy(genGroup(size - 1))	
		}
	}
	
	def genQuantified(size : Int) : Gen[Quantified] = {
		require(size > 0, s"size=$size <= 0")
		for {
			mode <- Gen.oneOf(Quantified.Greedy, Quantified.Reluctant, Quantified.Possessive)
			quantifiableGen = genQuantifiable(size)
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
	}

	
	def genSequence(size : Int) : Gen[Sequence] = 
		genCombined(size)(genSequenceable)(_ ~~ _)(_ ~~ _)
	
		
	def genSequenceable(size : Int) : Gen[Sequenceable] = {
		require (size > 0, s"size=$size <= 0")
		size match {
			case 1 => Gen.oneOf(
					genCharClass(1),
					genLiteral(1),
					genRaw(1)
				)
			case _ =>
				Gen.oneOf(
						genCharClass(size),
						genLiteral(size),
						genRaw(size),
						Gen.lzy(genGroup(size - 1))
					)
		}
	}
}



object ExpressionGenerators extends ExpressionGenerators