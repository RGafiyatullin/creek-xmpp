name := "creek-xmpp"

version := "0.3.5"

scalaVersion in ThisBuild := "2.11.8"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
scalacOptions ++= Seq("-language:implicitConversions")
scalacOptions ++= Seq("-Ywarn-value-discard", "-Xfatal-warnings")

organization := "com.github.rgafiyatullin"

/*
publishTo := {
  val nexus = "http://am3-v-perftest-xmppcs-1.be.core.pw:8081/"
  Some("releases"  at nexus + "content/repositories/sbt-releases/")
}
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials.am3")
*/

publishTo := {
  val nexus = "http://nexus.in-docker.localhost:8081/"
  Some("releases"  at nexus + "repository/my-releases")
}
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials.local")

lazy val commonSettings = Seq()

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6",
  "com.github.rgafiyatullin" %% "creek-xml" % "0.1.8"
)

lazy val creekXmpp = Project("creek-xmpp", file("."))
