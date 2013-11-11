package io.github.reggert.reb4s.test

import org.scalacheck.Shrink
import Shrink.shrink
import io.github.reggert.reb4s.Literal
import Literal.StringLiteral

trait LiteralShrinkers {
	
	implicit val shrinkStringLiteral : Shrink[StringLiteral] = Shrink {stringLiteral =>
		for {
			s <- shrink(stringLiteral.unescaped)
		} yield StringLiteral(s)
	}
	
	implicit val shrinkLiteral : Shrink[Literal] = Shrink {
		case stringLiteral : StringLiteral => shrink(stringLiteral)
		case _ => Stream.empty
	}
}


object LiteralShrinkers extends LiteralShrinkers