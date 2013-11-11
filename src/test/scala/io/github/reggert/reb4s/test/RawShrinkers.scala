package io.github.reggert.reb4s.test

import org.scalacheck.Shrink
import Shrink.shrink
import io.github.reggert.reb4s.EscapedLiteral
import io.github.reggert.reb4s.CompoundRaw
import io.github.reggert.reb4s.Raw

trait RawShrinkers extends LiteralShrinkers {
	implicit val shrinkEscapedLiteral : Shrink[EscapedLiteral] = Shrink {escapedLiteral =>
		for {
			literal <- shrink(escapedLiteral.literal)
		} yield EscapedLiteral(literal)
	}
	
	implicit val shrinkCompoundRaw : Shrink[CompoundRaw] = Shrink {compoundRaw =>
		for {
			components <- shrink(compoundRaw.components)
			if components.length >= 2
		} yield CompoundRaw(components)
	}
	
	implicit val shrinkRaw : Shrink[Raw] = Shrink {
		case escapedLiteral : EscapedLiteral => shrink(escapedLiteral)
		case compoundRaw : CompoundRaw => shrink(compoundRaw)
		case _ => Stream.empty
	}
}


object RawShrinkers extends RawShrinkers