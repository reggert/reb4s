package io.github.reggert.reb4s.test

import org.scalacheck.Shrink
import org.scalacheck.Shrink.shrink

import io.github.reggert.reb4s.charclass._

trait CharClassShrinkers extends CharShrinkers {
	
	implicit val shrinkUnion : Shrink[Union] = Shrink {
		case union : Union => for {
			subsets <- shrink(union.subsets)
			if subsets.length >= 2
		} yield ((subsets.head || subsets.tail.head) /: subsets.tail.tail){_ || _}
	}
	
	implicit val shrinkIntersection : Shrink[Intersection] = Shrink {
		case x : Intersection => for {
			supersets <- shrink(x.supersets)
			if supersets.length >= 2
		} yield ((supersets.head && supersets.tail.head) /: supersets.tail.tail){_ && _}
	}
	
	implicit val shrinkMultiChar : Shrink[MultiChar] = Shrink {multichar =>
		for {
			chars <- shrink(multichar.chars)
			if chars.size >= 2
		} yield CharClass.chars(chars)
	}
	
	implicit val shrinkSingleChar : Shrink[SingleChar] = Shrink {singlechar =>
		shrink(singlechar.char) map {CharClass.char}
	}
	
	implicit val shrinkCharRange : Shrink[CharRange] = Shrink {charRange =>
		for {
			first <- charRange.first #:: shrink(charRange.first)
			last <- charRange.last #:: shrink(charRange.last)
			if first <= last
			if first != charRange.first || last != charRange.last
		} yield CharClass.range(first, last)
	}
	
	implicit val shrinkPredefinedClass : Shrink[PredefinedClass] = Shrink {
		case _ : NamedPredefinedClass => Stream(CharClass.Perl.Digit)
		case _ => Stream.empty
	}
	
	implicit val shrinkCharClass : Shrink[CharClass] = Shrink {
		case multichar : MultiChar => 
			(multichar.chars.toTraversable map {CharClass.char}) ++: shrink(multichar)
		case singlechar : SingleChar => shrink(singlechar)
		case union : Union => union.subsets ++: shrink(union)
		case intersection : Intersection => intersection.supersets ++: shrink(intersection)
		case range : CharRange => CharClass.char(range.first) +: CharClass.char(range.last) +: shrink(range)
		case negated : Negated[_] => negated.positive +: shrink(negated.positive)
		case predefined : PredefinedClass => CharClass.char('a') +: shrink(predefined)
	}
}

object CharClassShrinkers extends CharClassShrinkers
