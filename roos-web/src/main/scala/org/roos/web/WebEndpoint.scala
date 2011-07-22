package org.roos.web

import akka.actor.Actor._
import akka.http.{RootEndpoint, Endpoint}
import akka.actor.{ActorRef, Actor}

object EndpointURI {
  val RoosWebQualifier = "/roos/v0-1"
}

class WebEndpoint extends Actor with Endpoint {

  def hook(uri: String): Boolean = {
    uri.contains(EndpointURI.RoosWebQualifier)
  }

  override def preStart() = {
    val root = Actor.registry.actorsFor(classOf[RootEndpoint]).head
    root ! Endpoint.Attach(hook, provide)
  }

  def receive = handleHttpRequest

  def provide(uri: String): ActorRef = actorOf[WebEventService].start
}