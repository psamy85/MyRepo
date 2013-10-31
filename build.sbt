name := "addon_Clever"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "com.basho.riak" % "riak-client" % "1.4.2"
)     

play.Project.playScalaSettings
