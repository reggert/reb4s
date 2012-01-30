package net.sourceforge.reb4j.scala.test
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.Suite
import net.sourceforge.reb4j.scala.charclass.CharClass
import org.scalatest.matchers.ShouldMatchers
import net.sourceforge.reb4j.scala.Literal
import net.sourceforge.reb4j.scala.Group

@RunWith(classOf[JUnitRunner])
class IPAddressExampleTest extends Suite with ShouldMatchers
{
	val oneDigitOctet = CharClass.Posix.digit
	val twoDigitOctet = CharClass.range('1', '9') + CharClass.Posix.digit
	val oneHundredsOctet = Literal('1') + (CharClass.Posix.digit ## 2)
	val lowTwoHundredsOctet = Literal('2') + CharClass.range('0', '4') + CharClass.Posix.digit
	val highTwoHundredsOctet = Literal("25") + CharClass.range('0', '5')
	val octet = Group(oneDigitOctet||twoDigitOctet||oneHundredsOctet||lowTwoHundredsOctet||highTwoHundredsOctet)
	val dottedDecimalIPAddress = octet + (Group(Literal('.') + octet) ## 2)
	
	def testOneDigitOctet()
	{
		val pattern = oneDigitOctet.toPattern
		for (i <- 0 to 9)
			pattern.matcher(i.toString()).matches() should be (true)
		for (i <- 10 to 1000)
			pattern.matcher(i.toString()).matches() should be (false)
	}
	
	def testTwoDigitOctet()
	{
		val pattern = twoDigitOctet.toPattern
		for (i <- 0 to 9)
			pattern.matcher(i.toString()).matches() should be (false)
		for (i <- 10 to 99)
			pattern.matcher(i.toString()).matches() should be (true)
		for (i <- 100 to 1000)
			pattern.matcher(i.toString()).matches() should be (false)
	}
	
	def testOneHundredsOctet()
	{
		val pattern = oneHundredsOctet.toPattern
		for (i <- 0 to 99)
			pattern.matcher(i.toString()).matches() should be (false)
		for (i <- 100 to 199)
			pattern.matcher(i.toString()).matches() should be (true)
		for (i <- 200 to 1000)
			pattern.matcher(i.toString()).matches() should be (false)
	}
	
	def testLowTwoHundredsOctet()
	{
		val pattern = lowTwoHundredsOctet.toPattern
		for (i <- 0 to 199)
			pattern.matcher(i.toString()).matches() should be (false)
		for (i <- 200 to 249)
			pattern.matcher(i.toString()).matches() should be (true)
		for (i <- 250 to 1000)
			pattern.matcher(i.toString()).matches() should be (false)
	}
	
	def testHighTwoHundredsOctet()
	{
		val pattern = highTwoHundredsOctet.toPattern
		for (i <- 0 to 249)
			pattern.matcher(i.toString()).matches() should be (false)
		for (i <- 250 to 255)
			pattern.matcher(i.toString()).matches() should be (true)
		for (i <- 256 to 1000)
			pattern.matcher(i.toString()).matches() should be (false)
	}
	
	def testOctet()
	{
		val pattern = octet.toPattern
		for (i <- 0 to 255)
			pattern.matcher(i.toString()).matches() should be (true)
		for (i <- 256 to 1000)
			pattern.matcher(i.toString()).matches() should be (false)
		for (i <- 'a' to 'z')
			pattern.matcher(i.toString()).matches() should be (false)
	}
	
	
	def testDottedDecimalIPAddress()
	{
		val pattern = dottedDecimalIPAddress.toPattern
		pattern.matcher("1.2.3.4").matches() should be (true)
		pattern.matcher("123.239.35.254").matches() should be (true)
		pattern.matcher("1.256.3.4").matches() should be (false)
		pattern.matcher("1.2.a.4").matches() should be (false)
	}
	
	
}