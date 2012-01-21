package net.sourceforge.reb4j;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * Unit test case for the {@link CharClass} class.
 * 
 * @author Richard W. Eggert II
 *
 */
public class CharClassTest
{

	@Test
	public void testCharactersCharSequenceBoolean()
	{
		final CharClass charClass = CharClass.characters("abc", true);
		charClass.toPattern(); // verify that we don't explode
		assertThat(charClass.toString(), is(equalTo("[^abc]")));
	}


	@Test
	public void testCharactersCharSequence()
	{
		final CharClass charClass = CharClass.characters("abc");
		charClass.toPattern(); // verify that we don't explode
		assertThat(charClass.toString(), is(equalTo("[abc]")));
	}


	@Test
	public void testNegate()
	{
		final CharClass charClassA = CharClass.characters("abc");
		final CharClass charClassFinal = CharClass.negate(charClassA);
		charClassFinal.toPattern(); // verify that we don't explode
		assertThat(charClassFinal.toString(), is(equalTo("[^[abc]]")));
	}


	@Test
	public void testUnion()
	{
		final CharClass charClassA = CharClass.characters("abc");
		final CharClass charClassB = CharClass.characters("def");
		final CharClass charClassFinal = CharClass.union(charClassA, charClassB);
		charClassFinal.toPattern(); // verify that we don't explode
		assertThat(charClassFinal.toString(), is(equalTo("[[abc][def]]")));
	}


	@Test
	public void testIntersection()
	{
		final CharClass charClassA = CharClass.characters("abc");
		final CharClass charClassB = CharClass.characters("bcd");
		final CharClass charClassFinal = CharClass.intersection(charClassA, charClassB);
		charClassFinal.toPattern(); // verify that we don't explode
		assertThat(charClassFinal.toString(), is(equalTo("[[abc]&&[bcd]]")));
	}


	@Test
	public void testRangeCharCharBoolean()
	{
		final CharClass charClass = CharClass.range('a', 'z', true);
		charClass.toPattern(); // verify that we don't explode
		assertThat(charClass.toString(), is(equalTo("[^a-z]")));
	}


	@Test
	public void testRangeCharChar()
	{
		final CharClass charClass = CharClass.range('a', 'z');
		charClass.toPattern(); // verify that we don't explode
		assertThat(charClass.toString(), is(equalTo("[a-z]")));
	}


	@Test
	public void testUnicodeBlock()
	{
		final CharClass charClass = CharClass.unicodeBlock("Greek");
		charClass.toPattern(); // verify that we don't explode
		assertThat(charClass.toString(), is(equalTo("\\p{InGreek}")));
	}


	@Test
	public void testNotUnicodeBlock()
	{
		final CharClass charClass = CharClass.notUnicodeBlock("Greek");
		charClass.toPattern(); // verify that we don't explode
		assertThat(charClass.toString(), is(equalTo("\\P{InGreek}")));
	}


	@Test
	public void testUnicodeCategory()
	{
		final CharClass charClass = CharClass.unicodeCategory("Lu");
		charClass.toPattern(); // verify that we don't explode
		assertThat(charClass.toString(), is(equalTo("\\p{IsLu}")));
	}


	@Test
	public void testNotUnicodeCategory()
	{
		final CharClass charClass = CharClass.notUnicodeCategory("Lu");
		charClass.toPattern(); // verify that we don't explode
		assertThat(charClass.toString(), is(equalTo("\\P{IsLu}")));
	}

}
