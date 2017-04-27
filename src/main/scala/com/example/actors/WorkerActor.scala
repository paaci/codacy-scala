package com.example.actors

import akka.actor.Actor

class WorkerActor extends Actor {

  def receive = {

    case s: String => sender ! s"<html><body>Akka-http web app is running on $s!</body></html>"
    case _ => sender ! "<html><body>Something went wrong...</body></html>"

  }

}
