package net.sourceforge.reb4j;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.regex.Pattern;

import org.junit.Test;



/**
 * Unit test case that demonstrates the IP address example that is posted on the website.
 * 
 * @author Richard W. Eggert II
 *
 */
public class IPAddressExampleTest
{
	private final Regex oneDigitOctet = 
		CharClass.POSIX_DIGIT;
	private final Regex twoDigitOctet = 
		CharClass.range('1', '9').then(CharClass.POSIX_DIGIT);
	private final Regex oneHundredsOctet =
		Regex.literal("1").then(CharClass.POSIX_DIGIT.repeat(2));
	private final Regex lowTwoHundredsOctet =
		Regex.literal("2").then(CharClass.range('0', '4')).then(CharClass.POSIX_DIGIT);
	private final Regex highTwoHundredsOctet =
		Regex.literal("25").then(CharClass.range('0', '5'));
	private final Regex octet =
		Regex.or(
				oneDigitOctet, 
				twoDigitOctet, 
				oneHundredsOctet, 
				lowTwoHundredsOctet, 
				highTwoHundredsOctet
			);
	private final Regex dottedDecimalIPAddress =
		octet.then(Regex.literal(".")).repeat(3).then(octet);
	
	
	
	@Test
	public void testOneDigitOctet()
	{
		final Pattern pattern = oneDigitOctet.toPattern();
		for (int i = 0; i < 10; i++)
		{
			String s = Integer.toString(i);
			assertThat(pattern.matcher(s).matches(), is(true));
		}
		assertThat(pattern.matcher("10").matches(), is(false));
	}
	
	
	@Test
	public void testTwoDigitOctet()
	{
		final Pattern pattern = twoDigitOctet.toPattern();
		for (int i = 10; i < 100; i++)
		{
			String s = Integer.toString(i);
			assertThat(s, pattern.matcher(s).matches(), is(true));
		}
		assertThat("100", pattern.matcher("100").matches(), is(false));
		assertThat("9", pattern.matcher("9").matches(), is(false));
	}
	
	
	@Test
	public void testOneHundredsOctet()
	{
		final Pattern pattern = oneHundredsOctet.toPattern();
		for (int i = 100; i < 200; i++)
		{
			String s = Integer.toString(i);
			assertThat(s, pattern.matcher(s).matches(), is(true));
		}
		assertThat("200", pattern.matcher("200").matches(), is(false));
		assertThat("99", pattern.matcher("99").matches(), is(false));
	}
	
	
	@Test
	public void testLowTwoHundredsOctet()
	{
		final Pattern pattern = lowTwoHundredsOctet.toPattern();
		for (int i = 200; i < 250; i++)
		{
			String s = Integer.toString(i);
			assertThat(s, pattern.matcher(s).matches(), is(true));
		}
		assertThat("250", pattern.matcher("250").matches(), is(false));
		assertThat("199", pattern.matcher("199").matches(), is(false));
	}
	
	
	@Test
	public void testHighTwoHundredsOctet()
	{
		final Pattern pattern = highTwoHundredsOctet.toPattern();
		for (int i = 250; i < 256; i++)
		{
			String s = Integer.toString(i);
			assertThat(s, pattern.matcher(s).matches(), is(true));
		}
		assertThat("256", pattern.matcher("256").matches(), is(false));
		assertThat("249", pattern.matcher("249").matches(), is(false));
	}
	
	
	@Test
	public void testOctet()
	{
		final Pattern pattern = octet.toPattern();
		for (int i = 0; i < 256; i++)
		{
			String s = Integer.toString(i);
			assertThat(s, pattern.matcher(s).matches(), is(true));
		}
		assertThat("256", pattern.matcher("256").matches(), is(false));
		assertThat("a", pattern.matcher("a").matches(), is(false));
		assertThat("1111", pattern.matcher("1111").matches(), is(false));
	}
	
	
	
	@Test
	public void testDottedDecimalIPAddress()
	{
		final Pattern pattern =
			dottedDecimalIPAddress.toPattern();
		assertThat("1.2.3.4", pattern.matcher("1.2.3.4").matches(), is(true));
		assertThat("123.239.35.254", pattern.matcher("123.239.35.254").matches(), is(true));
		assertThat("1.256.3.4", pattern.matcher("1.256.3.4").matches(), is(false));
		assertThat("1.2.a.4", pattern.matcher("1.2.a.4").matches(), is(false));
	}
}
