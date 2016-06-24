name := "creek-xmpp"

version := "0.0"

scalaVersion in ThisBuild := "2.11.8"

lazy val commonSettings = Seq()

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6"
)

val creekXmlVersion = "a60c08f2e649ffea5d5101461ac8b7dc0e4bfb15"
val creekXmlUrlBase = "https://github.com/RGafiyatullin/creek-xml.git"
val creekXmlUrl = url("%s#%s".format(creekXmlUrlBase, creekXmlVersion))
lazy val creekXmlSubProject = RootProject(creekXmlUrl.toURI)


lazy val creekXmpp = Project("creek-xmpp", file(".")).dependsOn(creekXmlSubProject)
