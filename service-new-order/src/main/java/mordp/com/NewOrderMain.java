package mordp.com;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
       try(var orderDispatcher = new KafkaDispatcher<Order>()) {
           var email = Math.random() + "@email.com";
        try(var emailDispatcher = new KafkaDispatcher<String>()) {
                for (var i = 0; i < 100; i++) {

                    var orderId = UUID.randomUUID().toString();
                    var amount = new BigDecimal(Math.random() * 5000 + 1);

                    var order = new Order(orderId, amount, email);
                    orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, order);

                    var emailCode = "Obrigado pelsa sua Order, Estamos processando";
                    emailDispatcher.send("ECOMMERCE_NEW_EMAIL", email, emailCode);
                }
            }
        }
    }
}
