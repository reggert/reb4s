package io.github.reggert.reb4s.test

import org.scalacheck.Shrink
import org.scalacheck.Shrink.shrink
import io.github.reggert.reb4s.charclass.{Intersection, MultiChar, Union}
import io.github.reggert.reb4s.charclass.CharClass
import io.github.reggert.reb4s.charclass.SingleChar

trait CharClassShrinkers {
	
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
	
	implicit val shrinkCharClass : Shrink[CharClass] = Shrink {
		case multichar : MultiChar => 
			(multichar.chars map {CharClass.char}) ++: shrink(multichar)
		case singlechar : SingleChar => shrink(singlechar)
		case union : Union => union.subsets ++: shrink(union)
		case intersection : Intersection => intersection.supersets ++: shrink(intersection)
	}
}