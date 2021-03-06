package mordp.com;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

public class EmailService {

    public static void main(String[] args) {
        var emailService = new EmailService();
        try (var service = new KafkaService (EmailService.class.getSimpleName(),
                "ECOMMERCE_NEW_EMAIL",
                emailService::parse,
                String.class,
                Map.of())) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String,String> record) {
        System.out.println("---------------------------------------------------");
        System.out.println("Enviando EMAIL ");
        System.out.println("Key: " + record.key());
        System.out.println("Value: " + record.value());
        System.out.println("Partition: " + record.partition());
        System.out.println("Offset: " + record.offset());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignore
            e.printStackTrace();
        }
        System.out.println("Email Enviado");

    }

    }
