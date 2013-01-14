package net.sourceforge.reb4j;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class AlternationTest
{

	@Test
	public void testAlternation()
	{
		final CharLiteral a = Literal.literal('a');
		final CharLiteral b = Literal.literal('b');
		final CharLiteral c = Literal.literal('c');
		final CharLiteral d = Literal.literal('d');
		final CharLiteral e = Literal.literal('e');
		final CharLiteral f = Literal.literal('f');
		final Alternation aOrB = a.or(b);
		final Alternation aOrBOrC = aOrB.or(c);
		final Alternation eOrF = e.or(f);
		final Alternation dOrEOrF = d.or(eOrF);
		final Alternation aOrBOrCOrDOrEOrF = aOrBOrC.or(dOrEOrF);
		
		assertThat(aOrB.toString(), is("a|b"));
		assertThat(aOrBOrC.toString(), is("a|b|c"));
		assertThat(eOrF.toString(), is("e|f"));
		assertThat(dOrEOrF.toString(), is("d|e|f"));
		assertThat(aOrBOrCOrDOrEOrF.toString(), is("a|b|c|d|e|f"));
	}

}
