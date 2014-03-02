package io.github.reggert.reb4s.test

import io.github.reggert.reb4s.charclass.CharClass
import org.scalacheck.{Gen, Shrink}
import Shrink._
import org.scalacheck.Prop.forAll

trait CharClassProperties[E <: CharClass] {
	def symmetricNegation(g : Gen[E])(implicit s : Shrink[E]) = 
		forAll(g) {e => e.negated.negated == e}
}