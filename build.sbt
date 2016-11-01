name := """log-aggregation"""

version := "1.0"

scalaVersion := "2.11.7"

// Change this to another test framework if you prefer
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

mainClass in assembly := Some("kykl.log.UserTimeSpentAverageMain")
