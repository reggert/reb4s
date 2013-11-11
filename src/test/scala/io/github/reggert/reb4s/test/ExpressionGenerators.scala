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
	with LiteralGenerators with RawGenerators with AdoptedGenerators
{
	implicit val arbExpression = Arbitrary(genExpression)
	implicit val arbAlternation = Arbitrary(genAlternation)
	implicit val arbAlternative = Arbitrary(genAlternative)
	implicit val arbFlag = Arbitrary(genFlag)
	implicit val arbGroup = Arbitrary(genGroup)
	implicit val arbQuantifiable = Arbitrary(genQuantifiable)
	implicit val arbQuantified = Arbitrary(genQuantified)
	implicit val arbSequence = Arbitrary(genSequence)
	implicit val arbSequenceable = Arbitrary(genSequenceable)
	
	
	def genExpression : Gen[Expression] = Gen.oneOf(
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
		
	def genAlternation : Gen[Alternation] = for {
		first <- arbitrary[Alternative]
		second <- arbitrary[Alternative]
		otherAlternatives <- Gen.listOf(arbitrary[Alternative])
	} yield ((first || second) /: otherAlternatives) {_ || _}
	
	def genAlternative : Gen[Alternative] = Gen.oneOf(
			Gen.oneOf(
					arbitrary[CharClass],
					arbitrary[Literal],
					arbitrary[Raw]
				),
			Gen.oneOf(
					Gen.lzy(arbitrary[Alternation]),
					Gen.lzy(arbitrary[Group]),
					Gen.lzy(arbitrary[Quantified]),
					Gen.lzy(arbitrary[Sequence])
				)
		)
	
	def genFlag : Gen[Flag] = Gen.oneOf(
			Flag.CaseInsensitive,
			Flag.UnixLines,
			Flag.Multiline,
			Flag.DotAll,
			Flag.UnicodeCase, 
			Flag.Comments
		)
	
	def genGroup : Gen[Group] = for {
		g <- Gen.oneOf(
				Gen.resultOf(Group.Capture),
				Gen.resultOf(Group.Independent),
				Gen.resultOf(Group.NegativeLookAhead),
				Gen.resultOf(Group.NegativeLookBehind),
				Gen.resultOf(Group.NonCapturing),
				Gen.resultOf(Group.PositiveLookAhead),
				Gen.resultOf(Group.PositiveLookBehind),
				(for {
					flags <- Gen.listOf(arbitrary[Flag])
					genEnabled = Gen.lzy(Gen.resultOf {e : Expression => Group.EnableFlags(e, flags : _*)})
					genDisabled = Gen.lzy(Gen.resultOf {e : Expression => Group.DisableFlags(e, flags : _*)})
					g <- Gen.oneOf(genEnabled, genDisabled)
				} yield g)
			)
	} yield g
	
	def genQuantifiable : Gen[Quantifiable] = Gen.oneOf(
			arbitrary[Raw with Quantifiable],
			arbitrary[CharLiteral],
			Gen.lzy(arbitrary[Group])
		)
	
	def genQuantified : Gen[Quantified] = for {
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
							n <- arbitrary[Int] if n > 0
							greedy <- Gen.resultOf[Quantifiable, Quantified](_ repeat n)
							reluctant <- Gen.resultOf[Quantifiable, Quantified](_ repeatReluctantly n)
							possessive <- Gen.resultOf[Quantifiable, Quantified](_ repeatPossessively n)
							repeated <- Gen.oneOf(greedy, reluctant, possessive)
						} yield repeated),
						(for {
							(n, m) <- arbitrary[(Int, Int)] if (n >= 0 && m > n)
							greedy <- Gen.resultOf[Quantifiable, Quantified](_.repeat(n, m))
							reluctant <- Gen.resultOf[Quantifiable, Quantified](_.repeatReluctantly(n, m))
							possessive <- Gen.resultOf[Quantifiable, Quantified](_.repeatPossessively(n, m))
							repeated <- Gen.oneOf(greedy, reluctant, possessive)
						} yield repeated),
						(for {
							n <- arbitrary[Int] if n > 0
							greedy <- Gen.resultOf[Quantifiable, Quantified](_ atLeast n)
							reluctant <- Gen.resultOf[Quantifiable, Quantified](_ atLeastReluctantly n)
							possessive <- Gen.resultOf[Quantifiable, Quantified](_ atLeastPossessively n)
							repeated <- Gen.oneOf(greedy, reluctant, possessive)
						} yield repeated)
					)
			)
	} yield q
	
	def genSequence : Gen[Sequence] = for {
		first <- arbitrary[Sequenceable]
		second <- arbitrary[Sequenceable]
		otherTerms <- Gen.listOf(arbitrary[Sequenceable]) 
	} yield ((first ~~ second) /: otherTerms) {_ ~~ _}
	
	def genSequenceable : Gen[Sequenceable] = Gen.oneOf(
			arbitrary[CharClass],
			arbitrary[Literal],
			arbitrary[Raw],
			Gen.lzy(arbitrary[Group]) 
		)
}



object ExpressionGenerators extends ExpressionGenerators