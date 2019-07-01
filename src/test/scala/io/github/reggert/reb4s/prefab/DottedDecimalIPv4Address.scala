package io.github.reggert.reb4s.prefab

import java.net.{Inet4Address, InetAddress}

import io.github.reggert.reb4s.Group.Capture
import io.github.reggert.reb4s.Implicits.{char2Literal, string2Literal}
import io.github.reggert.reb4s.{Alternation, Sequence}
import io.github.reggert.reb4s.charclass.CharClass.{Perl, range}
import io.github.reggert.reb4s.charclass.PredefinedClass

import scala.util.matching.Regex


/**
	* Example demonstrating how to construct a pattern to match a dotted-decimal IP address and extract the octets.
	*/
object DottedDecimalIPv4Address {
	val oneDigitOctet: PredefinedClass = Perl.Digit
	val twoDigitOctet: Sequence = range('1', '9') ~~ Perl.Digit
	val threeDigitOctet: Alternation =
		'1' ~~ (Perl.Digit repeat 2) || '2' ~~ range('0', '4') ~~ Perl.Digit || "25" ~~ range('0', '5')
	val octet: Alternation = oneDigitOctet||twoDigitOctet||threeDigitOctet
	val dottedDecimalIPAddress: Sequence =
		Capture(octet) ~~ '.' ~~ 
		Capture(octet) ~~ '.' ~~ 
		Capture(octet) ~~ '.' ~~ 
		Capture(octet)
	val DottedDecimalIPAddress: Regex = dottedDecimalIPAddress.toRegex()

	/**
		* Parses the specified string as an IPv4 address.
		*
		* @param s the string to parse.
		* @return the parsed [[Inet4Address]], or `None` if the string is invalid.
		*/
	def parse(s : String) : Option[Inet4Address] =
		DottedDecimalIPAddress.unapplySeq(s).map {octets =>
			InetAddress.getByAddress(s, octets.view.map(_.toInt.toByte).toArray).asInstanceOf[Inet4Address]
		}

}