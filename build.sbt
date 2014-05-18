name := "reb4s"

organization := "io.github.reggert"

licenses := Seq("GNU Lesser General Public License v3.0" -> url("http://www.gnu.org/licenses/lgpl-3.0.html"))

homepage := Some(url("http://reggert.github.io/reb4s"))

scalaVersion := "2.11.0"

crossScalaVersions := Seq("2.10.3", "2.11.0")

scalacOptions += "-unchecked"

scalacOptions += "-deprecation"

scalacOptions += "-feature"

autoAPIMappings := true

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.11.4" % "test"

testOptions in Test += Tests.Argument(TestFrameworks.ScalaCheck, "-s", "200000")
