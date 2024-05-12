package zad1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI extends JFrame implements ActionListener {
    Client client;
    private DefaultListModel<String> availableTopicsModel;
    private DefaultListModel<String> subscribedTopicsModel;
    private JList<String> availableTopicsList;
    private JList<String> subscribedTopicsList;

    public UI(Client client) {
        this.client = client;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        // Available Topics Panel
        JPanel availableTopicsPanel = new JPanel(new BorderLayout());
        JLabel availableLabel = new JLabel("Available Topics");
        availableTopicsModel = new DefaultListModel<>();
        String[] topics = client.getTopics().split(" ");
        for (var topic : topics){
            availableTopicsModel.addElement(topic);
        }
        availableTopicsList = new JList<>(availableTopicsModel);
        availableTopicsPanel.add(availableLabel, BorderLayout.NORTH);
        availableTopicsPanel.add(new JScrollPane(availableTopicsList), BorderLayout.CENTER);

        // Subscribed Topics Panel
        JPanel subscribedTopicsPanel = new JPanel(new BorderLayout());
        JLabel subscribedLabel = new JLabel("Subscribed Topics");
        subscribedTopicsModel = new DefaultListModel<>();
        subscribedTopicsList = new JList<>(subscribedTopicsModel);
        subscribedTopicsPanel.add(subscribedLabel, BorderLayout.NORTH);
        subscribedTopicsPanel.add(new JScrollPane(subscribedTopicsList), BorderLayout.CENTER);

        // Subscribe and Unsubscribe Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton subscribeButton = new JButton("Subscribe");
        JButton unsubscribeButton = new JButton("Unsubscribe");
        subscribeButton.addActionListener(this);
        unsubscribeButton.addActionListener(this);
        buttonPanel.add(subscribeButton);
        buttonPanel.add(unsubscribeButton);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridLayout(3, 1));
        mainPanel.add(availableTopicsPanel);
        mainPanel.add(subscribedTopicsPanel);
        mainPanel.add(buttonPanel);

        add(mainPanel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Subscribe")) {
            for (String topic : availableTopicsList.getSelectedValuesList()) {
                client.subscribe(topic);
            }
        } else if (e.getActionCommand().equals("Unsubscribe")) {
            for (String topic : subscribedTopicsList.getSelectedValuesList()) {
                client.unsubscribe(topic);
            }
        }

        try {
            Thread.sleep(5);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        String[] subscribed = client.getSubscriptions().split(" ");
        subscribedTopicsModel.removeAllElements();
        if (!subscribed[0].equals("NONE")){
            for (var sub : subscribed){
                subscribedTopicsModel.addElement(sub);
            }
        }
    }
}

