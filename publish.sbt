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

ThisBuild / developers := List(
	Developer(
		id    = "reggert",
		name  = "Richard W. Eggert II",
		email = "richard.eggert+reb4s@gmail.com",
		url   = url("https://github.com/reggert")
	)
)

ThisBuild / scmInfo := Some(
	ScmInfo(
		url("https://github.com/reggert/reb4s"),
		"scm:git:https://github.com/reggert/reb4s.git",
		"scm:git:git@github.com:reggert/reb4s.git"
	)
)

