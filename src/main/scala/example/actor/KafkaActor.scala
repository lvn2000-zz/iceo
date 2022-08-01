package example.actor

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.pattern.pipe
import akka.util.Timeout
import example.model.{ConsumeMessageRequest, ProduceMessage}
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

import java.util.Properties
import scala.collection.JavaConverters._
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Future, blocking}
import scala.util.Try


class KafkaActor(props: Properties, topic: String) extends Actor with ActorLogging {

  implicit private val system: ActorSystem = context.system
  implicit private val executionContext = system.dispatcher
  implicit private val askTimeout: Timeout = 30.seconds

  val producer = new KafkaProducer[String, String](props)
  val consumer = new KafkaConsumer(props)
  private val topics = List(topic)

  consumer.subscribe(topics.asJava)

  def sendMessageToTopic(record: ProducerRecord[String, String]): Try[Future[RecordMetadata]] = {

    Try {

      val metadata = producer.send(record)

      log.info(s"sent record(key=%s value=%s) " +
        "meta(partition=%d, offset=%d)\n",
        record.key(), record.value(),
        metadata.get().partition(),
        metadata.get().offset())

      scala.concurrent.Future {
        blocking {
          metadata.get()
        }
      }
    }

  }

  def getMessagesFroTopics: Try[Vector[ConsumerRecords[Nothing, Nothing]]] = {

    Try {
      val records = consumer.poll(10)

      for (record <- records.asScala) {
        log.info("Topic: " + record.topic() +
          ",Key: " + record.key() +
          ",Value: " + record.value() +
          ", Offset: " + record.offset() +
          ", Partition: " + record.partition())
      }
      Vector(records)
    }
  }

  override def postStop() = {
    consumer.close()
    producer.close()
  }

  override def receive: Receive = {
    case prod: ProduceMessage => prod.messages.foreach(sendMessageToTopic)
    case ConsumeMessageRequest => Future.successful(getMessagesFroTopics.getOrElse(Vector.empty[ConsumerRecords[Nothing, Nothing]])) pipeTo sender()
    case _ =>
  }
}

object KafkaActor {
  def props()(props: Properties, topic: String) = Props(classOf[KafkaActor], props, topic)
}