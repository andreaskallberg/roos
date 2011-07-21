import sbt._

class RoosProject(info: ProjectInfo) extends ParentProject(info) {

  // *****************************************
  // *** Repositories                      ***
  // *****************************************
  object Repositories {
    val akkaRepo 		      = "Akka Repository" at "http://akka.io/repository"
    val scalaToolsRelRepo = MavenRepository("Scala Tools Releases Repo", "http://scala-tools.org/repo-releases")
  }

  import Repositories._

  val akkaModuleConf 		  = ModuleConfiguration("se.scalablesolutions.akka", akkaRepo)

  // *****************************************
  // *** Versions                          ***
  // *****************************************
  val JUNIT_VERSION            	= "4.5"
  val SCALATEST_VERSION		= "1.6.1"


  // *****************************************
  // *** Sub Project Definitions           ***
  // *****************************************
  lazy val roos_core 		  = project("roos-core", "roos-core", new RoosCoreProject(_))
  lazy val roos_document	= project("roos-document", "roos-document", new RoosDocumentProject(_), roos_core)
  lazy val roos_web		    = project("roos-web", "roos-web", new RoosWebProject(_), roos_core, roos_document)

  class RoosDefaultProject(info: ProjectInfo) extends DefaultProject(info) with AkkaProject with Scalariform {   // compileOptions("-unchecked")
    override def compileOptions = super.compileOptions ++ compileOptions("-deprecation")
    
    object Dependencies {
      lazy val akka_kernel  = akkaModule("kernel") intransitive
      lazy val akka_http	  = akkaModule("http")

      lazy val scala_test 	= "org.scalatest"	% "scalatest_2.9.0"	% SCALATEST_VERSION	% "test"
      lazy val junit      	= "junit"         % "junit"           % JUNIT_VERSION     % "test"

      // Boot Information
      lazy val boot_class   = "akka.kernel.Main"
      lazy val config_files = List("akka.conf")      
    }
  }

  class RoosCoreProject(info: ProjectInfo) extends RoosDefaultProject(info) {
    // Test
    val scala_test   = Dependencies.scala_test
    val junit        = Dependencies.junit
  }

  class RoosDocumentProject(info: ProjectInfo) extends RoosDefaultProject(info) {
  }

  class RoosWebProject(info: ProjectInfo) extends RoosDefaultProject(info) {
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
