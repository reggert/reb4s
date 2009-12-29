package net.sourceforge.reb4j;

import static junit.framework.Assert.assertEquals;


import org.junit.Test;

public class CharClassTest
{

	@Test
	public void testCharactersCharSequenceBoolean()
	{
		CharClass charClass = CharClass.characters("abc", true);
		charClass.toPattern();
		assertEquals("[^abc]", charClass.toString());
	}


	@Test
	public void testCharactersCharSequence()
	{
		CharClass charClass = CharClass.characters("abc");
		charClass.toPattern();
		assertEquals("[abc]", charClass.toString());
	}


	@Test
	public void testNegate()
	{
		CharClass charClassA = CharClass.characters("abc");
		CharClass charClassFinal = CharClass.negate(charClassA);
		charClassFinal.toPattern();
		assertEquals("[^[abc]]", charClassFinal.toString());
	}


	@Test
	public void testUnion()
	{
		CharClass charClassA = CharClass.characters("abc");
		CharClass charClassB = CharClass.characters("def");
		CharClass charClassFinal = CharClass.union(charClassA, charClassB);
		charClassFinal.toPattern();
		assertEquals("[[abc][def]]", charClassFinal.toString());
	}


	@Test
	public void testIntersection()
	{
		CharClass charClassA = CharClass.characters("abc");
		CharClass charClassB = CharClass.characters("bcd");
		CharClass charClassFinal = CharClass.intersection(charClassA, charClassB);
		charClassFinal.toPattern();
		assertEquals("[[abc]&&[bcd]]", charClassFinal.toString());
	}


	@Test
	public void testRangeCharCharBoolean()
	{
		CharClass charClass = CharClass.range('a', 'z', true);
		charClass.toPattern();
		assertEquals("[^a-z]", charClass.toString());
	}


	@Test
	public void testRangeCharChar()
	{
		CharClass charClass = CharClass.range('a', 'z');
		charClass.toPattern();
		assertEquals("[a-z]", charClass.toString());
	}


	@Test
	public void testUnicodeBlock()
	{
		CharClass charClass = CharClass.unicodeBlock("Greek");
		charClass.toPattern();
		assertEquals("\\p{InGreek}", charClass.toString());
	}


	@Test
	public void testNotUnicodeBlock()
	{
		CharClass charClass = CharClass.notUnicodeBlock("Greek");
		charClass.toPattern();
		assertEquals("\\P{InGreek}", charClass.toString());
	}


	@Test
	public void testUnicodeCategory()
	{
		CharClass charClass = CharClass.unicodeCategory("Lu");
		charClass.toPattern();
		assertEquals("\\p{IsLu}", charClass.toString());
	}


	@Test
	public void testNotUnicodeCategory()
	{
		CharClass charClass = CharClass.notUnicodeCategory("Lu");
		charClass.toPattern();
		assertEquals("\\P{IsLu}", charClass.toString());
	}

}
