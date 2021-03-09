package mordp.com;

import org.eclipse.jetty.servlet.Source;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {

    private final  KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
    private final  KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
        emailDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                try {

                    //We are not caring about any security issues, we are only
                    // showing how to use http as a starting point
                    var email = req.getParameter("email");
                    var amount = new BigDecimal(req.getParameter("amount"));
                    var orderId = UUID.randomUUID().toString();


                    var order = new Order(orderId, amount, email);
                    orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, order);

                    var emailCode = "Obrigado pelsa sua Order, Estamos processando";
                    emailDispatcher.send("ECOMMERCE_NEW_EMAIL", email, emailCode);

                    System.out.println("New Order send successfully.");
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getWriter().println("New Order send ");

                } catch (ExecutionException e) {
                   throw new ServletException(e);
                } catch (InterruptedException e) {
                    throw new ServletException(e);
                }
    }
}


