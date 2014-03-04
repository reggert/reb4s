package io.github.reggert.reb4s.test

import org.scalacheck.Shrink
import Shrink.shrink
import io.github.reggert.reb4s._
import io.github.reggert.reb4s.charclass._
import Literal.CharLiteral

trait ExpressionShrinkers extends LiteralShrinkers with RawShrinkers with CharClassShrinkers {

	implicit val shrinkExpression : Shrink[Expression] = Shrink {
		case literal : Literal => shrink(literal) 
		case raw : Raw => shrink(raw)
		case charclass : SingleChar => Literal(charclass.char) +: shrink(charclass)
		case charclass : CharClass => shrink(charclass)
		case alternation : Alternation => alternation.alternatives ++: shrink(alternation)
		case group : Group => group.nested +: shrink(group)
		case quantified : Quantified => quantified.base +: shrink(quantified)
		case sequence : Sequence => sequence.components ++: shrink(sequence)
		case adopted : Adopted => shrink(adopted)
	}
	
	implicit val shrinkAlternative : Shrink[Alternative] = Shrink {
		case literal : Literal => shrink(literal) 
		case raw : Raw => shrink(raw)
		case charclass : SingleChar => Literal(charclass.char) +: shrink(charclass)
		case charclass : CharClass => shrink(charclass)
		case alternation : Alternation => alternation.alternatives ++: shrink(alternation)
		case group : Group =>
			(Some(group.nested) collect {case alt : Alternative => alt}) ++: shrink(group)
		case quantified : Quantified =>
			(Some(quantified.base) collect {case alt : Alternative => alt}) ++: shrink(quantified)
		case sequence : Sequence => 
			(sequence.components collect {case alt : Alternative => alt}) ++: shrink(sequence)
	}
	
	implicit val shrinkSequenceable : Shrink[Sequenceable] = Shrink {
		case literal : Literal => shrink(literal) 
		case raw : Raw => shrink(raw)
		case charclass : SingleChar => Literal(charclass.char) +: shrink(charclass) 
		case charclass : CharClass => shrink(charclass)
		case group : Group =>
			(Some(group.nested) collect {case seq : Sequenceable => seq}) ++: shrink(group)
		case quantified : Quantified =>
			(Some(quantified.base) collect {case seq : Sequenceable => seq}) ++: shrink(quantified)
		case sequence : Sequence => sequence.components ++: shrink(sequence)
	}
	
	implicit val shrinkAlternation : Shrink[Alternation] = Shrink {alternation =>
		for {
			alternatives <- shrink(alternation.alternatives)
			if (alternatives.length >= 2)
		} yield ((alternatives(0) || alternatives(1)) /: (alternatives drop 2)) {_ || _}
	}
	
	implicit val shrinkSequence : Shrink[Sequence] = Shrink {sequence =>
		for {
			components <- shrink(sequence.components)
			if (components.length >= 2)
		} yield ((components(0) ~~ components(1)) /: (components drop 2)) {_ ~~ _}
	}
	
	private def shrinkGroupType[T <: Group](construct : Expression => T) = Shrink {group : T =>
		for {
			nested <- shrink(group.nested) 
			shrunk <- try {Some(construct(nested))} catch {case _ : UnboundedLookBehindException => None}
		} yield shrunk
	}
	
	implicit val shrinkCapture = shrinkGroupType(Group.Capture)
	
	implicit val shrinkIndependent = shrinkGroupType(Group.Independent)
	
	implicit val shrinkNegativeLookAhead = shrinkGroupType(Group.NegativeLookAhead)
	
	implicit val shrinkNegativeLookBehind = shrinkGroupType(Group.NegativeLookBehind)
	
	implicit val shrinkNonCapturing = shrinkGroupType(Group.NonCapturing)
	
	implicit val shrinkPositiveLookAhead = shrinkGroupType(Group.PositiveLookAhead)
	
	implicit val shrinkPositiveLookBehind = shrinkGroupType(Group.PositiveLookBehind)
	
	implicit val shrinkDisableFlags : Shrink[Group.DisableFlags] = Shrink {
		case Group.DisableFlags(nested, flags @ _*) => 
			(for (f <- shrink(flags)) yield Group.DisableFlags(nested, f : _*)) ++:
			(for (n <- shrink(nested)) yield Group.DisableFlags(n, flags : _*))
	}
	
	implicit val shrinkEnableFlags : Shrink[Group.EnableFlags] = Shrink {
		case Group.EnableFlags(nested, flags @ _*) =>
			(for (f <- shrink(flags)) yield Group.EnableFlags(nested, f : _*)) ++:
			(for (n <- shrink(nested)) yield Group.EnableFlags(n, flags : _*))
	}
	
	implicit val shrinkGroup : Shrink[Group] = Shrink {
		case group : Group.Capture => for (g <- shrink(group)) yield g
		case group : Group.Independent => Group.Capture(group.nested) +: (for (g <- shrink(group)) yield g)
		case group : Group.NegativeLookAhead => Group.Capture(group.nested) +: (for (g <- shrink(group)) yield g)
		case group : Group.NegativeLookBehind => Group.Capture(group.nested) +: (for (g <- shrink(group)) yield g)
		case group : Group.NonCapturing => Group.Capture(group.nested) +: (for (g <- shrink(group)) yield g)
		case group : Group.PositiveLookAhead => Group.Capture(group.nested) +: (for (g <- shrink(group)) yield g)
		case group : Group.PositiveLookBehind => Group.Capture(group.nested) +: (for (g <- shrink(group)) yield g)
		case group : Group.DisableFlags => Group.Capture(group.nested) +: (for (g <- shrink(group)) yield g)
		case group : Group.EnableFlags => Group.Capture(group.nested) +: (for (g <- shrink(group)) yield g)
	}
	
	implicit val shrinkQuantifiable : Shrink[Quantifiable] = Shrink {
		case literal : CharLiteral => shrink(literal)
		case raw : Raw => shrink(raw)
		case charclass : CharClass => shrink(charclass)
		case group : Group => 
			(Some(group.nested) collect {case q : Quantifiable => q}) ++: shrink(group)
		case quantified : Quantified => quantified.base +: shrink(quantified)
	}
	
	implicit val shrinkAnyTimes : Shrink[Quantified.AnyTimes] = Shrink {quantified =>
		(Some(quantified) filter {_.mode != Quantified.Greedy} map {_.copy(mode = Quantified.Greedy)}) ++:
		(for (base <- shrink(quantified.base)) yield quantified.copy(base = base))
	}
	
	implicit val shrinkAtLeastOnce : Shrink[Quantified.AtLeastOnce] = Shrink {quantified =>
		(Some(quantified) filter {_.mode != Quantified.Greedy} map {_.copy(mode = Quantified.Greedy)}) ++:
		(for (base <- shrink(quantified.base)) yield quantified.copy(base = base))
	}
	
	implicit val shrinkOptional : Shrink[Quantified.Optional] = Shrink {quantified =>
		(Some(quantified) filter {_.mode != Quantified.Greedy} map {_.copy(mode = Quantified.Greedy)}) ++:
		(for (base <- shrink(quantified.base)) yield quantified.copy(base = base))
	}
	
	implicit val shrinkRepeatExactly : Shrink[Quantified.RepeatExactly] = Shrink {quantified =>
		(Some(quantified) filter {_.mode != Quantified.Greedy} map {_.copy(mode = Quantified.Greedy)}) ++:
		(for (repetitions <- shrink(quantified.repetitions) if repetitions > 0) yield quantified.copy(repetitions = repetitions)) ++:
		(for (base <- shrink(quantified.base)) yield quantified.copy(base = base))
	}
	
	implicit val shrinkRepeatRange : Shrink[Quantified.RepeatRange] = Shrink {quantified =>
		(Some(quantified) filter {_.mode != Quantified.Greedy} map {_.copy(mode = Quantified.Greedy)}) ++:
		(for {
			minRepetitions <- shrink(quantified.minRepetitions) 
			if minRepetitions >= 0
			if quantified.maxRepetitions.isEmpty || minRepetitions < quantified.maxRepetitions.get
		} yield quantified.copy(minRepetitions = minRepetitions)) ++:
		(for {
			maxRepetitions <- shrink(quantified.maxRepetitions) 
			if maxRepetitions.isDefined
			if maxRepetitions.get > quantified.minRepetitions
		} yield quantified.copy(maxRepetitions = maxRepetitions)) ++:
		(for (base <- shrink(quantified.base)) yield quantified.copy(base = base))
	}
	
	implicit val shrinkQuantified : Shrink[Quantified] = Shrink {
		case quantified : Quantified.AnyTimes => quantified.base.repeat(1) +: (for (q <- shrink(quantified)) yield q)
		case quantified : Quantified.AtLeastOnce => quantified.base.repeat(1) +: (for (q <- shrink(quantified)) yield q)
		case quantified : Quantified.Optional => quantified.base.repeat(1) +: (for (q <- shrink(quantified)) yield q)
		case quantified : Quantified.RepeatExactly => for (q <- shrink(quantified)) yield q
		case quantified : Quantified.RepeatRange => quantified.base.repeat(1) +: (for (q <- shrink(quantified)) yield q)
	}
}

object ExpressionShrinkers extends ExpressionShrinkers
