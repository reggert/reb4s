package net.sourceforge.reb4j;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashSet;

import org.junit.Test;

public class StringLiteralTest
{

	@Test
	public void testHashCodeAndEquals()
	{
		final StringLiteral literal1 = Literal.literal("abc");
		final StringLiteral literal2 = Literal.literal("abc");
		final StringLiteral literal3 = Literal.literal("123");
		final StringLiteral literal4 = Literal.literal("xyz");
		final HashSet<Expression> set = new HashSet<Expression>();
		set.add(literal1);
		set.add(literal3);
		assertThat(set.size(), is(2));
		assertThat(set.contains(literal1), is(true));
		assertThat(set.contains(literal2), is(true));
		assertThat(set.contains(literal3), is(true));
		assertThat(set.contains(literal4), is(false));
		assertThat(literal1.equals(literal1), is(true));
		assertThat(literal1.equals(null), is(false));
		assertThat(literal1.equals(Entity.ANY_CHAR), is(false));
	}

}
