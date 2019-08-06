name := "scalaio-speakers"

version := "1.0"

scalaVersion := "2.12.3"

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "com.danielasfregola" %% "twitter4s" % "5.3",
  "ch.qos.logback" % "logback-classic" % "1.1.9",
	"com.softwaremill.sttp" %% "core" % "1.6.4"
)

val circeVersion = "0.11.1"
libraryDependencies ++= Seq(
	"io.circe" %% "circe-core",
	"io.circe" %% "circe-generic",
	"io.circe" %% "circe-parser"
).map(_ % circeVersion)
