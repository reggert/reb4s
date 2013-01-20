package net.sourceforge.reb4j;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.regex.Pattern;

import org.junit.Test;

public class AdoptTest
{
	@Test
	public void testAdopt()
	{
		final Pattern pattern = Pattern.compile("abc");
		final Adopted expression = Adopted.fromPattern(pattern);
		assertEquals("abc", expression.expression().toString());
	}
	
	@Test
	public void testAdoptedHashcodeEquals()
	{
		final Pattern pattern1 = Pattern.compile("abc");
		final Adopted expression1 = Adopted.fromPattern(pattern1);
		final Pattern pattern2 = expression1.toPattern();
		final Adopted expression2 = Adopted.fromPattern(pattern2);
		final Literal literal = Literal.literal("abc");
		assertThat(expression1.equals(expression1), is(true));
		assertThat(expression1.equals(expression2), is(true));
		assertThat(expression1.equals(literal), is(false));
		assertThat(expression1.hashCode(), is(expression2.hashCode()));
	}
}
