package io.github.reggert.reb4s.test

import org.scalacheck.Shrink
import Shrink.shrink
import io.github.reggert.reb4s.Expression
import io.github.reggert.reb4s.Quantifiable
import io.github.reggert.reb4s.Literal
import io.github.reggert.reb4s.Raw
import io.github.reggert.reb4s.charclass.CharClass
import io.github.reggert.reb4s.Alternation
import io.github.reggert.reb4s.Quantified
import io.github.reggert.reb4s.Sequence
import io.github.reggert.reb4s.Group

trait ExpressionShrinkers extends LiteralShrinkers with RawShrinkers {

	implicit val shrinkExpression : Shrink[Expression] = Shrink {
		case literal : Literal => shrink(literal) 
		case raw : Raw => shrink(raw)
		case charclass : CharClass => shrink(charclass)
		case alternation : Alternation => shrink(alternation)
		case group : Group => shrink(group)
		case quantified : Quantified => shrink(quantified)
		case sequence : Sequence => shrink(sequence)
	}
	
	implicit val shrinkAlternation : Shrink[Alternation] = Shrink {alternation =>
		for {
			alternatives <- shrink(alternation.alternatives)
			if (alternatives.length >= 2)
		} yield ((alternatives.head || alternatives.tail.head) /: (alternatives.tail.tail)) {_ || _}
	}
	
	implicit val shrinkSequence : Shrink[Sequence] = Shrink {sequence =>
		for {
			components <- shrink(sequence.components)
			if (components.length >= 2)
		} yield ((components.head ~~ components.tail.head) /: (components.tail.tail)) {_ ~~ _}
	}
	
	// TODO: implement this!
	implicit val shrinkGroup : Shrink[Group] = ??? 
	
	// TODO: implement this!
	implicit val shrinkQuantified : Shrink[Quantified] = ???
}