package mordp.com;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService {

    public static void main(String[] args) {
        var fraudService = new FraudDetectorService();
        try (var service = new KafkaService<>(FraudDetectorService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                fraudService::parse,
                Order.class,
                Map.of())) {
            service.run();
        }
    }

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException {
        System.out.println("-------------------------------------------------");
        System.out.println("Processando New Order, verificando fruade");
        System.out.println("Key: " + record.key());
        System.out.println("Value: " + record.value());
        System.out.println("Partiton: " + record.partition());
        System.out.println("Offset: " + record.offset());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // ignore
            e.printStackTrace();
        }
        var order = record.value();
        if (isFraud(order)) {
            // pretending that the fraud happens when the amount is >= 4500
            System.out.println("Order is Fraud!!! " +order);
            orderDispatcher.send("ECOMMERCE_ORDER_REJECTED", order.getEmail(), order);
        } else {
            System.out.println("Approved: " + order);
            orderDispatcher.send("ECOMMERCE_ORDER_APPROVED", order.getEmail(), order);
        }

    }

    private boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;
    }
}
