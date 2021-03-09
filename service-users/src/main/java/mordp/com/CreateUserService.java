package mordp.com;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class CreateUserService {

    private final Connection connection;

    CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        connection = DriverManager.getConnection(url);
        try {
            connection.createStatement().execute("create table Users (" +
                    "uuid varchar (200) primary key," +
                    "email varchar (200))");
        } catch (SQLException ex) {
            // be careful, the sql could be wrong, be really careful
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        var createUserService = new CreateUserService();
        try (var service = new KafkaService<>(CreateUserService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                createUserService::parse,
                Order.class,
                Map.of())) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) throws SQLException {
        System.out.println("-------------------------------------------------");
        System.out.println("Processando New Order, verificando new user");
        System.out.println("Key: " + record.key());
        System.out.println("Value: " + record.value());
        System.out.println("Partiton: " + record.partition());
        System.out.println("Offset: " + record.offset());
        var order = record.value();
        if(isNewUser(order.getEmail())){
            insertNewUser(order.getUserID(), order.getEmail());

        }
    }
    private void insertNewUser (String uuid, String email) throws SQLException {
        var insert= connection.prepareStatement("insert into Users (uuid, email)" +
                "values (?,?)");
        insert.setString(1, uuid);
        insert.setString(2, email);
        insert.execute();
        System.out.println("User uuid e " + email + " adicionado");
    }

    private boolean isNewUser(String email) throws SQLException {
        var exists = connection.prepareStatement("select uuid from users " +
                "where email = ? limit 1");
        exists.setString(1, email);
        var results =  exists.executeQuery();
        return !results.next();
    }
}