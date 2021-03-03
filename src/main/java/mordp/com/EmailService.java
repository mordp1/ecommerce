package mordp.com;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService {

    public static void main(String[] args) {
        var emailService = new EmailService();
        try (var service = new KafkaService<O>(EmailService.class.getSimpleName(),
                "ECOMMERCE_NEW_EMAIL",
                emailService::parse,
                Order.class)) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String,String> record) {
        System.out.println("Enviando EMAIL");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignore
            e.printStackTrace();
        }
        System.out.println("Email Enviado");

    }

    }
