package example.actor

import akka.actor.{Actor, ActorLogging, Props}
import akka.util.Timeout
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success, Try}

class KafkaActor(props: Properties, topic: String) extends Actor with ActorLogging {

  implicit private val system = context.system
  implicit private val executionContext = system.dispatcher
  implicit private val askTimeout: Timeout = 30.seconds

  val producer = new KafkaProducer[String, String](props)


  def sendMessage(record: ProducerRecord[String, String]) = {

    try {

      Try {
        //val record = new ProducerRecord[String, String](topic, i.toString, "My Site is sparkbyexamples.com " + i)
        val metadata = producer.send(record)

        log.info(s"sent record(key=%s value=%s) " +
          "meta(partition=%d, offset=%d)\n",
          record.key(), record.value(),
          metadata.get().partition(),
          metadata.get().offset())
          metadata
      } match {
        case Success(m) => log.info(s"Message  was sent into topic $topic successfully!  Offset ${m.get().offset()}")
        case Failure(ex) => log.error(s"Error in sending of message to $topic ${ex.getMessage}")
      }

    } finally {
      producer.close()
    }

  }

  override def postStop() = {
    producer.close()
  }

  override def receive: Receive = Actor.emptyBehavior
}

object KafkaActor {
  def props()(props: Properties, topic: String) = Props(classOf[KafkaActor],props, topic)
}