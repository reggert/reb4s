package io.github.reggert.reb4s.test.prefab

import io.github.reggert.reb4s._
import io.github.reggert.reb4s.prefab.DottedDecimalIPv4Address._
import org.scalacheck.{Gen, Properties}
import org.scalacheck.Prop.{BooleanOperators, forAll}
import org.scalacheck.Prop.propBoolean
import scala.util.Try

object DottedDecimalIPv4AddressProps extends Properties("DottedDecimalIPv4Address") {
	
	val numericString = """\d+""".r.pattern
	def isNumericString(s : String) = numericString.matcher(s).matches
	
	val genOctets = for {
		a <- Gen.choose(0, 255)
		b <- Gen.choose(0, 255)
		c <- Gen.choose(0, 255)
		d <- Gen.choose(0, 255)
	} yield (a, b, c, d)

	object IntString {
		import java.lang.Integer.parseInt
		def unapply(s : String) = Try{parseInt(s)}.toOption
	}
	
	implicit final class ExpressionProps(val expr : Expression) {
		
		def validatesNumberInRange(min : Int, max : Int) = forAll (Gen.choose(min, max)) { (n : Int) =>
			expr.toPattern.matcher(n.toString).matches
		}
		
		def invalidatesNumberOutOfRange(min : Int, max : Int) = forAll { (n : Int) =>
			(n < min || n > max) ==> (!expr.toPattern.matcher(n.toString).matches)
		}
		
		val invalidatesNonNumericString = forAll { (s : String) =>
			(!isNumericString(s)) ==> (!expr.toPattern.matcher(s).matches)
		}
		
		def matchesOnlyNumberInRange(min : Int, max : Int) =
			validatesNumberInRange(min, max) && invalidatesNumberOutOfRange(min, max) && invalidatesNonNumericString
		
		private val Extractor = expr.toRegex()
		
		val validatesAddressAndCapturesOctets = forAll (genOctets) {
			case (a, b, c, d) => s"$a.$b.$c.$d" match
			{
				case Extractor(as, bs, cs, ds) 
					if (as, bs, cs, ds) == (a.toString, b.toString, c.toString, d.toString) => true
				case _ => false
			}
		}
	
		val invalidatesAddressWithBadOctet = forAll {(a : Int, b : Int, c : Int, d : Int) =>
			(List(a, b, c, d).exists(x => x < 0 || x > 255)) ==> 
				(!expr.toPattern.matcher(s"$a.$b.$c.$d").matches)
		}
		
		val invalidatesNonAddress = forAll { s : String => s match {
			case Extractor(as, bs, cs, ds) => (as, bs, cs, ds) match {
				case _ if s != s"$as.$bs.$cs.$ds" => false
				case (IntString(a), IntString(b), IntString(c), IntString(d)) 
					if Traversable(a, b, c, d) forall {x => x >= 0 && x <= 255} => true
				case _ => false
			}
			case _ => true
		}}
			
	}
	
	
	property("oneDigitOctet") = oneDigitOctet matchesOnlyNumberInRange(0, 9)
	property("twoDigitOctet") = twoDigitOctet matchesOnlyNumberInRange(10, 99)
	property("oneHundredsOctet") = oneHundredsOctet matchesOnlyNumberInRange(100, 199)
	property("lowTwoHundredsOctet") = lowTwoHundredsOctet matchesOnlyNumberInRange(200, 249)
	property("highTwoHundredsOctet") = highTwoHundredsOctet matchesOnlyNumberInRange(250, 255)
	property("octet") = octet matchesOnlyNumberInRange(0, 255)
	property("dottedDecimalIPAddress") = 
		dottedDecimalIPAddress.validatesAddressAndCapturesOctets &&
		dottedDecimalIPAddress.invalidatesAddressWithBadOctet &&
		dottedDecimalIPAddress.invalidatesNonAddress
		
}