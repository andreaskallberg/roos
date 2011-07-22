package org.roos.web

import akka.actor.SupervisorFactory
import akka.config.Supervision
import akka.http.RootEndpoint
import akka.actor.Actor._
import akka.config.Supervision._

class WebBoot {

  val factory = SupervisorFactory(
    SupervisorConfig(
      OneForOneStrategy(List(classOf[Exception]), 3, 100),

      Supervision.Supervise(
        actorOf[RootEndpoint],
        Permanent) ::
        Supervision.Supervise(
          actorOf[WebEndpoint],
          Permanent) :: Nil))

  factory.newInstance.start
}