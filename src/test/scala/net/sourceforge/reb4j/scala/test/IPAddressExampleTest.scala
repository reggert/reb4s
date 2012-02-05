package net.sourceforge.reb4j.scala.test
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import org.scalatest.Suite
import net.sourceforge.reb4j.scala.charclass.CharClass._
import org.scalatest.matchers.ShouldMatchers
import net.sourceforge.reb4j.scala.Literal
import net.sourceforge.reb4j.scala.Group._

@RunWith(classOf[JUnitRunner])
class IPAddressExampleTest extends Suite with ShouldMatchers
{
	val oneDigitOctet = Posix.Digit
	val twoDigitOctet = range('1', '9') + Posix.Digit
	val oneHundredsOctet = Literal('1') + (Posix.Digit repeat 2)
	val lowTwoHundredsOctet = Literal('2') + range('0', '4') + Posix.Digit
	val highTwoHundredsOctet = Literal("25") + range('0', '5')
	val octet = oneDigitOctet||twoDigitOctet||oneHundredsOctet||lowTwoHundredsOctet||highTwoHundredsOctet
	val dottedDecimalIPAddress = octet + ((Literal('.') + octet) repeat 3)
	
	def testOneDigitOctet()
	{
		val pattern = oneDigitOctet.toPattern
		for (i <- 0 to 9)
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString).matches() should be (true)}
		for (i <- 10 to 1000)
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString).matches() should be (false)}
	}
	
	def testTwoDigitOctet()
	{
		val pattern = twoDigitOctet.toPattern
		for (i <- 0 to 9)
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString).matches() should be (false)}
		for (i <- 10 to 99)
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString).matches() should be (true)}
		for (i <- 100 to 1000)
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString).matches() should be (false)}
	}
	
	def testOneHundredsOctet()
	{
		val pattern = oneHundredsOctet.toPattern
		for (i <- 0 to 99)
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString).matches() should be (false)}
		for (i <- 100 to 199)
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString).matches() should be (true)}
		for (i <- 200 to 1000)
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString).matches() should be (false)}
	}
	
	def testLowTwoHundredsOctet()
	{
		val pattern = lowTwoHundredsOctet.toPattern
		for (i <- 0 to 199)
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString).matches() should be (false)}
		for (i <- 200 to 249)
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString).matches() should be (true)}
		for (i <- 250 to 1000)
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString).matches() should be (false)}
	}
	
	def testHighTwoHundredsOctet()
	{
		val pattern = highTwoHundredsOctet.toPattern
		for (i <- 0 to 249)
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString()).matches() should be (false)}
		for (i <- 250 to 255)
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString()).matches() should be (true)}
		for (i <- 256 to 1000)
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString()).matches() should be (false)}
	}
	
	def testOctet()
	{
		val pattern = octet.toPattern
		for (i <- 0 to 255)
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString()).matches() should be (true)}
		for (i <- 256 to 1000)
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString()).matches() should be (false)}
		for (i <- 'a' to 'z')
			withClue(pattern + " matching " + i.toString + ":")
			{pattern.matcher(i.toString()).matches() should be (false)}
	}
	
	
	def testDottedDecimalIPAddress()
	{
		val pattern = dottedDecimalIPAddress.toPattern
		withClue(pattern + " matching 1.2.3.4:") 
			{pattern.matcher("1.2.3.4").matches() should be (true)}
		withClue(pattern + " matching 123.239.35.254:") 
			{pattern.matcher("123.239.35.254").matches() should be (true)}
		withClue(pattern + " matching 1.256.3.4:") 
			{pattern.matcher("1.256.3.4").matches() should be (false)}
		withClue(pattern + " matching 1.2.a.4:") 
			{pattern.matcher("1.2.a.4").matches() should be (false)}
	}
	
	
}