package io.github.reggert.reb4s.test

import org.scalacheck.Arbitrary
import Arbitrary.arbitrary
import org.scalacheck.Gen
import io.github.reggert.reb4s.charclass.PredefinedClass
import io.github.reggert.reb4s.charclass.SingleChar
import io.github.reggert.reb4s.charclass.MultiChar
import io.github.reggert.reb4s.charclass.CharRange
import io.github.reggert.reb4s.charclass.Intersection
import io.github.reggert.reb4s.charclass.CharClass
import io.github.reggert.reb4s.charclass.Union

trait CharClassGenerators extends UtilGenerators {
	implicit val arbCharClass : Arbitrary[CharClass] = Arbitrary(Gen.sized {depth => genCharClass(depth)})
	implicit val arbCharRange = Arbitrary(genCharRange)
	implicit val arbIntersection = Arbitrary(Gen.sized {depth => genIntersection(depth)})
	implicit val arbUnion = Arbitrary(Gen.sized {depth => genUnion(depth)})
	implicit val arbMultiChar = Arbitrary(genMultiChar)
	implicit val arbSingleChar = Arbitrary(genSingleChar)
	implicit val arbPredefinedClass = Arbitrary(genPredefinedClass)
	
	
	def genCharClass(depth : Int) : Gen[CharClass] =
	{
		require (depth >= 0)
		def nonRecursiveGen = 
			Gen.oneOf(
					genSingleChar,
					genMultiChar,
					genCharRange,
					genPredefinedClass
				)
		def recursiveGen = Gen.oneOf(genIntersection(depth - 1), genUnion(depth - 1))
		for {
			cc <- if (depth == 0) nonRecursiveGen else Gen.lzy(recursiveGen)
			ccn <- Gen.oneOf(cc, ~cc)
		} yield ccn
	}
	
	def genCharRange : Gen[CharRange] = for {
		first::last::Nil <- Gen.listOfN(2, arbitrary[Char])
		if first < last
	} yield CharClass.range(first, last)
	
	def genIntersection(depth : Int) : Gen[Intersection] = for {
		supersets <- genRecursiveList(minLength = 2) {genCharClass(depth)}
	} yield ((supersets(0) && supersets(1)) /: supersets.drop(2)) {_ && _}
	
	def genUnion(depth : Int) : Gen[Union] = for {
		subsets <- genRecursiveList(minLength = 2) {genCharClass(depth)}
	} yield ((subsets(0) || subsets(1)) /: subsets.drop(2)) {_ || _}
	
	def genMultiChar : Gen[MultiChar] = for {
		first::second::Nil <- Gen.listOfN(2, arbitrary[Char]) if first != second
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

}