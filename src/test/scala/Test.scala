import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import com.example.actors.WorkerActor
import org.scalatest._

import scala.concurrent.duration._

class ActorSpec() extends TestKit(ActorSystem("ActorSpec"))
  with ImplicitSender
  with AsyncWordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "WorkerActor must answer gently!" must {

    "answer in HTML message" in {

      implicit val timeout = Timeout(5 seconds)

      val testActor = system.actorOf(Props[WorkerActor])
      val testName = "John"

      testActor ? testName map {
        case s: String => s shouldBe s"<html><body>Akka-http web app is running on $testName!</body></html>"
      }

    }

  }
}