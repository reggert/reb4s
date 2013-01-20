package net.sourceforge.reb4j;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashSet;

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
		
		assertThat(aOrB.toPattern().pattern(), is("a|b"));
		assertThat(aOrBOrC.toPattern().pattern(), is("a|b|c"));
		assertThat(eOrF.toPattern().pattern(), is("e|f"));
		assertThat(dOrEOrF.toPattern().pattern(), is("d|e|f"));
		assertThat(aOrBOrCOrDOrEOrF.toPattern().pattern(), is("a|b|c|d|e|f"));
	}

	@Test
	public void testAlternationHashcodeEquals()
	{
		final CharLiteral a = Literal.literal('a');
		final CharLiteral b = Literal.literal('b');
		final CharLiteral c = Literal.literal('c');
		
		final Alternation aOrB = a.or(b);
		final Alternation aOrBOrC1 = aOrB.or(c);
		final Alternation aOrBOrC2 = Alternation.alternatives(a, b, c);
		final HashSet<Expression> expressions = new HashSet<Expression>();
		expressions.add(a);
		expressions.add(b);
		expressions.add(c);
		expressions.add(aOrBOrC1);
		assertThat(expressions.size(), is(4));
		assertThat(expressions.contains(aOrBOrC1), is(true));
		assertThat(expressions.contains(aOrBOrC2), is(true));
		assertThat(expressions.contains(aOrB), is(false));
		assertThat(aOrBOrC1.equals(aOrBOrC1), is(true));
		assertThat(aOrBOrC1.equals(aOrBOrC2), is(true));
		assertThat(aOrBOrC1.equals(a), is(false));
		assertThat(aOrBOrC1.equals(null), is(false));
	}
	
}
