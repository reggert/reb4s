#About reb4j

The purpose of **reb4j** is to provide a pure Java wrapper around
the regular expression syntax provided by the JRE's 
[java.util.regex.Pattern](http://java.sun.com/javase/6/docs/api/java/util/regex/Pattern.html) class.

**reb4j** provides the following benefits over writing regular expressions directly:

*	The **reb4j** API guarantees proper expression syntax.
	If the Java code compiles, the regular expression will compile at runtime.  
	In other words, it is not necessary to deal with [PatternSyntaxException](http://java.sun.com/javase/6/docs/api/java/util/regex/PatternSyntaxException.html)s.
	You will know right away if there is a syntax error in your regular expression, rather than having to wait until runtime to find out. 
*	The **reb4j** API enables composition of subexpressions.  Complex expressions can be broken into manageable pieces, each of which can be independently tested and reused.
*	Patterns built using **reb4j** are inherently self-documenting (at least, moreso than regular expression syntax).
	Since composition is supported, each subexpression can be given a meaningful name that describes what it represents.
*	Since **reb4j** hides regular expression syntax from the developer, there is no need to memorize (or repeatedly look up) what symbols represent which expression constructs, nor is it necessary to "double-escape" strings (i.e., escaped once to override how the pattern compiler interprets special characters, and escaped again to fit into Java string literals).
	

Of course, this comes at the cost of a modest performance penalty at startup as **reb4j** builds strings to pass into the pattern compiler, but the time required for this processing is dwarfed by the time spent by the compiler itself and should not be noticeable.

**reb4j** currently provides two API's: one in Java and one in Scala.  They each function in roughly the same way.  However, the Scala API takes advantage of several language features of Scala to make its use easier and more readable when invoked from Scala code.  The Scala API depends only upon the standard Scala library.  The Java API depends upon the [Functional Java](http://functionaljava.org) library in order to achieve some performance optimizations and make use of functional idioms.

As a quick example, here's one way to use **reb4j** to describe a pattern that validates the format of a dotted decimal IP address (ensuring that each octet is a decimal value between 0 and 255) and extracts the octets.  First, using the Java API (imports omitted for clarity):
	
	final Pattern pattern = dottedDecimalIPAddress.toPattern();
	String input = "10.10.1.204";
	boolean valid = pattern.matcher(input).matches();
	
	final Alternative oneDigitOctet = 
		Perl.DIGIT;
	final Alternative twoDigitOctet = 
		CharClass.range('1', '9').then(Perl.DIGIT);
	final Alternative oneHundredsOctet = 
		Literal.literal('1').then(Perl.DIGIT.repeat(2));
	public final Alternative lowTwoHundredsOctet = Sequence.sequence(
			Literal.literal('2'),
			CharClass.range('0', '4'),
			Perl.DIGIT
		);
	public final Alternative highTwoHundredsOctet = 
		Literal.literal("25").then(CharClass.range('0', '5'));
	public final Alternation octet = Alternation.alternatives(
			oneDigitOctet, 
			twoDigitOctet, 
			oneHundredsOctet, 
			lowTwoHundredsOctet,
			highTwoHundredsOctet
		);
	public final CharLiteral dot = Literal.literal('.');
	public final Sequence dottedDecimalIPAddress = Sequence.sequence(
			Group.capture(octet), 
			dot, 
			Group.capture(octet), 
			dot, 
			Group.capture(octet), 
			dot, 
			Group.capture(octet)
		);
		
	final Pattern pattern = dottedDecimalIPAddress.toPattern();
	final String input = "1.2.3.4";
	final Matcher matcher = pattern.matcher(input);
	final int[] octets;
	if (matcher.matches())
	{
		octets = new int[4];
		for (int i = 0; i < 4; i++)
			octets[i] = Integer.parseInt(matcher.group(i+1));
		// octets = {1, 2, 3, 4}
	}
	else
		octets = null;

For reference, the generated regular expression looks like this:
	
	(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])

Here's the same example again in Scala (imports again omitted):

	val oneDigitOctet = Perl.Digit
	val twoDigitOctet = range('1', '9') ~~ Perl.Digit
	val oneHundredsOctet = '1' ~~ (Perl.Digit repeat 2)
	val lowTwoHundredsOctet = '2' ~~ range('0', '4') ~~ Perl.Digit
	val highTwoHundredsOctet = "25" ~~ range('0', '5')
	val octet = oneDigitOctet||twoDigitOctet||oneHundredsOctet||lowTwoHundredsOctet||highTwoHundredsOctet
	val dottedDecimalIPAddress = Capture(octet) ~~ '.' ~~ Capture(octet) ~~ '.' ~~ Capture(octet) ~~ '.' ~~ Capture(octet)
	
	val regex = dottedDecimalIPAddress.toRegex()
	val input = "10.10.1.204"
	val octets = input match {
		case regex(first, second, third, fourth) => 
			Some(Array(first.toInt, second.toInt, third.toInt, fourth.toInt))
		case _ => None
	}

The generated expression is identical.
	
