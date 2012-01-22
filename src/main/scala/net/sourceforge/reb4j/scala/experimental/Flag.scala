package net.sourceforge.reb4j.scala.experimental

final class Flag private (val c : Char)
{
	override def toString = String.valueOf(c)
}


object Flag
{
	val CASE_INSENSITIVE = new Flag('i')
	val UNIX_LINES = new Flag('d')
	val MULTILINE = new Flag('m')
	val DOTALL = new Flag('s')
	val UNICODE_CASE = new Flag('u') 
	val COMMENTS = new Flag('x')
	
	def enable(nested : Expression, flags : Flag*) =
		new Group(nested, flags addString new StringBuilder("(?") append ':')
	def disable(nested : Expression, flags : Flag*) =
		new Group(nested, flags addString new StringBuilder("(?-") append ':')
}

