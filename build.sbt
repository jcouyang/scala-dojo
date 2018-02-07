import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Hello",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.pegdown" % "pegdown" % "1.6.0" % Test,
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-o"),
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports")
  )
