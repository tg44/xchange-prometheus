name := "xchange-prometheus"

version := "0.1"

scalaVersion := "2.12.4"

//workaround for https://github.com/sbt/sbt/issues/3618 not really working with docker build :(
val workaround = {
  sys.props += "packaging.type" -> "jar"
  ()
}

libraryDependencies ++= {
  val akkaHttpVersion = "10.0.11"
  val scalaTestVersion = "3.0.4"
  sys.props += "packaging.type" -> "jar"
  Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "io.prometheus" % "simpleclient" % "0.1.0",
    "io.prometheus" % "simpleclient_common" % "0.1.0",
    //needed to add explicitly idea from https://github.com/ronmamo/reflections/issues/169
    "javax.ws.rs" % "javax.ws.rs-api" % "2.1" artifacts Artifact("javax.ws.rs-api", "jar", "jar"),
    //"javax.ws.rs" % "jsr311-api" % "1.1.1",
    //"org.apache.cxf" % "cxf-rt-frontend-jaxrs" % "3.2.1",
    "org.knowm.xchange" % "xchange-core" % "4.3.2",
    "org.knowm.xchange" % "xchange-kraken" % "4.3.2"

  )
}