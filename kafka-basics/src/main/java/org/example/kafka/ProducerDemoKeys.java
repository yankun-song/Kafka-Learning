package org.example.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKeys {

    private static final Logger log = LoggerFactory.getLogger(ProducerDemoKeys.class.getSimpleName());

    public static void main(String[] args) {


        log.info("I am a Kafka Producer!");

        // create Producer Properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create the Producer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for (int i=0; i<10; i++){

            // create a producer record
            String topic = "demo_java";
            String value = "hello world" + i;
            String key = "id_" + i;



            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);



            // send data - async
            producer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    // executed everytime is successfully sent or an exception is thrown
                    if (exception == null){
                        log.info("Received new metadata/ \n" +
                                "Topic: " + metadata.topic() + "\n" +
                                "Key: " + producerRecord.key() + "\n" +
                                "Partition: " + metadata.partition() + "\n" +
                                "Offset: "+ metadata.offset() + "\n" +
                                "Timestamp: " + metadata.timestamp());
                    }else{
                        log.error("Error while producing", exception);
                    }
                }
            });

            // The default partitioner is Sticky
//            try {
//                Thread.sleep(1000);
//            }catch (InterruptedException e){
//                e.printStackTrace();
//            }

        }


        // flush data - sync
        producer.flush();

        // flush and close the Producer
        producer.close();
    }
}
