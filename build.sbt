import Dependencies.{test, _}

//ThisBuild / scalaVersion     := "2.13.8"
//ThisBuild / version          := "0.1.0-SNAPSHOT"
//ThisBuild / organization     := "com.example"
//ThisBuild / organizationName := "example"
//
//lazy val root = (project in file("."))
//  .settings(
//    name := "pg",
//    libraryDependencies += scalaTest % Test
//  )

val shared = Seq(
  organization := "com.example",
  version := "1.0.0-SNAPSHOT",
  scalaVersion := "2.13.8", //"2.12.15",
  //scalacOptions += "-Ypartial-unification", //For Scala 2.11.9+ or 2.12 you should add this. (Partial unification is on by default since Scala 2.13, the compiler no longer accepts -Ypartial-unification)
  Test / fork := true,
  run / run := true
)

lazy val root = (project in file("."))
  .settings(
    shared,
    libraryDependencies ++= (commonDeps ++ testDeps),
    mainClass / run := Some("com.example.MyMain")
  )

lazy val commonDeps = Seq(
  akkaHttp.http, akkaHttp.sprayJSon, akkaHttp.actorTyped,
  akkaHttp.stream, akkaHttp.logBack,
  slick.slick, slick.nop, slick.typeSafe, slick.postgreSql,
  kafkaClient,
  swagger.sw_annot, swagger.jsr
)

lazy val testDeps = Seq(test, akkaHttp.httpTestKit, akkaHttp.actorTestKitTyped)

testFrameworks += new TestFramework("munit.Framework")

