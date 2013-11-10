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
			arbitrary[Alternation], 
			arbitrary[Adopted],
			arbitrary[Group], 
			arbitrary[Literal], 
			arbitrary[Quantified], 
			arbitrary[Raw with Quantifiable], 
			arbitrary[Sequence]
		)
		
	// This generator only generates patterns for quoted strings
	def genPattern : Gen[Pattern] = for {
		s <- arbitrary[String]
	} yield Pattern.compile(Pattern.quote(s))
	
	def genAdopted : Gen[Adopted] = for {
		p <- arbitrary[Pattern]
	} yield Adopted.fromPattern(p)
	
	def genAlternation : Gen[Alternation] = for {
		(first, second) <- arbitrary[(Alternative, Alternative)]
		otherAlternatives <- Gen.listOf(arbitrary[Alternative])
	} yield ((first || second) /: otherAlternatives) {_ || _}
	
	def genAlternative : Gen[Alternative] = Gen.oneOf(
			arbitrary[Alternation],
			arbitrary[CharClass],
			arbitrary[Group],
			arbitrary[Literal],
			arbitrary[Quantified],
			arbitrary[Raw],
			arbitrary[Sequence]
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
		e <- arbitrary[Expression]
		g <- Gen.oneOf(
				Gen.const(Group.Capture(e)),
				for {flags <- Gen.listOf(arbitrary[Flag])} yield Group.DisableFlags(e, flags : _*),
				for {flags <- Gen.listOf(arbitrary[Flag])} yield Group.EnableFlags(e, flags : _*),
				Gen.const(Group.Independent(e)),
				Gen.const(Group.NegativeLookAhead(e)),
				Gen.const(Group.NegativeLookBehind(e)),
				Gen.const(Group.NonCapturing(e)),
				Gen.const(Group.PositiveLookAhead(e)),
				Gen.const(Group.PositiveLookBehind(e))
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
			arbitrary[Group]
		)
	
	def genQuantified : Gen[Quantified] = for {
		q <- arbitrary[Quantifiable]
		qq <- Gen.oneOf(
				Gen.const(q *),
				Gen.const(q *?),
				Gen.const(q *+),
				Gen.const(q +),
				Gen.const(q +?),
				Gen.const(q ++),
				Gen.const(q ?),
				Gen.const(q ??),
				Gen.const(q ?+),
				for {n <- arbitrary[Int] suchThat {_ > 0}} yield (q repeat n),
				for {n <- arbitrary[Int] suchThat {_ > 0}} yield (q repeatReluctantly n),
				for {n <- arbitrary[Int] suchThat {_ > 0}} yield (q repeatPossessively n),
				for {(n, m) <- arbitrary[(Int, Int)] suchThat {case (n, m) => (n >= 0 && m > n)}} yield (q.repeat(n, m)),
				for {(n, m) <- arbitrary[(Int, Int)] suchThat {case (n, m) => (n >= 0 && m > n)}} yield (q.repeatReluctantly(n, m)),
				for {(n, m) <- arbitrary[(Int, Int)] suchThat {case (n, m) => (n >= 0 && m > n)}} yield (q.repeatPossessively(n, m)),
				for {n <- arbitrary[Int] suchThat {_ > 0}} yield q.atLeast(n),
				for {n <- arbitrary[Int] suchThat {_ > 0}} yield q.atLeastReluctantly(n),
				for {n <- arbitrary[Int] suchThat {_ > 0}} yield q.atLeastPossessively(n)
			)
	} yield qq
	
	def genSequence : Gen[Sequence] = for {
		(first, second) <- arbitrary[(Sequenceable, Sequenceable)]
		otherTerms <- Gen.listOf(arbitrary[Sequenceable]) 
	} yield ((first ~~ second) /: otherTerms) {_ ~~ _}
	
	def genSequenceable : Gen[Sequenceable] = Gen.oneOf(arbitrary[CharClass], arbitrary[Group], arbitrary[Raw])
	
	def genCharClass : Gen[CharClass] = for {
		cc <- Gen.oneOf(
				arbitrary[SingleChar],
				arbitrary[MultiChar],
				arbitrary[CharRange],
				arbitrary[PredefinedClass],
				arbitrary[Intersection],
				arbitrary[Union]
			)
		ccn <- Gen.oneOf(cc, ~cc)
	} yield ccn
	
	def genCharRange : Gen[CharRange] = for {
		(min, max) <- arbitrary[(Char, Char)] suchThat {case (a, b) => a < b}
	} yield CharClass.range(min, max)
	
	def genIntersection : Gen[Intersection] = for {
		(first, second) <- arbitrary[(CharClass, CharClass)]
		otherSupersets <- Gen.listOf(arbitrary[CharClass])
	} yield ((first && second) /: otherSupersets) {_ && _}
	
	def genMultiChar : Gen[MultiChar] = for {
		chars <- Gen.listOf(arbitrary[Char])
	} yield CharClass.chars(chars)
	
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
		components <- Gen.listOf(arbitrary[Raw])
	} yield CompoundRaw(components)
	
	def genRaw : Gen[Raw] = Gen.oneOf(
			arbitrary[Raw with Quantifiable], 
			for {lit <- arbitrary[Literal]} yield EscapedLiteral(lit),
			arbitrary[CompoundRaw]
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
		(first, second) <- arbitrary[(CharClass, CharClass)]
		otherSubsets <- Gen.listOf(arbitrary[CharClass])
	} yield ((first || second) /: otherSubsets) {_ || _}
	
	
}