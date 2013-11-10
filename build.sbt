name := "reb4s"

organization := "io.github.reggert"

version := "3.0.0-SNAPSHOT"

licenses := Seq("GNU Lesser General Public License v3.0" -> url("http://www.gnu.org/licenses/lgpl-3.0.html"))

homepage := Some(url("http://reggert.github.io/reb4s"))

scalaVersion := "2.10.3"

scalacOptions += "-unchecked"

scalacOptions += "-deprecation"

scalacOptions += "-feature"

autoAPIMappings := true

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.11.0" % "test"

