package zad1;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client extends JFrame implements MessageListener {

    JTextArea msgArea;
    Receiver receiver;
    Sender sender;
    String username = "";

    public Client() {
        super("Simple JMS Chat");

        // Top content
        JPanel topPanel = new JPanel();
        JLabel usernameLabel = new JLabel("Enter username:");
        JTextField usernameField = new JTextField(10);
        JButton connectBtn = new JButton("Connect");
        JButton disconnectBtn = new JButton("Disconnect");
        disconnectBtn.setEnabled(false);

        topPanel.add(usernameLabel);
        topPanel.add(usernameField);
        topPanel.add(connectBtn);
        topPanel.add(disconnectBtn);

        // Center content
        msgArea = new JTextArea(10, 20);
        msgArea.setEditable(false);
        JScrollPane msgScrollPane = new JScrollPane(msgArea);
        msgScrollPane.setBorder(new TitledBorder(new MatteBorder(1, 1, 1, 1, Color.DARK_GRAY), "Messages"));

        // Bottom content
        JPanel bottomPanel = new JPanel();
        JTextField msg = new JTextField(50);
        JButton sendBtn = new JButton("Send");
        sendBtn.setEnabled(false);

        bottomPanel.add(msg);
        bottomPanel.add(sendBtn);


        // Button listeners
        connectBtn.addActionListener(e -> {
            if (!usernameField.getText().isEmpty()) {
                if (!username.equals("")) {
                    receiver.disconnect();
                    sender.disconnect();
                }

                receiver = new Receiver(this, usernameField.getText());
                username = usernameField.getText();
                disconnectBtn.setEnabled(true);
                sendBtn.setEnabled(true);
                sender = new Sender();
            }
        });

        disconnectBtn.addActionListener(e -> {
            if (receiver != null)
                receiver.disconnect();
        });

        sendBtn.addActionListener(e -> {
            sender.send(username + "  -  " + msg.getText() + "   " +
                    new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis())));
        });


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.PAGE_START);
        mainPanel.add(msgScrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
        this.add(mainPanel);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setVisible(true);
        this.setResizable(false);
    }

    @Override
    public void onMessage(Message message) {
        try {
            msgArea.append(((TextMessage) message).getText() + "\n");
        } catch (JMSException exc) {
            exc.printStackTrace();
        }
    }
}