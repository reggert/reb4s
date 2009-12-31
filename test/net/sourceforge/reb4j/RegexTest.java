package net.sourceforge.reb4j;

import static org.junit.Assert.assertEquals;


import java.util.regex.Pattern;


import org.junit.Test;

public class RegexTest 
{

	@Test
	public void testFromPattern() 
	{
		final String REGEX = "abc";
		Pattern pattern = Pattern.compile(REGEX);
		Regex regex = Regex.fromPattern(pattern);
		regex.toPattern();
		assertEquals(REGEX, regex.toString());
	}

	@Test
	public void testLiteral() 
	{
		Regex regex = Regex.literal("([");
		regex.toPattern();
		assertEquals("(?:\\(\\[)", regex.toString());
	}

	@Test
	public void testSequence() 
	{
		Regex regexA = Regex.literal("ab");
		Regex regexB = Regex.literal("cd");
		Regex regexFinal = Regex.sequence(regexA, regexB);
		regexFinal.toPattern();
		assertEquals("(?:(?:ab)(?:cd))", regexFinal.toString());
	}

	@Test
	public void testThen() 
	{
		Regex regexA = Regex.literal("ab");
		Regex regexB = Regex.literal("cd");
		Regex regexFinal = regexA.then(regexB);
		regexFinal.toPattern();
		assertEquals("(?:(?:ab)(?:cd))", regexFinal.toString());
	}

	@Test
	public void testOrRegexArray() 
	{
		Regex regexA = Regex.literal("ab");
		Regex regexB = Regex.literal("cd");
		Regex regexFinal = Regex.or(regexA, regexB);
		regexFinal.toPattern();
		assertEquals("(?:(?:ab)|(?:cd))", regexFinal.toString());
	}

	@Test
	public void testOrRegex() 
	{
		Regex regexA = Regex.literal("ab");
		Regex regexB = Regex.literal("cd");
		Regex regexFinal = regexA.or(regexB);
		regexFinal.toPattern();
		assertEquals("(?:(?:ab)|(?:cd))", regexFinal.toString());
	}

	@Test
	public void testRepeatIntInt() 
	{
		Regex regexA = Regex.literal("ab");
		Regex regexFinal = regexA.repeat(1, 3);
		regexFinal.toPattern();
		assertEquals("(?:(?:ab){1,3})", regexFinal.toString());
	}

	@Test
	public void testAtLeastInt() 
	{
		Regex regexA = Regex.literal("ab");
		Regex regexFinal = regexA.atLeast(5);
		regexFinal.toPattern();
		assertEquals("(?:(?:ab){5,})", regexFinal.toString());
	}

	@Test
	public void testOptional() 
	{
		Regex regexA = Regex.literal("ab");
		Regex regexFinal = regexA.optional();
		regexFinal.toPattern();
		assertEquals("(?:(?:ab)?)", regexFinal.toString());
	}

	@Test
	public void testStar() 
	{
		Regex regexA = Regex.literal("ab");
		Regex regexFinal = regexA.star();
		regexFinal.toPattern();
		assertEquals("(?:(?:ab)*)", regexFinal.toString());
	}

	@Test
	public void testAtLeastOnce() 
	{
		Regex regexA = Regex.literal("ab");
		Regex regexFinal = regexA.atLeastOnce();
		regexFinal.toPattern();
		assertEquals("(?:(?:ab)+)", regexFinal.toString());
	}

	@Test
	public void testGroup() 
	{
		Regex regexA = Regex.literal("ab");
		Regex regexFinal = regexA.group();
		regexFinal.toPattern();
		assertEquals("((?:ab))", regexFinal.toString());
	}

}
