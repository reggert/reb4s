package io.github.reggert.reb4s.test

import org.scalacheck.Shrink
import Shrink.{shrinkString => _, _}
import io.github.reggert.reb4s.Literal
import Literal.{CharLiteral, StringLiteral}

trait LiteralShrinkers extends CharShrinkers {
	
	implicit val shrinkStringLiteral : Shrink[StringLiteral] = Shrink {stringLiteral =>
		for {s <- shrink(stringLiteral.unescaped)} yield StringLiteral(s)
	}
	
	implicit val shrinkCharLiteral : Shrink[CharLiteral] = Shrink {charLiteral =>
		shrink(charLiteral.unescapedChar) map {Literal.apply}
	}
	
	implicit val shrinkLiteral : Shrink[Literal] = Shrink {
		case stringLiteral : StringLiteral => shrink(stringLiteral)
		case charLiteral : CharLiteral => shrink(charLiteral)
	}
}


object LiteralShrinkers extends LiteralShrinkers