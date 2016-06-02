name := "creek-xmpp-stream"

version := "0.0"

scalaVersion in ThisBuild := "2.11.8"

lazy val commonSettings = Seq()

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6"
)

val creekXmlVersion = "2da0a481a93c2a84206a9c6d450d8bc2f891a078"
val creekXmlUrlBase = "https://github.com/RGafiyatullin/creek-xml.git"
val creekXmlUrl = url("%s#%s".format(creekXmlUrlBase, creekXmlVersion))
lazy val creekXmlSubProject = RootProject(creekXmlUrl.toURI)


lazy val creekXmpp = Project("creek-xmpp", file(".")).dependsOn(creekXmlSubProject)
