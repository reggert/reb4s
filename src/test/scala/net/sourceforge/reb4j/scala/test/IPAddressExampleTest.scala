package net.sourceforge.reb4j.scala.test
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.Suite
import net.sourceforge.reb4j.scala.charclass.CharClass

@RunWith(classOf[JUnitRunner])
class IPAddressExampleTest extends Suite
{
	val oneDigitOctet = CharClass.Posix.digit
	val twoDigitOctet = CharClass.range('1', '9') + CharClass.Posix.digit
	
	
	def testOneDigitOctet()
	{
		
	}
}