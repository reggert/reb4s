package net.sourceforge.reb4j.scala.experimental
import java.lang.Character.UnicodeBlock

trait CharClass extends Expression with Alternative with Quantifiable
{
	def ^ : CharClass
	def negate = ^
	
	/* 
	 * We could do an override def || (right : Alternative) here, but it's
	 * unlikely to be useful.
	 */
	def || (right : CharClass) = right match
	{
		case Union(rhs, negated) if (!negated) => new Union(this::rhs, false)
		case _ => new Union(List(this, right), false)
	}
	def || (right : Union) = new Union(List(this, right), false)
	def union (right : CharClass) = this || right
	def union (right : Union) = this || right
	
	private[CharClass] case class Union (
			val subclasses : List[CharClass], 
			val negated : Boolean
		) extends CharClass
	{
		override def toString = 
			(subclasses addString new StringBuilder("[") append "]").toString()
		override def ^ = new Union(subclasses, !negated)
	}
}






object CharClass
{

	private[CharClass] class PredefinedClass (val nameChar : Char) extends CharClass
	{
		protected def invertedNameChar = 
			if (nameChar.isUpperCase) 
				nameChar.toLowerCase
			else
				nameChar.toUpperCase
		override def ^ = new PredefinedClass(invertedNameChar)
		override def toString = "\\" + nameChar
	}

	private[CharClass] class ParameterizedPredefinedClass (
			nameChar : Char, 
			val className : String
		) extends PredefinedClass(nameChar)
	{
		override def ^ = new ParameterizedPredefinedClass(invertedNameChar, className)
		override def toString = super.toString() + "{" + className + "}"
	}

	
	def unicodeBlock(unicodeBlock : UnicodeBlock) = 
		new ParameterizedPredefinedClass('p', "In" + unicodeBlock.toString())

	
	// TODO: Unicode categories
	/*
	 * "C",
						"Cc",
						"Cf",
						"Cn",
						"Co", 
						"Cs",
						"L",
						"Ll", 
						"Lm",
						"Lo", 
						"Lt",
						"Lu",
						"M",
						"Mc",
						"Me",
						"Mn",
						"N",
						"Nd",
						"Nl",
						"No",
						"P",
						"Pc",
						"Pd",
						"Pe",
						"Pf",
						"Pi",
						"Po",
						"Ps",
						"S",
						"Sc",
						"Sk",
						"Sm",
						"So",
						"Z",
						"Zl",
						"Zp",
						"Zs"
	 */

	val perlDigit = new PredefinedClass('d')
	val perlSpace = new PredefinedClass('s')
	val perlWord = new PredefinedClass('w')
	val posixLower = new ParameterizedPredefinedClass('p', "Lower")
	val posixUpper = new ParameterizedPredefinedClass('p', "Upper")
	val posixAlpha = new ParameterizedPredefinedClass('p', "Alpha")
	val posixDigit = new ParameterizedPredefinedClass('p', "Digit")
	val posixAlnum = new ParameterizedPredefinedClass('p', "Alnum")
	val posixPunct = new ParameterizedPredefinedClass('p', "Punct")
	val posixGraph = new ParameterizedPredefinedClass('p', "Graph")
	val posixPrint = new ParameterizedPredefinedClass('p', "Print")
	val posixBlank = new ParameterizedPredefinedClass('p', "Blank")
	val posixControl = new ParameterizedPredefinedClass('p', "Cntrl")
	val posixHexDigit = new ParameterizedPredefinedClass('p', "XDigit")
	val posixSpace = new ParameterizedPredefinedClass('p', "Space")
	val lowerCase = new ParameterizedPredefinedClass('p', "javaLowerCase")
	val upperCase = new ParameterizedPredefinedClass('p', "javaUpperCase")
	val whitespace = new ParameterizedPredefinedClass('p', "javaWhitespace")
	val mirrored = new ParameterizedPredefinedClass('p', "javaMirrored")

}

