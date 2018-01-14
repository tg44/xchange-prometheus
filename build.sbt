name := "xchange-prometheus"

version := "0.1"

scalaVersion := "2.12.4"

//workaround for https://github.com/sbt/sbt/issues/3618
val workaround = {
  sys.props += "packaging.type" -> "jar"
  ()
}

libraryDependencies ++= {
  val akkaHttpVersion = "10.0.11"
  val scalaTestVersion = "3.0.4"

  Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "io.prometheus" % "simpleclient" % "0.1.0",
    "io.prometheus" % "simpleclient_common" % "0.1.0",
    "org.knowm.xchange" % "xchange-core" % "4.3.2",
    "org.knowm.xchange" % "xchange-kraken" % "4.3.2"

  )
}