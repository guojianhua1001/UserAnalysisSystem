package com.baizhi.main

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

/**
 * 将流处理的结果存入Kafka中
 */
object KafkaSinks {

  private def createKafkaProducer(): KafkaProducer[String, String] = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "SparkOnStandalone:9092")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true")
    props.put(ProducerConfig.RETRIES_CONFIG, "3")
    props.put(ProducerConfig.ACKS_CONFIG, "all")
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, "1024")
    props.put(ProducerConfig.LINGER_MS_CONFIG, "500")
    new KafkaProducer[String, String](props)
  }

  private lazy val kafkaProducer: KafkaProducer[String, String] = createKafkaProducer();

  def saveToKafka(topic: String, value: String): Unit = {
    kafkaProducer.send(new ProducerRecord[String, String](topic, value))
  }

  //添加JVM关闭前回调，退出之前调用shutdownHook
  Runtime.getRuntime.addShutdownHook(new Thread() {
    override def run(): Unit = {
      kafkaProducer.flush()
      kafkaProducer.close()
    }
  })

}
