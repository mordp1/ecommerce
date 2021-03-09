package mordp.com;

import java.math.BigDecimal;

public class Order {

    private final String userID, orderID;
    private final BigDecimal amount;
    private final String email;

    public Order(String userID, String orderID, BigDecimal amount, String email) {
        this.userID = userID;
        this.orderID = orderID;
        this.amount = amount;
        this.email = email;
    }
}
