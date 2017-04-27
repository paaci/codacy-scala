package com.example

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.example.actors.WorkerActor

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Properties

object SampleApp extends App {

  implicit val system = ActorSystem("SampleSystem")
  val worker = system.actorOf(Props[WorkerActor], "worker1")
  implicit val materializer = ActorMaterializer()
  implicit val timeout = Timeout(5 seconds)

  val port = Properties.envOrElse("PORT", "8080").toInt

  // needed for the future map/flatmap in the end
  implicit val executionContext = system.dispatcher

  val requestHandler: HttpRequest => Future[HttpResponse] = {

    case HttpRequest(GET, Uri.Path("/"), _, _, _) =>

      worker ? "Heroku" map { resp =>


        HttpResponse(entity = HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          resp.toString
        ))
      }

    case r: HttpRequest =>
      r.discardEntityBytes() // important to drain incoming HTTP Entity stream
      Future(HttpResponse(404, entity = "Unknown resource!"))

  }

  Http().bindAndHandleAsync(requestHandler, "0.0.0.0", port)

}