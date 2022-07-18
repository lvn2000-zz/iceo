import sbt._

object Dependencies {

  lazy val test = "org.scalatest" %% "scalatest" % "3.2.10" % "test->compile"

  object akkaHttp {
    val version = "10.2.7"
    val logbackVersion = "1.2.10"
    val akkaStreamVersion = "2.6.18"
    lazy val http = "com.typesafe.akka" %% "akka-http" % version
    lazy val sprayJSon = "com.typesafe.akka" %% "akka-http-spray-json" % version
    lazy val logBack = "ch.qos.logback" % "logback-classic" % logbackVersion
    lazy val actorTyped = "com.typesafe.akka" %% "akka-actor-typed" % akkaStreamVersion
    lazy val stream = "com.typesafe.akka" %% "akka-stream" % akkaStreamVersion
    lazy val actorTestKitTyped = "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaStreamVersion % Test
    lazy val httpTestKit = "com.typesafe.akka" %% "akka-http-testkit" % version % Test
  }

  object slick {
    val version = "3.3.3"
    val slick =  "com.typesafe.slick" %% "slick" % version
    val typeSafe = "com.typesafe.slick" %% "slick-hikaricp" % version
    val nop = "org.slf4j" % "slf4j-nop" % "1.7.36"
    val postgreSql = "org.postgresql" % "postgresql" % "42.4.0"
  }

  val kafkaClient = "org.apache.kafka" % "kafka-clients" % "3.2.0"

  object swagger {
    val sw_annot = "io.swagger.core.v3" % "swagger-annotations" % "2.2.1"
    val jsr = "javax.ws.rs" % "jsr311-api" % "1.1.1"
  }
}

