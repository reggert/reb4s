package net.sourceforge.reb4j;

/**
 * Interface indicating that an expression can be used as an alternative in an alternation.
 * 
 * This interface is not intended to be implemented by clients.
 */
public interface Alternative extends Expression
{
	/**
	 * Constructs an expression matching either the receiver or the 
	 * any of the alternatives contained within the specified argument 
	 * expression.
	 */
	Alternation or(Alternation right);
	
	/**
	 * Constructs an expression matching either the receiver or the 
	 * specified argument expression.
	 */
	Alternation or(Alternative right);
}