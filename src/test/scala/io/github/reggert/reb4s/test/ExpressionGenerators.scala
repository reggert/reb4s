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
	implicit val arbExpression = Arbitrary(genExpression())
	implicit val arbAlternation = Arbitrary(genAlternation())
	implicit val arbAlternative : Arbitrary[Alternative] = Arbitrary(genAlternative())
	implicit val arbFlag = Arbitrary(genFlag)
	implicit val arbGroup = Arbitrary(genGroup())
	implicit val arbQuantifiable = Arbitrary(genQuantifiable())
	implicit val arbQuantified = Arbitrary(genQuantified(arbitrary[Int]))
	implicit val arbSequence : Arbitrary[Sequence] = Arbitrary(genSequence())
	implicit val arbSequenceable = Arbitrary(genSequenceable())
	
	
	def genExpression(
			adoptedGen : => Gen[Adopted] = arbitrary[Adopted],
			literalGen : => Gen[Literal] = arbitrary[Literal],
			rawQuantifiableGen : => Gen[Raw with Quantifiable] = arbitrary[Raw with Quantifiable],
			charClassGen : => Gen[CharClass] = arbitrary[CharClass],
			alternationGen : => Gen[Alternation] = arbitrary[Alternation],
			groupGen : => Gen[Group] = arbitrary[Group],
			sequenceGen : => Gen[Sequence] = arbitrary[Sequence]
		) : Gen[Expression] = Gen.oneOf(
			Gen.oneOf(
					arbitrary[Adopted],
					arbitrary[Literal], 
					arbitrary[Raw with Quantifiable],
					arbitrary[CharClass]
				),
			Gen.oneOf(
					Gen.lzy(arbitrary[Alternation]), 
					Gen.lzy(arbitrary[Group]), 
					Gen.lzy(arbitrary[Quantified]), 
					Gen.lzy(arbitrary[Sequence])
				)
		)
		
	def genAlternation(alternativeGen : => Gen[Alternative] = arbitrary[Alternative]) : Gen[Alternation] = for {
		first <- alternativeGen
		otherAlternatives <- genNonEmptyRecursiveList(alternativeGen)
	} yield ((first || otherAlternatives.head) /: otherAlternatives.tail) {_ || _}
	
	def genAlternative(
			charClassGen : => Gen[CharClass] = arbitrary[CharClass], 
			literalGen : => Gen[Literal] = arbitrary[Literal], 
			rawGen : => Gen[Raw] = arbitrary[Raw],
			alternationGen : => Gen[Alternation] = arbitrary[Alternation],
			groupGen : => Gen[Group] = arbitrary[Group],
			quantifiedGen : => Gen[Quantified] = arbitrary[Quantified],
			sequenceGen : => Gen[Sequence] = arbitrary[Sequence]
		) : Gen[Alternative] = Gen.oneOf(
			Gen.oneOf(charClassGen, literalGen, rawGen),
			Gen.oneOf(Gen.lzy(alternationGen), Gen.lzy(groupGen), Gen.lzy(quantifiedGen), Gen.lzy(sequenceGen))
		)
	
	def genFlag() : Gen[Flag] = Gen.oneOf(
			Flag.CaseInsensitive,
			Flag.UnixLines,
			Flag.Multiline,
			Flag.DotAll,
			Flag.UnicodeCase, 
			Flag.Comments
		)
	
	def genGroup(
			flagGen : => Gen[Flag] = arbitrary[Flag], 
			expressionGen : => Gen[Expression] = arbitrary[Expression]
		) : Gen[Group] = for {
		g <- Gen.oneOf(
				expressionGen.map(Group.Capture),
				expressionGen.map(Group.Independent),
				expressionGen.map(Group.NegativeLookAhead),
				expressionGen.map(Group.NegativeLookBehind),
				expressionGen.map(Group.NonCapturing),
				expressionGen.map(Group.PositiveLookAhead),
				expressionGen.map(Group.PositiveLookBehind),
				(for {
					flags <- Gen.listOf(flagGen)
					genEnabled = Gen.lzy(expressionGen map {e : Expression => Group.EnableFlags(e, flags : _*)})
					genDisabled = Gen.lzy(expressionGen map {e : Expression => Group.DisableFlags(e, flags : _*)})
					g <- Gen.oneOf(genEnabled, genDisabled)
				} yield g)
			)
	} yield g
	
	def genQuantifiable(
			rawQuantifiableGen : => Gen[Raw with Quantifiable] = arbitrary[Raw with Quantifiable], 
			charLiteralGen : => Gen[CharLiteral] = arbitrary[CharLiteral],
			groupGen : => Gen[Group] = arbitrary[Group]
		) : Gen[Quantifiable] = 
		Gen.oneOf(rawQuantifiableGen, charLiteralGen, Gen.lzy(groupGen))
	
	def genQuantified(intGen : Gen[Int] = arbitrary[Int]) : Gen[Quantified] = for {
		mode <- Gen.oneOf(Quantified.Greedy, Quantified.Reluctant, Quantified.Possessive)
		q <- Gen.oneOf(
				Gen.oneOf(
						Gen.resultOf[Quantifiable, Quantified](_ *),
						Gen.resultOf[Quantifiable, Quantified](_ *?),
						Gen.resultOf[Quantifiable, Quantified](_ *+),
						Gen.resultOf[Quantifiable, Quantified](_ +),
						Gen.resultOf[Quantifiable, Quantified](_ +?),
						Gen.resultOf[Quantifiable, Quantified](_ ++),
						Gen.resultOf[Quantifiable, Quantified](_ ?),
						Gen.resultOf[Quantifiable, Quantified](_ ??),
						Gen.resultOf[Quantifiable, Quantified](_ ?+)
					),
				Gen.oneOf(
						(for {
							n <- intGen if n > 0
							repeated <- Gen.resultOf[Quantifiable, Quantified](_.repeat(n, mode))
						} yield repeated),
						(for {
							List(n, m) <- Gen.listOfN(2, intGen) if (n >= 0 && m > n)
							repeated <- Gen.resultOf[Quantifiable, Quantified](_.repeatRange(n, m, mode))
						} yield repeated),
						(for {
							n <- intGen if n > 0
							repeated <- Gen.resultOf[Quantifiable, Quantified](_.atLeast(n, mode))
						} yield repeated)
					)
			)
	} yield q
	
	def genSequence(sequenceableGen : => Gen[Sequenceable] = arbitrary[Sequenceable]) : Gen[Sequence] = for {
		first <- sequenceableGen
		otherTerms <- genNonEmptyRecursiveList(sequenceableGen) 
	} yield ((first ~~ otherTerms.head) /: otherTerms.tail) {_ ~~ _}
	
	def genSequenceable(
			charClassGen : => Gen[CharClass] = arbitrary[CharClass],
			literalGen : => Gen[Literal] = arbitrary[Literal],
			rawGen : => Gen[Raw] = arbitrary[Raw],
			groupGen : => Gen[Group] = arbitrary[Group]
		) : Gen[Sequenceable] = 
			Gen.oneOf(charClassGen, literalGen, rawGen, Gen.lzy(groupGen))
}



object ExpressionGenerators extends ExpressionGenerators