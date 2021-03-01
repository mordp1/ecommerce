package mordp.com;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
       try(var dispatcher = new KafkaDispatcher<Order>());
        {
            for (var i = 0; i < 10; i++) {
                var key = UUID.randomUUID().toString();
                var value = key + "1,555,33333,123";
                dispatcher.send("ECOMMERCE_NEW_ORDER", key, value);
                var email = "Obrigado pelsa sua Order, Estamos processando";
                dispatcher.send("ECOMMERCE_NEW_EMAIL", key, email);
            }
        }
    }
}
