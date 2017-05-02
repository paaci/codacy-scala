import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest._
import akka.pattern.ask
import com.example.actors.WorkerActor

class ActorSpec() extends TestKit(ActorSystem("ActorSpec"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "WorkerActor must answer gently!" must {

    "answer in HTML message" in {
      val actor = system.actorOf(Props[WorkerActor])
      val testName = "John"
      actor ! testName
      expectMsg(s"<html><body>Akka-http web app is running on $testName!</body></html>")
    }

  }
}