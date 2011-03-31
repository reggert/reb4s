package net.sourceforge.reb4j;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Unit test case for the {@link Regex} class.
 * 
 * @author Richard W. Eggert II
 *
 */
public class RegexTest 
{

	@Test
	public void testFromPattern() 
	{
		final String REGEX = "abc";
		final Pattern pattern = Pattern.compile(REGEX);
		final Regex regex = Regex.fromPattern(pattern);
		regex.toPattern(); // verify that we don't explode
		assertThat(regex.toString(), is(equalTo(REGEX)));
	}

	@Test
	public void testLiteral() 
	{
		final Regex regex = Regex.literal("([");
		regex.toPattern(); // verify that we don't explode
		assertThat(regex.toString(), is(equalTo("(?:\\(\\[)")));
	}

	@Test
	public void testSequence() 
	{
		final Regex regexA = Regex.literal("ab");
		final Regex regexB = Regex.literal("cd");
		final Regex regexFinal = Regex.sequence(regexA, regexB);
		regexFinal.toPattern(); // verify that we don't explode
		assertThat(regexFinal.toString(), is(equalTo("(?:(?:ab)(?:cd))")));
	}

	@Test
	public void testThen() 
	{
		final Regex regexA = Regex.literal("ab");
		final Regex regexB = Regex.literal("cd");
		final Regex regexFinal = regexA.then(regexB);
		regexFinal.toPattern(); // verify that we don't explode
		assertThat(regexFinal.toString(), is(equalTo("(?:(?:ab)(?:cd))")));
	}

	@Test
	public void testOrRegexArray() 
	{
		final Regex regexA = Regex.literal("ab");
		final Regex regexB = Regex.literal("cd");
		final Regex regexFinal = Regex.or(regexA, regexB);
		regexFinal.toPattern(); // verify that we don't explode
		assertThat(regexFinal.toString(), is(equalTo("(?:(?:ab)|(?:cd))")));
	}

	@Test
	public void testOrRegex() 
	{
		final Regex regexA = Regex.literal("ab");
		final Regex regexB = Regex.literal("cd");
		final Regex regexFinal = regexA.or(regexB);
		regexFinal.toPattern(); // verify that we don't explode
		assertThat(regexFinal.toString(), is(equalTo("(?:(?:ab)|(?:cd))")));
	}

	@Test
	public void testRepeatIntInt() 
	{
		final Regex regexA = Regex.literal("ab");
		final Regex regexFinal = regexA.repeat(1, 3);
		regexFinal.toPattern(); // verify that we don't explode
		assertThat(regexFinal.toString(), is(equalTo("(?:(?:ab){1,3})")));
	}

	@Test
	public void testAtLeastInt() 
	{
		final Regex regexA = Regex.literal("ab");
		final Regex regexFinal = regexA.atLeast(5);
		regexFinal.toPattern(); // verify that we don't explode
		assertThat(regexFinal.toString(), is(equalTo("(?:(?:ab){5,})")));
	}

	@Test
	public void testOptional() 
	{
		final Regex regexA = Regex.literal("ab");
		final Regex regexFinal = regexA.optional();
		regexFinal.toPattern(); // verify that we don't explode
		assertThat(regexFinal.toString(), is(equalTo("(?:(?:ab)?)")));
	}

	@Test
	public void testStar() 
	{
		final Regex regexA = Regex.literal("ab");
		final Regex regexFinal = regexA.star();
		regexFinal.toPattern(); // verify that we don't explode
		assertThat(regexFinal.toString(), is(equalTo("(?:(?:ab)*)")));
	}

	@Test
	public void testAtLeastOnce() 
	{
		final Regex regexA = Regex.literal("ab");
		final Regex regexFinal = regexA.atLeastOnce();
		regexFinal.toPattern(); // verify that we don't explode
		assertThat(regexFinal.toString(), is(equalTo("(?:(?:ab)+)")));
	}

	@Test
	public void testGroup() 
	{
		final Regex regexA = Regex.literal("ab");
		final Regex regexFinal = regexA.group();
		regexFinal.toPattern(); // verify that we don't explode
		assertThat(regexFinal.toString(), is(equalTo("((?:ab))")));
	}
	
	@Test
	public void testEquals()
	{
		final Regex regexA = Regex.literal("ab").atLeast(20);
		final Regex regexB = Regex.literal("ab").atLeast(20);
		final Regex regexC = Regex.literal("ab").atLeast(10);
		assertThat(regexA.equals(regexB), is(true));
		assertThat(regexA.equals(regexC), is(false));
		assertThat(regexA.equals("ab"), is(false));
	}

}
