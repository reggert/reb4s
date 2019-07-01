package io.github.reggert.reb4s

/**
 * Flag that can be passed to the java.util.regex.Pattern 
 * regular expression engine.
 */
@SerialVersionUID(1L)
sealed class Flag private (val c : Char) extends Serializable 
	with Immutable
{
	override final def toString = String.valueOf(c)
	
	/**
	 * Wraps the specified expression in a group that enables this flag.
	 */
	final def enable(nested : Expression) = Group.EnableFlags(nested, this)
	
	/**
	 * Wraps the specified expression in a group that disables this flag.
	 */
	final def disable(nested : Expression) = Group.DisableFlags(nested, this)
}


object Flag
{
	case object CaseInsensitive extends Flag('i')
	case object UnixLines extends Flag('d')
	case object Multiline extends Flag('m')
	case object DotAll extends Flag('s')
	case object UnicodeCase extends Flag('u') 
	@deprecated(message = "Use Adopted instead if you want to put comments into a pattern", since = "3.2")
	case object Comments extends Flag('x')
	
	/**
	 * Wraps the specified expression in a group that enables the specified 
	 * matcher flags.
	 */
	def enable(nested : Expression, flags : Flag*) = Group.EnableFlags(nested, flags : _*) 
	
	/**
	 * Wraps the specified expression in a group that disables the specified
	 * matcher flags.
	 */
	def disable(nested : Expression, flags : Flag*) = Group.DisableFlags(nested, flags : _*)
	
}

