package net.sourceforge.reb4j.scala

@SerialVersionUID(1L)
final class Flag private (val c : Char) extends Serializable with NotNull
{
	override def toString = String.valueOf(c)
	override def equals (other : Any) = other match
	{
		case that : Flag => this.c == that.c
		case _ => false
	}
	override def hashCode() = 31 * c.hashCode()
}


object Flag
{
	val CaseInsensitive = new Flag('i')
	val UnixLines = new Flag('d')
	val Multiline = new Flag('m')
	val DotAll = new Flag('s')
	val UnicodeCase = new Flag('u') 
	val Comments = new Flag('x')
	
	def enable(nested : Expression, flags : Flag*) =
		new Group(nested, flags addString new StringBuilder("(?") append ':')
	def disable(nested : Expression, flags : Flag*) =
		new Group(nested, flags addString new StringBuilder("(?-") append ':')
}

