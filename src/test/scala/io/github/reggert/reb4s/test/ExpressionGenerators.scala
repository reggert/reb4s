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

trait ExpressionGenerators {
	
	implicit val arbExpression = Arbitrary(genExpression)
	implicit val arbPattern = Arbitrary(genPattern)
	implicit val arbAdopted = Arbitrary(genAdopted)
	implicit val arbAlternation = Arbitrary(genAlternation)
	implicit val arbAlternative = Arbitrary(genAlternative)
	implicit val arbFlag = Arbitrary(genFlag)
	implicit val arbGroup = Arbitrary(genGroup)
	implicit val arbCharLiteral = Arbitrary(genCharLiteral)
	implicit val arbStringLiteral = Arbitrary(genStringLiteral)
	implicit val arbLiteral = Arbitrary(genLiteral)
	implicit val arbQuantifiable = Arbitrary(genQuantifiable)
	implicit val arbQuantified = Arbitrary(genQuantified)
	implicit val arbSequence = Arbitrary(genSequence)
	implicit val arbSequenceable = Arbitrary(genSequenceable)
	implicit val arbCharClass = Arbitrary(genCharClass)
	implicit val arbCharRange = Arbitrary(genCharRange)
	implicit val arbIntersection = Arbitrary(genIntersection)
	implicit val arbMultiChar = Arbitrary(genMultiChar)
	implicit val arbSingleChar = Arbitrary(genSingleChar)
	implicit val arbPredefinedClass = Arbitrary(genPredefinedClass)
	implicit val arbCompoundRaw = Arbitrary(genCompoundRaw)
	implicit val arbRaw = Arbitrary(genRaw)
	implicit val arbRawQuantifiable = Arbitrary(genRawQuantifiable)
	implicit val arbUnion = Arbitrary(genUnion)
	
	
	def genExpression : Gen[Expression] = Gen.oneOf(
			Gen.oneOf(
					arbitrary[Adopted],
					arbitrary[Literal], 
					arbitrary[Raw with Quantifiable]
				),
			Gen.oneOf(
					Gen.lzy(arbitrary[Alternation]), 
					Gen.lzy(arbitrary[Group]), 
					Gen.lzy(arbitrary[Quantified]), 
					Gen.lzy(arbitrary[Sequence])
				)
		)
		
	// This generator only generates patterns for quoted strings
	def genPattern : Gen[Pattern] = for {
		s <- arbitrary[String]
	} yield Pattern.compile(Pattern.quote(s))
	
	def genAdopted : Gen[Adopted] = for {
		p <- arbitrary[Pattern]
	} yield Adopted.fromPattern(p)
	
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
	
	def genCharLiteral : Gen[CharLiteral] = for {
		c <- arbitrary[Char]
	} yield CharLiteral(c)
	
	def genStringLiteral : Gen[StringLiteral] = for {
		s <- arbitrary[String]
	} yield StringLiteral(s)
	
	def genLiteral : Gen[Literal] = Gen.oneOf(arbitrary[CharLiteral], arbitrary[StringLiteral])
	
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
	
	def genCharClass : Gen[CharClass] = for {
		cc <- Gen.oneOf(
				arbitrary[SingleChar],
				arbitrary[MultiChar],
				arbitrary[CharRange],
				arbitrary[PredefinedClass],
				Gen.lzy(arbitrary[Intersection]),
				Gen.lzy(arbitrary[Union])
			)
		ccn <- Gen.oneOf(cc, ~cc)
	} yield ccn
	
	def genCharRange : Gen[CharRange] = for {
		(first, last) <- arbitrary[(Char, Char)]
		if first < last
	} yield CharClass.range(first, last)
	
	def genIntersection : Gen[Intersection] = for {
		first <- arbitrary[CharClass]
		second <- arbitrary[CharClass]
		otherSupersets <- Gen.listOf(arbitrary[CharClass])
	} yield ((first && second) /: otherSupersets) {_ && _}
	
	def genMultiChar : Gen[MultiChar] = for {
		(first, second) <- arbitrary[(Char, Char)] if first != second
		otherChars <- Gen.listOf(arbitrary[Char])
	} yield CharClass.chars(first::second::otherChars)
	
	def genSingleChar : Gen[SingleChar] = for {
		c <- arbitrary[Char]
	} yield CharClass.char(c)
	
	def genPredefinedClass : Gen[PredefinedClass] = Gen.oneOf(
			CharClass.Perl.Digit,
			CharClass.Perl.Space,
			CharClass.Perl.Word,
			CharClass.Posix.Alnum,
			CharClass.Posix.Alpha,
			CharClass.Posix.Blank,
			CharClass.Posix.Control,
			CharClass.Posix.Digit,
			CharClass.Posix.Graph,
			CharClass.Posix.HexDigit,
			CharClass.Posix.Lower,
			CharClass.Posix.Print,
			CharClass.Posix.Punct,
			CharClass.Posix.Space,
			CharClass.Posix.Upper,
			CharClass.Java.LowerCase,
			CharClass.Java.Mirrored,
			CharClass.Java.UpperCase,
			CharClass.Java.Whitespace,
			//CharClass.Unicode.block(arbitrary[UnicodeBlock]),
			CharClass.Unicode.Letter.Lowercase,
			CharClass.Unicode.Letter.Modifier,
			CharClass.Unicode.Letter.Other,
			CharClass.Unicode.Letter.Titlecase,
			CharClass.Unicode.Letter.Uppercase,
			CharClass.Unicode.Mark.Enclosing,
			CharClass.Unicode.Mark.Nonspacing,
			CharClass.Unicode.Mark.SpacingCombining,
			CharClass.Unicode.Number.DecimalDigit,
			CharClass.Unicode.Number.Letter,
			CharClass.Unicode.Number.Other,
			CharClass.Unicode.Other.Control,
			CharClass.Unicode.Other.Format,
			CharClass.Unicode.Other.NotAssigned,
			CharClass.Unicode.Other.PrivateUse,
			CharClass.Unicode.Other.Surrogate,
			CharClass.Unicode.Punctuation.Close,
			CharClass.Unicode.Punctuation.Connector,
			CharClass.Unicode.Punctuation.Dash,
			CharClass.Unicode.Punctuation.FinalQuote,
			CharClass.Unicode.Punctuation.InitialQuote,
			CharClass.Unicode.Punctuation.Open,
			CharClass.Unicode.Punctuation.Other,
			CharClass.Unicode.Separator.Line,
			CharClass.Unicode.Separator.Paragraph,
			CharClass.Unicode.Separator.Space,
			CharClass.Unicode.Symbol.Currency,
			CharClass.Unicode.Symbol.Math,
			CharClass.Unicode.Symbol.Modifier,
			CharClass.Unicode.Symbol.Other
		)
	
	def genCompoundRaw : Gen[CompoundRaw] = for {
		first <- arbitrary[Raw]
		second <- arbitrary[Raw]
		otherComponents <- Gen.listOf(arbitrary[Raw])
	} yield CompoundRaw(first::second::otherComponents)
	
	def genRaw : Gen[Raw] = Gen.oneOf(
			arbitrary[Raw with Quantifiable], 
			for {lit <- arbitrary[Literal]} yield EscapedLiteral(lit),
			Gen.lzy(arbitrary[CompoundRaw])
		)
	
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
	
	def genUnion : Gen[Union] = for {
		first <- arbitrary[CharClass]
		second <- arbitrary[CharClass]
		otherSubsets <- Gen.listOf(arbitrary[CharClass])
	} yield ((first || second) /: otherSubsets) {_ || _}
	
	
}