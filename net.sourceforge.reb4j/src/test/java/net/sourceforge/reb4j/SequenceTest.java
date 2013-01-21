package net.sourceforge.reb4j;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashSet;

import org.junit.Test;


public class SequenceTest
{

	@Test
	public void testSequence()
	{
		final CharLiteral a = Literal.literal('a');
		final CharLiteral b = Literal.literal('b');
		final CharLiteral c = Literal.literal('c');
		final CharLiteral d = Literal.literal('d');
		final CharLiteral e = Literal.literal('e');
		final CharLiteral f = Literal.literal('f');
		final Sequence ab = a.andThen(b);
		final Sequence abc = ab.andThen(c);
		final Sequence ef = e.andThen(f);
		final Sequence def = d.andThen(ef);
		final Sequence abcdef = abc.andThen(def);
		
		assertThat(ab.toPattern().pattern(), is("ab"));
		assertThat(abc.toPattern().pattern(), is("abc"));
		assertThat(ef.toPattern().pattern(), is("ef"));
		assertThat(def.toPattern().pattern(), is("def"));
		assertThat(abcdef.toPattern().pattern(), is("abcdef"));
	}

	@Test
	public void testSequenceHashcodeEquals()
	{
		final CharLiteral a = Literal.literal('a');
		final CharLiteral b = Literal.literal('b');
		final CharLiteral c = Literal.literal('c');
		
		final Sequence ab = a.andThen(b);
		final Sequence abc1 = ab.andThen(c);
		final Sequence abc2 = Sequence.sequence(a, b, c);
		final HashSet<Expression> expressions = new HashSet<Expression>();
		expressions.add(a);
		expressions.add(b);
		expressions.add(c);
		expressions.add(abc1);
		assertThat(expressions.size(), is(4));
		assertThat(expressions.contains(abc1), is(true));
		assertThat(expressions.contains(abc2), is(true));
		assertThat(expressions.contains(ab), is(false));
		assertThat(abc1.equals(abc1), is(true));
		assertThat(abc1.equals(abc2), is(true));
		assertThat(abc1.equals(a), is(false));
		assertThat(abc1.equals(null), is(false));
	}
	
}
