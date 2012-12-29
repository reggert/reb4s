package net.sourceforge.reb4j;

import static org.junit.Assert.assertEquals;

import java.util.regex.Pattern;

import org.junit.Test;

public class AdoptTest
{
	@Test
	public void testAdopt()
	{
		final Pattern pattern = Pattern.compile("abc");
		final Adopted expression = Adopted.adopt(pattern);
		assertEquals("abc", expression.expression().toString());
	}
}
