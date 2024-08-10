ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.4.2"

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "utf8",
  "--release:21",
  "-deprecation",
  "-Xfatal-warnings",
)

lazy val commonsLang = (project in file("commons") / "lang")
  .settings(
    name := "messenger-commons-lang"
  )

lazy val core = (project in file("core"))
  .settings(
    name := "messenger-core"
  )

lazy val config = (project in file("config"))
  .settings(
    name := "messenger-config",
    libraryDependencies ++= Seq(
      "org.apache.commons" % "commons-lang3" % "3.16.0",
      "commons-io" % "commons-io" % "2.16.1",
    ),
  )
  .dependsOn(commonsLang, core)

lazy val root = (project in file("."))
  .settings(
    name := "messenger"
  )
  .aggregate(
    commonsLang,
    core,
    config,
  )

commands ++= Seq(
  Command.command("build") { state =>
    "scalafmtCheckAll" ::
      "scalafmtSbtCheck" ::
      "test" ::
      state
  },
  Command.command("rebuild") { state =>
    "clean" :: "build" :: state
  },
)
