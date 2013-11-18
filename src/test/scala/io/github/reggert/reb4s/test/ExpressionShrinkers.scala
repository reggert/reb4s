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
	
	private def shrinkGroupType[T <: Group](construct : Expression => T) = Shrink {group : T =>
		for (nested <- shrink(group.nested)) yield construct(nested)
	}
	
	implicit val shrinkCapture = shrinkGroupType(Group.Capture)
	
	implicit val shrinkIndependent = shrinkGroupType(Group.Independent)
	
	implicit val shrinkNegativeLookAhead = shrinkGroupType(Group.NegativeLookAhead)
	
	implicit val shrinkNegativeLookBehind = shrinkGroupType(Group.NegativeLookBehind)
	
	implicit val shrinkNonCapturing = shrinkGroupType(Group.NonCapturing)
	
	implicit val shrinkPositiveLookAhead = shrinkGroupType(Group.PositiveLookAhead)
	
	implicit val shrinkPositiveLookBehind = shrinkGroupType(Group.PositiveLookBehind)
	
	implicit val shrinkDisableFlags : Shrink[Group.DisableFlags] = Shrink {
		case Group.DisableFlags(nested, flags) => for (n <- shrink(nested)) yield Group.DisableFlags(n, flags)
	}
	
	implicit val shrinkEnableFlags : Shrink[Group.EnableFlags] = Shrink {
		case Group.EnableFlags(nested, flags) => for (n <- shrink(nested)) yield Group.EnableFlags(n, flags)
	}
	
	implicit val shrinkGroup : Shrink[Group] = Shrink {
		case group : Group.Capture => for (g <- shrinkCapture.shrink(group)) yield g
		case group : Group.Independent => for (g <- shrinkIndependent.shrink(group)) yield g
		case group : Group.NegativeLookAhead => for (g <- shrinkNegativeLookAhead.shrink(group)) yield g
		case group : Group.NegativeLookBehind => for (g <- shrinkNegativeLookBehind.shrink(group)) yield g
		case group : Group.NonCapturing => for (g <- shrinkNonCapturing.shrink(group)) yield g
		case group : Group.PositiveLookAhead => for (g <- shrinkPositiveLookAhead.shrink(group)) yield g
		case group : Group.PositiveLookBehind => for (g <- shrinkPositiveLookBehind.shrink(group)) yield g
		case group : Group.DisableFlags => for (g <- shrinkDisableFlags.shrink(group)) yield g
		case group : Group.EnableFlags => for (g <- shrinkEnableFlags.shrink(group)) yield g
	}
	
	implicit val shrinkAnyTimes : Shrink[Quantified.AnyTimes] = Shrink {quantified =>
		for (nested <- shrink(quantified.base)) 
			yield Quantified.AnyTimes(nested, quantified.mode)
	}
	
	implicit val shrinkAtLeastOnce : Shrink[Quantified.AtLeastOnce] = Shrink {quantified =>
		for (nested <- shrink(quantified.base)) 
			yield Quantified.AtLeastOnce(nested, quantified.mode)
	}
	
	implicit val shrinkOptional : Shrink[Quantified.Optional] = Shrink {quantified =>
		for (nested <- shrink(quantified.base)) 
			yield Quantified.Optional(nested, quantified.mode)
	}
	
	implicit val shrinkRepeatExactly : Shrink[Quantified.RepeatExactly] = Shrink {quantified =>
		for (nested <- shrink(quantified.base)) 
			yield Quantified.RepeatExactly(nested, quantified.repetitions, quantified.mode)
	}
	
	implicit val shrinkRepeatRange : Shrink[Quantified.RepeatRange] = Shrink {quantified =>
		for (nested <- shrink(quantified.base)) 
			yield Quantified.RepeatRange(nested, quantified.minRepetitions, quantified.maxRepetitions, quantified.mode)
	}
	
	implicit val shrinkQuantified : Shrink[Quantified] = Shrink {
		case quantified : Quantified.AnyTimes => for (q <- shrinkAnyTimes.shrink(quantified)) yield q
		case quantified : Quantified.AtLeastOnce => for (q <- shrinkAtLeastOnce.shrink(quantified)) yield q
		case quantified : Quantified.Optional => for (q <- shrinkOptional.shrink(quantified)) yield q
		case quantified : Quantified.RepeatExactly => for (q <- shrinkRepeatExactly.shrink(quantified)) yield q
		case quantified : Quantified.RepeatRange => for (q <- shrinkRepeatRange.shrink(quantified)) yield q
	}
}