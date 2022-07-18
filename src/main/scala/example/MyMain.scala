package example

import akka.actor.{ActorSystem, PoisonPill}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import example.actor.{DBManagerActor, KafkaActor, RestActor}
import org.slf4j.LoggerFactory
import slick.jdbc.JdbcBackend.Database

import java.util.Properties
import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt


object MyMain extends App {

  val log = LoggerFactory.getLogger(this.getClass)
  implicit val askTimeout: Timeout = 10.seconds
  implicit val system = ActorSystem("root")
  implicit val dispatcher = system.dispatcher

  implicit val materializer = ActorMaterializer()

  val config = ConfigFactory.parseResources("defaults.conf")

  //used in DBWrapper
  implicit lazy val fixedThreadPoolExecutionContext: ExecutionContext = {
    val fixedThreadPool: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors * 2)
    ExecutionContext.fromExecutor(fixedThreadPool)
  }

  val restInterface = config.getString("rest.host")
  val restPort = config.getInt("rest.port")

  implicit lazy val dbSlick: Database = Database.forConfig(path = "db.default", config = config)

  val kafkaProps:Properties = new Properties()

  kafkaProps.put("bootstrap.servers", config.getString("kafka.server"))
  kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  kafkaProps.put("acks","all")

  val kafkaTopic = config.getString("kafka.topic")

  val rest = system.actorOf(RestActor.props()(restInterface, restPort), "rest-actor")
  val kafka = system.actorOf(KafkaActor.props()(kafkaProps, kafkaTopic), "kafka-actor")
  val dbManager = system.actorOf(DBManagerActor.props(), "dbmanager-actor")

  log.info("Start")

  sys.addShutdownHook({
    log.info("Finish")
    rest ! PoisonPill
    kafka ! PoisonPill
    dbManager ! PoisonPill
    
    dbSlick.shutdown
    system.terminate
  })

}
