import Dependencies._

name := "scala-url-builder"

organization := "org.f100ded.scala-url-builder"

scalaVersion := Versions.scala

crossScalaVersions := Versions.crossScala

scalacOptions ++= List("-unchecked", "-deprecation", "-encoding", "UTF-8")

libraryDependencies += Libraries.scalaTest % "test"

licenses := Seq("Apache 2.0 License" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

homepage := Some(url("https://github.com/f100ded/scala-url-builder"))

publishTo := {
  if (isSnapshot.value) {
    Some(Opts.resolver.sonatypeSnapshots)
  } else {
    Some(Opts.resolver.sonatypeStaging)
  }
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := (_ => false)

pomExtra := {
  <scm>
    <url>https://github.com/f100ded/scala-url-builder</url>
    <connection>scm:git:git://github.com/f100ded/scala-url-builder.git</connection>
  </scm>
    <developers>
      <developer>
        <id>tartakynov</id>
        <name>Artem Tartakynov</name>
        <url>http://github.com/tartakynov</url>
      </developer>
    </developers>
}

useGpg := true

useGpgAgent := true

usePgpKeyHex("7A198644")