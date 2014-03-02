package io.github.reggert.reb4s.test

import java.util.regex.PatternSyntaxException

import org.scalacheck.{Gen, Shrink}
import Shrink._
import org.scalacheck.Prop.forAll

import io.github.reggert.reb4s.Expression

trait ExpressionProperties[E <: Expression] {
	def toPattern(g : Gen[E])(implicit s: Shrink[E]) = 
		forAll(g) {e : E => e.toPattern; true}
}