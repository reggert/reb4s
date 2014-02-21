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
	implicit val arbCharClass : Arbitrary[CharClass] = 
	  Arbitrary(Gen.sized{size => Gen.choose(1, size) flatMap (genCharClass)})
	implicit val arbCharRange = Arbitrary(genCharRange)
	implicit val arbIntersection = 
	  Arbitrary(Gen.sized{size => Gen.choose(2, size) flatMap (genIntersection)})
	implicit val arbUnion = 
	  Arbitrary(Gen.sized{size => Gen.choose(2, size) flatMap (genUnion)})
	implicit val arbMultiChar = 
	  Arbitrary(Gen.sized{size => Gen.choose(2, size) flatMap (genMultiChar)})
	implicit val arbSingleChar = Arbitrary(genSingleChar)
	implicit val arbPredefinedClass = Arbitrary(genPredefinedClass)
	
	
	def genCharClass(size : Int) : Gen[CharClass] =
	{
		require (size > 0, s"size=$size <= 0")
		val nonInverted = size match {
			case 1 => 
		    	Gen.oneOf(genSingleChar, genCharRange, genPredefinedClass)
			case _ =>
		    	Gen.oneOf(
		    			Gen.lzy(genMultiChar(size)),
		    			Gen.lzy(genIntersection((size))),
		    			Gen.lzy(genUnion(size))
		    		)
		}
		for {
		  cc <- nonInverted
		  ccn <- Gen.oneOf(cc, ~cc)
		} yield ccn
	}
	
	
	def genCharRange : Gen[CharRange] = for {
		first::last::Nil <- Gen.listOfN(2, arbitrary[Char])
	} yield if (first < last) CharClass.range(first, last) else CharClass.range(last, first)
	
	
	private def genCombined[CombinedType <: CharClass](size : Int)(combine : (CharClass, CharClass) => CombinedType) : Gen[CombinedType] = {
		require(size >= 2, s"size=$size < 2")
		val sizesGen = size match
		{
			case 2 => Gen.const(1::1::Nil)
			case _ => genSizes(size) filter {_.length >= 2}
		}
		for {
			sizes <- sizesGen
			subtreeGens = for {s <- sizes} yield genCharClass(s) 
			subtreesGen = (Gen.const(Nil : List[CharClass]) /: subtreeGens) {(ssGen, sGen) => 
				for {
					ss <- ssGen
					s <- sGen
				} yield s::ss
			}
			subtrees <- subtreesGen
		} yield (combine(subtrees(0), subtrees(1)) /: subtrees.drop(2)) (combine)
	}
	
	
	def genIntersection(size : Int) : Gen[Intersection] = genCombined(size) {_ && _}
	
	
	def genUnion(size : Int) : Gen[Union] = genCombined(size) {_ || _} 
	  
	
	def genMultiChar(size : Int) : Gen[MultiChar] = {
	  require(size > 0, s"size=$size <= 0")
	  for {chars <- Gen.listOfN(size, arbitrary[Char])} yield CharClass.chars(chars)
	}
	
	
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