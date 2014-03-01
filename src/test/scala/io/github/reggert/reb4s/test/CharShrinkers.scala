package io.github.reggert.reb4s.test

import org.scalacheck.Shrink

trait CharShrinkers 
{
	implicit val shrinkChar : Shrink[Char] = Shrink {
		case c if c.isLetterOrDigit => Stream.empty
		case _ => 'a' #:: 'Z' #:: '0' #:: '9' #:: Stream.empty
	}
}