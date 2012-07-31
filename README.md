#About reb4j

The purpose of **reb4j** is to provide a pure Java wrapper around
the regular expression syntax provided by the JRE's 
[java.util.Pattern](http://java.sun.com/javase/6/docs/api/java/util/regex/Pattern.html}java.util.regex.Pattern) class.

**reb4j** provides the following benefits over writing regular expressions directly:

*	The **reb4j** API guarantees proper expression syntax.
	If the Java code compiles, the regular expression will compile at runtime.  
	In other words, it is not necessary to deal with [PatternSyntaxException](http://java.sun.com/javase/6/docs/api/java/util/regex/PatternSyntaxException.html}PatternSyntaxException)s.
	You will know right away if there is a syntax error in your regular expression, rather than having to wait until runtime to find out. 
*	The **reb4j** API enables composition of subexpressions.  Complex expressions can be broken into manageable pieces, each of which can be independently tested and reused.
*	Patterns built using **reb4j** are inherently self-documenting (at least, moreso than regular expression syntax).
	Since composition is supported, each subexpression can be given a meaningful name that describes what it represents.
*	Since **reb4j** hides regular expression syntax from the developer, there is no need to memorize (or repeatedly look up) what symbols represent which expression constructs, nor is it necessary to "double-escape" strings (i.e., escaped once to override how the pattern compiler interprets special characters, and escaped again to fit into Java string literals).
	

Of course, this comes at the cost of a modest performance penalty at startup as **reb4j** builds strings to pass into the pattern compiler, but the time required for this processing is dwarfed by the time spent by the compiler itself and should not be noticeable.
	
As a quick example, here's one way to use **reb4j** to describe a pattern that validates the format of a dotted decimal IP address (ensuring that each octet is a decimal value between 0 and 255):
	
	final Regex oneDigitOctet = 
		CharClass.POSIX_DIGIT;
	final Regex twoDigitOctet = 
		CharClass.range('1', '9').then(CharClass.POSIX_DIGIT);
	final Regex oneHundredsOctet =
		Regex.literal("1").then(CharClass.POSIX_DIGIT.repeat(2));
	final Regex lowTwoHundredsOctet =
		Regex.literal("2").then(CharClass.range('0', '4')).then(CharClass.POSIX_DIGIT);
	final Regex highTwoHundredsOctet =
		Regex.literal("25").then(CharClass.range('0', '5'));
	final Regex octet =
		Regex.or(
				oneDigitOctet, 
				twoDigitOctet, 
				oneHundredsOctet, 
				lowTwoHundredsOctet, 
				highTwoHundredsOctet
			);
	final Regex dottedDecimalIPAddress =
		octet.then(Regex.literal(".")).repeat(3).then(octet);
		
	final Pattern pattern = dottedDecimalIPAddress.toPattern();
	String input = "10.10.1.204";
	boolean valid = pattern.matcher(input).matches();

For reference, the generated regular expression looks like this (note that **reb4j** makes liberal use of "non-capturing groups"):
	
	(?:(?:(?:(?:\p{Digit}|(?:[1-9]\p{Digit})|(?:(?:1)(?:\p{Digit}{2}))|(?:(?:(?:2)[0-4])\p{Digit})|(?:(?:25)[0-5]))(?:\.)){3})(?:\p{Digit}|(?:[1-9]\p{Digit})|(?:(?:1)(?:\p{Digit}{2}))|(?:(?:(?:2)[0-4])\p{Digit})|(?:(?:25)[0-5])))

	

	
	
