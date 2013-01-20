package net.sourceforge.reb4j;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.reb4j.charclass.CharClass;
import net.sourceforge.reb4j.charclass.CharClass.Perl;

import org.junit.Test;

import fj.Effect;
import fj.F;
import fj.F2;
import fj.Show;
import fj.data.LazyString;
import fj.data.List;

public class IPAddressExampleTest
{
	public final Alternative oneDigitOctet = 
		Perl.DIGIT;
	public final Alternative twoDigitOctet = 
		CharClass.range('1', '9').andThen(Perl.DIGIT);
	public final Alternative oneHundredsOctet = 
		Literal.literal('1').andThen(Perl.DIGIT.repeat(2));
	public final Alternative lowTwoHundredsOctet = Sequence.sequence(
			Literal.literal('2'),
			CharClass.range('0', '4'),
			Perl.DIGIT
		);
	public final Alternative highTwoHundredsOctet = 
		Literal.literal("25").andThen(CharClass.range('0', '5'));
	public final Alternation octet = Alternation.alternatives(
			oneDigitOctet, 
			twoDigitOctet, 
			oneHundredsOctet, 
			lowTwoHundredsOctet,
			highTwoHundredsOctet
		);
	public final CharLiteral dot = Literal.literal('.');
	public final Sequence dottedDecimalIPAddress = Sequence.sequence(
			Group.capture(octet), 
			dot, 
			Group.capture(octet), 
			dot, 
			Group.capture(octet), 
			dot, 
			Group.capture(octet)
		);

	
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
	
	@Test
	public void testOctetValid()
	{
		validateAgainst(
				"octet", 
				octet.toPattern(), 
				List.range(0, 255).map(Show.intShow.showS_())
			);
	}
	
	@Test
	public void testOctetInvalid()
	{
		invalidateAgainst(
				"octet",
				octet.toPattern(),
				List.list("020", "0250", "a", "256", "249 ")
			);
	}
	
	@Test
	public void testDottedDecimalIPAddressValid()
	{
		final List<LazyString> inputOctets = 
			List.list(0, 1, 9, 10, 59, 100, 123, 210, 251)
			.map(new F<Integer, LazyString>()
				{
					@Override
					public LazyString f(final Integer a)
					{return LazyString.str(a.toString());}
				});
		final F2<LazyString, LazyString, LazyString> concatOctets =
			new F2<LazyString, LazyString, LazyString>()
			{
				@Override
				public LazyString f(final LazyString a, final LazyString b)
				{return a.append(".").append(b);}
				
			};
		final List<String> inputs = inputOctets
				.zipWith(inputOctets, concatOctets)
				.zipWith(inputOctets, concatOctets)
				.zipWith(inputOctets, concatOctets)
				.map(Show.<LazyString>anyShow().showS_());
		validateAgainst(
				"dottedDecimalIPAddress", 
				dottedDecimalIPAddress.toPattern(), 
				inputs
			);
	}
	
	@Test
	public void testDottedDecimalIPAddressInvalid()
	{
		invalidateAgainst(
				"dottedDecimalIPAddress",
				dottedDecimalIPAddress.toPattern(),
				List.list("1", "1.2", "1.2.3", "1.2.3.300", "1.2.3.4 ")
			);
	}
	
	@Test
	public void testCapture()
	{
		//System.out.println(dottedDecimalIPAddress);
		final Pattern pattern = dottedDecimalIPAddress.toPattern();
		final Matcher matcher = pattern.matcher("5.99.123.251");
		assertThat(matcher.matches(), is(true));
		assertThat(matcher.group(1), is("5"));
		assertThat(matcher.group(2), is("99"));
		assertThat(matcher.group(3), is("123"));
		assertThat(matcher.group(4), is("251"));
	}

}
