package io.github.reggert.reb4s.test

import org.scalacheck.Properties
import io.github.reggert.reb4s.charclass.CharClass
import org.scalacheck.Arbitrary.arbitrary
import io.github.reggert.reb4s.charclass.SingleChar
import io.github.reggert.reb4s.charclass.MultiChar
import io.github.reggert.reb4s.charclass.CharRange
import io.github.reggert.reb4s.charclass.Intersection
import io.github.reggert.reb4s.charclass.Union
import io.github.reggert.reb4s.charclass.PredefinedClass

object CharClassProps extends Properties("CharClass") 
	with ExpressionProperties[CharClass] 
	with CharClassGenerators
{
	property("toPattern") = toPattern(arbitrary[CharClass])
}


object SingleCharProps extends Properties("SingleChar")
	with ExpressionProperties[SingleChar] 
	with CharClassGenerators
{
	property("toPattern") = toPattern(arbitrary[SingleChar])
}


object MultiCharProps extends Properties("MultiChar")
	with ExpressionProperties[MultiChar] 
	with CharClassGenerators 
	with CharClassShrinkers
{
	property("toPattern") = toPattern(arbitrary[MultiChar])
}


object CharRangeProps extends Properties("CharRange")
	with ExpressionProperties[CharRange] 
	with CharClassGenerators
{
	property("toPattern") = toPattern(arbitrary[CharRange])
}


object IntersectionProps extends Properties("Intersection")
	with ExpressionProperties[Intersection] 
	with CharClassGenerators
	with CharClassShrinkers
{
	property("toPattern") = toPattern(arbitrary[Intersection])
}


object UnionProps extends Properties("Union")
	with ExpressionProperties[Union] 
	with CharClassGenerators
	with CharClassShrinkers
{
	property("toPattern") = toPattern(arbitrary[Union])
}


object PredefinedClassProps extends Properties("PredefinedClass")
	with ExpressionProperties[PredefinedClass] 
	with CharClassGenerators
{
	property("toPattern") = toPattern(arbitrary[PredefinedClass])
}
