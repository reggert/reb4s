package net.sourceforge.reb4j;

public interface Sequenceable extends Expression
{
	@Deprecated
	Sequence then(Sequenceable right);
	
	@Deprecated
	Sequence then(Sequence right);
	
	
	/**
	 * Constructs a sequence consisting of the receiver followed by the 
	 * specified sub-expression.
	 * 
	 * @param right
	 * 	the sub-expression that must appear after the receiver in order to match;
	 * 	must not be <code>null</code>.
	 * @return a new sequence.
	 * @throws NullPointerException
	 * 	if <var>right</var> is <code>null</code>
	 */
	Sequence andThen(Sequenceable right);
	
	/**
	 * Constructs a sequence consisting of the receiver followed by the 
	 * specified sub-expression.
	 * 
	 * @param right
	 * 	the sub-expression that must appear after the receiver in order to match;
	 * 	must not be <code>null</code>.
	 * @return a new sequence.
	 * @throws NullPointerException
	 * 	if <var>right</var> is <code>null</code>
	 */
	Sequence andThen(Sequence right);
}