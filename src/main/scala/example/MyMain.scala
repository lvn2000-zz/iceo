package example

import akka.actor.{ActorSystem, PoisonPill}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import example.actor.ControllerRest
import org.slf4j.LoggerFactory
import slick.jdbc.JdbcBackend.Database

import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt


object MyMain extends App {

  val log = LoggerFactory.getLogger(this.getClass)
  implicit val askTimeout: Timeout = 10.seconds
  implicit val system = ActorSystem("root")
  implicit val dispatcher = system.dispatcher

  implicit val materializer = ActorMaterializer()

  val config = ConfigFactory.parseResources("defaults.conf");

  //used in DBWrapper
  implicit lazy val fixedThreadPoolExecutionContext: ExecutionContext = {
    val fixedThreadPool: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors * 2)
    ExecutionContext.fromExecutor(fixedThreadPool)
  }

  val restInterface = config.getString("endpoint.interface")
  val restPort = config.getInt("endpoint.port")

  implicit lazy val dbSlick: Database = Database.forConfig(path = "db.default", config = config)

  val rest = system.actorOf(ControllerRest.props()(restInterface, restPort), "rest-controller")

  //val comments = Await.result(dao.getCommets, Duration.Inf )


  log.info("Start")

  sys.addShutdownHook({
    log.info("Finish")
    rest ! PoisonPill
    dbSlick.shutdown
    system.terminate
  })

}
