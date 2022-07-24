package example

import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import example.actor.ActorSystemTest
import slick.jdbc.PostgresProfile.api._
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration

trait TestInit {

  implicit val timeToWait = Duration(10, TimeUnit.SECONDS)
  implicit val timeout = Timeout(10, TimeUnit.SECONDS)

  implicit val systemTest = new ActorSystemTest()
  implicit val system = systemTest.system
  implicit val ex = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val testDB = "db.test"
  val config = ConfigFactory.parseResources("defaults.conf")
  implicit val dbSlick = Database.forConfig(path = testDB,  config = config)

}
