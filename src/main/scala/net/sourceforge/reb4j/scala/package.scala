package net.sourceforge.reb4j

package object scala 
{
	implicit def wrapInParens(e : Expression) = Group.NonCapturing(e)
}