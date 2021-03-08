package mordp.com;

import java.math.BigDecimal;

public class Order {

    private final String userID, orderID;
    private final BigDecimal amount;

    public Order(String userID, String orderID, BigDecimal amount) {
        this.userID = userID;
        this.orderID = orderID;
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "userID='" + userID + '\'' +
                ", orderID='" + orderID + '\'' +
                ", amount=" + amount +
                '}';
    }

    public String getUserID() {
        return userID;
    }
}
