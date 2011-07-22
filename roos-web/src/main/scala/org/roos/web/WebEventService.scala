package org.roos.web

import akka.actor.Actor
import akka.routing.{SmallestMailboxSelector, FixedCapacityStrategy, DefaultActorPool}
import akka.http.{RequestMethod, Post, Get}

class WebEventService extends Actor with DefaultActorPool with FixedCapacityStrategy with SmallestMailboxSelector {

  import akka.http.Endpoint
  import akka.config.Supervision._
  import akka.actor.Actor._

  self.faultHandler = OneForOneStrategy(List(classOf[Throwable]), 10, 10000)
  self.lifeCycle = Permanent
  self.dispatcher = Endpoint.Dispatcher

  //
  // configure the actor pool
  //
  def instance = actorOf[WebEventServiceDelegate]

  def selectionCount = 1

  def partialFill = false

  def limit = Runtime.getRuntime.availableProcessors

  def capacityIncrement = 1

  def receive = _route
}

class WebEventServiceDelegate extends Actor {
  protected def receive = {
    case get: Get => get.OK("Hello GET from WebEventServiceDelegate")
    case post: Post => post.OK("Hello POST from WebEventServiceDelegate")
    case req: RequestMethod => req.NotAllowed("Invalid method for endpoint: " + req)
  }
}