package zad1;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class Receiver {

    Context context;
    ConnectionFactory factory;
    Topic topic;
    Session session;
    MessageConsumer receiver;
    Connection connection = null;

    public Receiver(MessageListener listener, String user) {
        try {
            Hashtable<String, String> properties = new Hashtable<>();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
            properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");

            context = new InitialContext(properties);
            factory = (ConnectionFactory) context.lookup("ConnectionFactory");
            topic = (Topic) context.lookup("topic1");
            connection = factory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            receiver = session.createDurableSubscriber(topic, user);
            session.createProducer(topic);

            receiver.setMessageListener(listener);
            connection.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                receiver.close();
                session.close();
                connection.close();
                context.close();
            } catch (JMSException | NamingException ex) {
                ex.printStackTrace();
            }
        }
    }
}