name := "net.sourceforge.reb4j.scala"

scalaVersion := "2.10.2"

scalacOptions += "-unchecked"

scalacOptions += "-deprecation"

scalacOptions += "-feature"

libraryDependencies in test += "org.scalatest" %% "scalatest" % "1.9.1"

libraryDependencies in test += "junit" % "junit" % "4.11"

osgiSettings

OsgiKeys.bundleSymbolicName := name.value