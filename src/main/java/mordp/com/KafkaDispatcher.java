package mordp.com;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

class KafkaDispatcher implements Closeable {

    private final KafkaProducer<String, String> producer;

    KafkaDispatcher(){
       this.producer = new KafkaProducer<>(properties());
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty (ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "129.213.117.48:9092");
        properties.setProperty (ProducerConfig.CLIENT_DNS_LOOKUP_CONFIG, "use_all_dns_ips");
        properties.setProperty (ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty (ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    void send(String topic, String key, String value) throws ExecutionException, InterruptedException {
        var record = new ProducerRecord<>(topic, key, value);
        Callback callback = (data, ex) -> {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }
                System.out.println("Sucesso enviando " + data.topic() + ":::Partition " + data.partition() + "/ Offset " + data.offset() + "/ Timestamp " + data.timestamp());
            };
        producer.send(record, callback).get();
    }

    @Override
    public void close() throws IOException {
        producer.close();
    }
}
