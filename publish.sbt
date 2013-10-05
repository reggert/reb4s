publishMavenStyle := true

publishTo := {
	val nexus = "https://oss.sonatype.org/"
	if (version.value.trim.endsWith("SNAPSHOT"))
		Some("snapshots" at nexus + "content/repositories/snapshots")
	else
		Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra in ThisBuild := (
  <organization>
		<name>Richard W. Eggert II</name>
		<url>https://github.com/reggert</url>
	</organization>
	<issueManagement>
		<system>GitHub</system>
		<url>https://github.com/reggert/reb4s/issues</url>
	</issueManagement>
	<developers>
		<developer>
			<id>reggert</id>
			<name>Richard W. Eggert II</name>
			<email>reggert@users.sourceforge.net</email>
			<roles>
				<role>lead</role>
				<role>developer</role>
				<role>administrator</role>
				<role>release-manager</role>
				<role>imperator</role>
			</roles>
			<timezone>-5</timezone>
		</developer>
	</developers>
	<scm>
		<connection>
			scm:git:git://github.com/reggert/reb4s.git
		</connection>
		<developerConnection>
			scm:git:git@github.com:reggert/reb4s.git
		</developerConnection>
		<url>https://github.com/reggert/reb4s</url>
	</scm>
)

