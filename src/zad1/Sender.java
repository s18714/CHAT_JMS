package zad1;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class Sender {

    private Context context;
    private Connection connection = null;
    private Session session;
    private MessageProducer sender;

    public Sender() {
        try {
            Hashtable<String, String> properties = new Hashtable<>();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
            properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");

            context = new InitialContext(properties);
            ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            Topic topic = (Topic) context.lookup("topic1");

            connection = factory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            sender = session.createProducer(topic);
            connection.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void send(String message) {
        try {
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText(message);
            sender.send(textMessage);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                sender.close();
                session.close();
                connection.close();
                context.close();
            } catch (JMSException | NamingException ex) {
                ex.printStackTrace();
            }
        }
    }
}