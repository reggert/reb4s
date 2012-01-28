package net.sourceforge.reb4j.scala

private[scala] object Util 
{
	def notNull[T] (x : T) = 
	{
		require (x != null, "Argument is null")
		x
	}
}