package mordp.com;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.net.InetAddress;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var producer = new KafkaProducer<String, String>(properties());
        for (var i = 0; i < 100; i++) {
            var key = UUID.randomUUID().toString();
            var value = key + ",555,33333";
            var record = new ProducerRecord<>("ECOMMERCE_NEW_ORDER", key, value);
            var email = "Obrigado pela sua Order, Estamos processando";
            var emailRecord = new ProducerRecord<>("ECOMMERCE_NEW_EMAIL", key, email);
            producer.send(record, getCallback()).get();
            producer.send(emailRecord, getCallback()).get();
        }
    }
    private static Callback getCallback() {
        return (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            System.out.println("Sucesso enviando " + data.topic() + ":::Partition " + data.partition() + "/ Offset " + data.offset() + "/ Timestamp " + data.timestamp());
        };
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty (ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "129.213.117.48:9092");
        properties.setProperty (ProducerConfig.CLIENT_DNS_LOOKUP_CONFIG, "use_all_dns_ips");
        properties.setProperty (ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty (ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }
}
