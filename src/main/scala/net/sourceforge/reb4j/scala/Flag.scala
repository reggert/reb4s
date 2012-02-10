package net.sourceforge.reb4j.scala

/**
 * Flag that can be passed to the {@link java.util.regex.Pattern} 
 * regular expression engine.
 */
@SerialVersionUID(1L)
final class Flag private (val c : Char) extends Serializable 
	with NotNull with Immutable
{
	override def toString = String.valueOf(c)
	override def equals (other : Any) = other match
	{
		case that : Flag => this.c == that.c
		case _ => false
	}
	override lazy val hashCode = 31 * c.hashCode
}


object Flag
{
	val CaseInsensitive = new Flag('i')
	val UnixLines = new Flag('d')
	val Multiline = new Flag('m')
	val DotAll = new Flag('s')
	val UnicodeCase = new Flag('u') 
	val Comments = new Flag('x')
	
	/**
	 * Wraps the specified expression in a group that enables the specified 
	 * matcher flags.
	 */
	def enable(nested : Expression, flags : Flag*) = 
		new Group(nested, flags.mkString("(?", "", ":"))
	
	/**
	 * Wraps the specified expression in a group that disables the specified
	 * matcher flags.
	 */
	def disable(nested : Expression, flags : Flag*) =
		new Group(nested, flags.mkString("(?-", "", ":"))
}

