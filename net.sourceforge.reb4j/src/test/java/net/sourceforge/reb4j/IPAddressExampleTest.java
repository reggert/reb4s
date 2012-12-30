package net.sourceforge.reb4j;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.regex.Pattern;

import net.sourceforge.reb4j.Alternation.Alternative;
import net.sourceforge.reb4j.charclass.CharClass;
import net.sourceforge.reb4j.charclass.CharClass.Perl;

import org.junit.Test;

import fj.Effect;
import fj.Show;
import fj.data.List;

public class IPAddressExampleTest
{
	public final Alternative oneDigitOctet = 
		Perl.DIGIT;
	public final Alternative twoDigitOctet = 
		CharClass.range('1', '9').then(Perl.DIGIT);
	public final Alternative oneHundredsOctet = 
		Literal.character('1').then(Perl.DIGIT.repeat(2));
	public final Alternative lowTwoHundredsOctet = 
		Literal.character('2').then(CharClass.range('0', '4')).then(Perl.DIGIT);
	public final Alternative highTwoHundredsOctet = 
		Literal.string("25").then(CharClass.range('0', '5'));
	public final Alternation octet = 
		oneDigitOctet.or(twoDigitOctet.or(lowTwoHundredsOctet.or(highTwoHundredsOctet)));
	public final Sequence dottedDecimalIPAddress = 
		Group.capture(octet).then(Group.capture(octet).then(Group.capture(octet).then(Group.capture(octet))));

	
	private void validateAgainst(final String name, final Pattern pattern, final List<String> validInputs)
	{
		validInputs.foreach(
				new Effect<String>()
				{
					@Override
					public void e(final String a)
					{
						assertThat(
								name + " does not match valid string: " + a,
								pattern.matcher(a).matches(),
								is(true)
							);
					}
				}
			);
	}
	
	private void invalidateAgainst(final String name, final Pattern pattern, final List<String> invalidInputs)
	{
		invalidInputs.foreach(
				new Effect<String>()
				{
					@Override
					public void e(final String a)
					{
						assertThat(
								name + " matches invalid string: " + a,
								pattern.matcher(a).matches(),
								is(false)
							);
					}
				}
			);
	}
	
	@Test
	public void testOneDigitOctetValid()
	{
		validateAgainst(
				"oneDigitOctet", 
				oneDigitOctet.toPattern(), 
				List.range(0, 10).map(Show.intShow.showS_())
			);
	}
	
	@Test
	public void testOneDigitOctetInvalid()
	{
		invalidateAgainst(
				"oneDigitOctet",
				oneDigitOctet.toPattern(),
				List.list("-1", "a", " 1", "11", "2z")
			);
	}
	
	@Test
	public void testTwoDigitOctetValid()
	{
		validateAgainst(
				"twoDigitOctet", 
				twoDigitOctet.toPattern(), 
				List.range(10, 100).map(Show.intShow.showS_())
			);
	}
	
	@Test
	public void testTwoDigitOctetInvalid()
	{
		invalidateAgainst(
				"twoDigitOctet",
				twoDigitOctet.toPattern(),
				List.list("00", "01", "9", "111", "1a")
			);
	}
	
	@Test
	public void testOneHundredsOctetValid()
	{
		validateAgainst(
				"oneHundredsOctet", 
				oneHundredsOctet.toPattern(), 
				List.range(100, 200).map(Show.intShow.showS_())
			);
	}
	
	@Test
	public void testOneHundredsOctetInvalid()
	{
		invalidateAgainst(
				"oneHundredsOctet",
				oneHundredsOctet.toPattern(),
				List.list("000", "011", "1000", "200", "1a1")
			);
	}
	
	@Test
	public void testLowTwoHundredsOctetValid()
	{
		validateAgainst(
				"lowTwoHundredsOctet", 
				lowTwoHundredsOctet.toPattern(), 
				List.range(200, 250).map(Show.intShow.showS_())
			);
	}
	
	@Test
	public void testLowTwoHundredsOctetInvalid()
	{
		invalidateAgainst(
				"lowTwoHundredsOctet",
				lowTwoHundredsOctet.toPattern(),
				List.list("020", "0200", "1000", "190", "251")
			);
	}
	
	@Test
	public void testHighTwoHundredsOctetValid()
	{
		validateAgainst(
				"highTwoHundredsOctet", 
				highTwoHundredsOctet.toPattern(), 
				List.range(250, 256).map(Show.intShow.showS_())
			);
	}
	
	@Test
	public void testHighTwoHundredsOctetInvalid()
	{
		invalidateAgainst(
				"highTwoHundredsOctet",
				highTwoHundredsOctet.toPattern(),
				List.list("020", "0250", "200", "256", "249")
			);
	}

}
