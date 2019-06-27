[![Build Status](https://travis-ci.org/reggert/reb4s.png)](https://travis-ci.org/reggert/reb4s)

# About reb4s

The purpose of **reb4s** is to provide a pure Scala wrapper around
the regular expression syntax provided by the JRE's 
[java.util.regex.Pattern](http://java.sun.com/javase/6/docs/api/java/util/regex/Pattern.html) class.
It is an offshoot of the [**reb4j** project](https://github.com/reggert/reb4j), which provides the same 
functionality with a pure Java API. 

**reb4s** provides the following benefits over writing regular expressions directly:

*	The **reb4s** API guarantees proper expression syntax.
	If the Scala code compiles, the regular expression will compile at runtime.  
	In other words, it is not necessary to deal with [PatternSyntaxException](http://java.sun.com/javase/6/docs/api/java/util/regex/PatternSyntaxException.html)s.
	You will know right away if there is a syntax error in your regular expression, rather than having to wait until runtime to find out. 
*	The **reb4s** API enables composition of subexpressions.  Complex expressions can be broken into manageable pieces, each of which can be independently tested and reused.
*	Patterns built using **reb4s** are inherently self-documenting (at least, moreso than regular expression syntax).
	Since composition is supported, each subexpression can be given a meaningful name that describes what it represents.
*	Since **reb4s** hides regular expression syntax from the developer, there is no need to memorize (or repeatedly look up) what symbols represent which expression constructs, nor is it necessary to "double-escape" strings (i.e., escaped once to override how the pattern compiler interprets special characters, and escaped again to fit into Java string literals).
	

Of course, this comes at the cost of a modest performance penalty at startup as **reb4s** builds strings to pass into the pattern compiler, but the time required for this processing is dwarfed by the time spent by the compiler itself and should not be noticeable.

**reb4s** depends only upon the standard Scala library.

As a quick example, here's one way to use **reb4s** to describe a pattern that validates the format of a dotted decimal IP address (ensuring that each octet is a decimal value between 0 and 255) and extracts the octets.
	
	val oneDigitOctet = Perl.Digit
	val twoDigitOctet = range('1', '9') ~~ Perl.Digit
	val oneHundredsOctet = '1' ~~ (Perl.Digit repeat 2)
	val lowTwoHundredsOctet = '2' ~~ range('0', '4') ~~ Perl.Digit
	val highTwoHundredsOctet = "25" ~~ range('0', '5')
	val octet = oneDigitOctet||twoDigitOctet||oneHundredsOctet||lowTwoHundredsOctet||highTwoHundredsOctet
	val dottedDecimalIPAddress = 
		Capture(octet) ~~ '.' ~~ 
		Capture(octet) ~~ '.' ~~ 
		Capture(octet) ~~ '.' ~~ 
		Capture(octet)
	
	val regex = dottedDecimalIPAddress.toRegex()
	val input = "10.10.1.204"
	val octets = input match {
		case regex(first, second, third, fourth) => 
			Some(Array(first.toInt, second.toInt, third.toInt, fourth.toInt))
		case _ => None
	}
	
For reference, the generated regular expression looks like this:
	
	(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])


#Obtaining reb4s

**reb4s** is most easily obtained using a dependency manager such as **Maven** or **sbt**, though you can manually download it from [Maven Central](http://search.maven.org/#search|ga|1|g:"io.github.reggert") if you prefer.

##Using Maven

To use **reb4s** in a **Maven** project, add the following to the `dependencies` section of your `pom.xml` file:

    <dependency>
        <groupId>io.github.reggert</groupId>
        <artifactId>reb4s_2.10</artifactId>
        <version>3.1.0</version>
    </dependency> 
    
Replace "2.10" with "2.11" for projects using Scala 2.11. 

##Using sbt

To use **reb4s** in an **sbt** project, add the following to your `build.sbt` file (or other `.sbt` file appropriate to your project):

    libraryDependencies += "io.github.reggert" %% "reb4s" % "3.1.0"


#Requirements

**reb4s** has no dependencies other than the Scala standard library. However, it is currently only built against Scala 2.10. For compatibility with Scala 2.9, take a look at the legacy artifact `reb4j-scala`, version 2.0 or earlier.
