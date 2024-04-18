package zad1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UI extends JFrame {
    private Client client;
    private MainServer mainServer;
    private JTextField inputField;
    private JTextField translationField;
    private JComboBox<String> languageComboBox;
    private JButton translateButton;

    public UI(Client client, MainServer mainServer) {
        this.client = client;
        this.mainServer = mainServer;
        setTitle("Translator");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel inputLabel = new JLabel("Enter word:");
        inputField = new JTextField();
        JLabel translationLabel = new JLabel("Translation:");
        translationField = new JTextField();
        translationField.setEditable(false);

        JLabel languageLabel = new JLabel("Choose language:");
        String[] languages = mainServer.langServers.keySet().toArray(String[]::new);
        languageComboBox = new JComboBox<>(languages);

        translateButton = new JButton("Translate");
        translateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                translate();
            }
        });

        panel.add(inputLabel);
        panel.add(inputField);
        panel.add(translationLabel);
        panel.add(translationField);
        panel.add(languageLabel);
        panel.add(languageComboBox);

        add(panel, BorderLayout.CENTER);
        add(translateButton, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void translate() {
        String word = inputField.getText();
        String langCode = (String) languageComboBox.getSelectedItem();
        String translation = client.getTranslation(word, langCode);
        translationField.setText(translation);
    }
}
