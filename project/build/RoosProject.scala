import sbt._

class RoosProject(info: ProjectInfo) extends ParentProject(info) {

  // *****************************************
  // *** Repositories                      ***
  // *****************************************
  object Repositories {
    val akkaRepo = "Akka Repository" at "http://akka.io/repository"
    val scalaToolsRelRepo = MavenRepository("Scala Tools Releases Repo", "http://scala-tools.org/repo-releases")
    val glassfishRepo = MavenRepository("Glassfish Repo", "http://download.java.net/maven/glassfish")
  }

  import Repositories._

  val akkaModuleConf = ModuleConfiguration("se.scalablesolutions.akka", akkaRepo)
  val glassfishModuleConfig = ModuleConfiguration("org.glassfish", glassfishRepo)
  val sjsonModuleConfig = ModuleConfiguration("net.debasishg", scalaToolsRelRepo)


  // *****************************************
  // *** Versions                          ***
  // *****************************************
  val JAVAX_SERVLET_VERSION = "3.0"
  val JUNIT_VERSION = "4.5"
  val SCALATEST_VERSION = "1.6.1"
  val JETTY_VERSION = "7.4.0.v20110414"
  val SJSON_VERSION = "0.12"
  val LOGBACK_VERSION = "0.9.28"


  // *****************************************
  // *** Sub Project Definitions           ***
  // *****************************************
  lazy val roos_core = project("roos-core", "roos-core", new RoosCoreProject(_))
  lazy val roos_document = project("roos-document", "roos-document", new RoosDocumentProject(_), roos_core)
  lazy val roos_web = project("roos-web", "roos-web", new RoosWebProject(_), roos_core, roos_document)

  class RoosDefaultProject(info: ProjectInfo) extends DefaultProject(info) with AkkaProject {
    // with Scalariform {
    // // compileOptions("-unchecked")
    override def compileOptions = super.compileOptions ++ compileOptions("-deprecation")

    object Dependencies {
      lazy val akka_kernel = akkaModule("kernel") intransitive
      lazy val akka_http = akkaModule("http")
      lazy val akka_remote = akkaModule("remote")
      lazy val akka_camel = akkaModule("camel")
      lazy val akka_slf4j   = akkaModule("slf4j")
      lazy val javax_servlet_30 = "org.glassfish" % "javax.servlet" % JAVAX_SERVLET_VERSION % "provided"
      lazy val sjson = "net.debasishg" % "sjson_2.9.0" % SJSON_VERSION % "compile"
      lazy val logback = "ch.qos.logback" % "logback-classic" % LOGBACK_VERSION % "compile"

      // Runtime environment
      lazy val jetty_server_rt = "org.eclipse.jetty" % "jetty-server" % JETTY_VERSION % "runtime"
      lazy val jetty_xml_rt = "org.eclipse.jetty" % "jetty-xml" % JETTY_VERSION % "runtime"
      lazy val jetty_util_rt = "org.eclipse.jetty" % "jetty-util" % JETTY_VERSION % "runtime"
      lazy val jetty_servlet_re = "org.eclipse.jetty" % "jetty-servlet" % JETTY_VERSION % "runtime"

      // Test
      lazy val scala_test = "org.scalatest" % "scalatest_2.9.0" % SCALATEST_VERSION % "test"
      lazy val junit = "junit" % "junit" % JUNIT_VERSION % "test"

      // Boot Information
      lazy val boot_class = "akka.kernel.Main"
      lazy val config_files = List("akka.conf")
    }

  }

  class RoosCoreProject(info: ProjectInfo) extends RoosDefaultProject(info) {
    // Test
    val scala_test = Dependencies.scala_test
    val junit = Dependencies.junit
  }

  class RoosDocumentProject(info: ProjectInfo) extends RoosDefaultProject(info) {
  }

  class RoosWebProject(info: ProjectInfo) extends RoosDefaultProject(info) {
    val akkaKernel = Dependencies.akka_kernel
    val akkaHttp = Dependencies.akka_http
    val javax_servlet_30 = Dependencies.javax_servlet_30

    // Runtime dependencies
    val akkaRemote = Dependencies.akka_remote
    val akkaCamel = Dependencies.akka_camel
    val akkaSLF4J = Dependencies.akka_slf4j
    val logback = Dependencies.logback
    val jettyServer = Dependencies.jetty_server_rt
    val jettyXml = Dependencies.jetty_xml_rt
    val jettyUtil = Dependencies.jetty_util_rt
    val jettyServlet = Dependencies.jetty_servlet_re
  }


  // ******************************************
  // *** Scalariform Setup                  ***
  // ******************************************

  import com.github.olim7t.sbtscalariform._

  trait Scalariform extends ScalariformPlugin {
    override def scalariformOptions = Seq(
      AlignParameters(true),
      CompactStringConcatenation(false),
      IndentPackageBlocks(true),
      PreserveSpaceBeforeArguments(true),
      DoubleIndentClassDeclaration(false),
      RewriteArrowSymbols(false),
      AlignSingleLineCaseStatements(true),
      SpaceBeforeColon(true),
      PreserveDanglingCloseParenthesis(false),
      IndentSpaces(2),
      IndentLocalDefs(false)
    )
  }
}