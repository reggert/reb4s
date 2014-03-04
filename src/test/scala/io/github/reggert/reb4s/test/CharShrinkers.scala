package io.github.reggert.reb4s.test

import org.scalacheck.Shrink
import Shrink.shrinkContainer

trait CharShrinkers 
{
	import CharShrinkers.simpleEnough
	
	implicit val shrinkChar : Shrink[Char] = Shrink {
		case c if simpleEnough(c) => Stream.empty
		case _ => 'a' #:: 'Z' #:: '0' #:: '9' #:: Stream.empty
	}
	
	implicit val shrinkString: Shrink[String] = Shrink { s =>
    	shrinkContainer[List,Char].shrink(s.toList).map(_.mkString)
	}
}


object CharShrinkers extends CharShrinkers
{
	private val simpleEnough : Set[Char] = ('A' to 'Z').toSet ++ ('a' to 'z') ++ ('0' to '9')
}