package io.github.reggert.reb4s.prefab

import io.github.reggert.reb4s.Group.Capture
import io.github.reggert.reb4s.Implicits.{char2Literal, string2Literal}
import io.github.reggert.reb4s.charclass.CharClass.{Perl, range}


object DottedDecimalIPv4Address {
	val oneDigitOctet = Perl.Digit
	val twoDigitOctet = range('1', '9') ~~ Perl.Digit
	val oneHundredsOctet = '1' ~~ (Perl.Digit repeat 2)
	val lowTwoHundredsOctet = '2' ~~ range('0', '4') ~~ Perl.Digit
	val highTwoHundredsOctet = "25" ~~ range('0', '5')
	val octet = oneDigitOctet||twoDigitOctet||oneHundredsOctet||lowTwoHundredsOctet||highTwoHundredsOctet
	val dottedDecimalIPAddress = 
		Capture(octet) ~~ '.' ~~ 
		Capture(octet) ~~ '.' ~~ 
		Capture(octet) ~~ '.' ~~ 
		Capture(octet)
	val DottedDecimalIPAddress = dottedDecimalIPAddress.toRegex()
}