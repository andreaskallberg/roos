import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  object Repositories {
    val akkaRepo = "Akka Repository" at "http://akka.io/repository"
  }

  import Repositories._

  val AKKA_VERSION 		      = "1.1.3"
  val SCALARIFORM_VERSION	  = "1.0.3"

  lazy val akkaModuleConf 	= ModuleConfiguration("se.scalablesolutions.akka", akkaRepo)
  lazy val akka_sbt_plugin 	= "se.scalablesolutions.akka" 	% "akka-sbt-plugin"	% AKKA_VERSION
  lazy val formatter 		    = "com.github.olim7t" 		% "sbt-scalariform" 	% SCALARIFORM_VERSION
}
