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
	with CharClassProperties[CharClass]
	with CharClassGenerators
{
	property("toPattern") = toPattern(arbitrary[CharClass])
	property("symmetricNegation") = symmetricNegation(arbitrary[CharClass])
}


object SingleCharProps extends Properties("SingleChar")
	with ExpressionProperties[SingleChar] 
	with CharClassProperties[SingleChar]
	with CharClassGenerators
{
	property("toPattern") = toPattern(arbitrary[SingleChar])
	property("symmetricNegation") = symmetricNegation(arbitrary[SingleChar])
}


object MultiCharProps extends Properties("MultiChar")
	with ExpressionProperties[MultiChar]
	with CharClassProperties[MultiChar]
	with CharClassGenerators 
	with CharClassShrinkers
{
	property("toPattern") = toPattern(arbitrary[MultiChar])
	property("symmetricNegation") = symmetricNegation(arbitrary[MultiChar])
}


object CharRangeProps extends Properties("CharRange")
	with ExpressionProperties[CharRange] 
	with CharClassProperties[CharRange]
	with CharClassGenerators
{
	property("toPattern") = toPattern(arbitrary[CharRange])
	property("symmetricNegation") = symmetricNegation(arbitrary[CharRange])
}


object IntersectionProps extends Properties("Intersection")
	with ExpressionProperties[Intersection]
	with CharClassProperties[Intersection]
	with CharClassGenerators
	with CharClassShrinkers
{
	property("toPattern") = toPattern(arbitrary[Intersection])
	property("symmetricNegation") = symmetricNegation(arbitrary[Intersection])
}


object UnionProps extends Properties("Union")
	with ExpressionProperties[Union] 
	with CharClassProperties[Union]
	with CharClassGenerators
	with CharClassShrinkers
{
	property("toPattern") = toPattern(arbitrary[Union])
	property("symmetricNegation") = symmetricNegation(arbitrary[Union])
}


object PredefinedClassProps extends Properties("PredefinedClass")
	with ExpressionProperties[PredefinedClass]
	with CharClassProperties[PredefinedClass]
	with CharClassGenerators
{
	property("toPattern") = toPattern(arbitrary[PredefinedClass])
	property("symmetricNegation") = symmetricNegation(arbitrary[PredefinedClass])
}
