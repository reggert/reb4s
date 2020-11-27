name := "reb4s"

organization := "io.github.reggert"

licenses := Seq("GNU Lesser General Public License v3.0" -> url("http://www.gnu.org/licenses/lgpl-3.0.html"))

homepage := Some(url("http://reggert.github.io/reb4s"))

scalaVersion := "2.12.12"

crossScalaVersions := Seq("2.10.7", "2.11.12", "2.12.12", "2.13.4")

scalacOptions += "-unchecked"

scalacOptions += "-deprecation"

scalacOptions += "-feature"

autoAPIMappings := true

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"

testOptions in Test += Tests.Argument(TestFrameworks.ScalaCheck, "-s", "200000")
