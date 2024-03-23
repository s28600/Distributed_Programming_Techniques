package zad1;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import javax.swing.*;
import java.awt.*;
import java.util.Currency;

public class GUI {
    static JFrame frame;
    static Service service;
    static JPanel panel;
    static JTextField countryField;
    static JTextField cityField;
    static JTextField currencyField;
    static JPanel infoPanel;

    public static void createGUI(Service s, String weatherJson, double rate1, double rate2) {
        service = s;
        frame = new JFrame("TPO C02");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 1000, 500);
        frame.setLayout(new BorderLayout());

        panel = new JPanel(new FlowLayout());
        frame.add(panel, BorderLayout.NORTH);

        JPanel selectPanel = new JPanel(new GridLayout(0, 1));
        panel.add(selectPanel);

        JLabel countryLabel = new JLabel("Country:");
        countryField = new JTextField(10);
        selectPanel.add(countryLabel);
        selectPanel.add(countryField);

        JLabel cityLabel = new JLabel("City:");
        cityField = new JTextField(10);
        selectPanel.add(cityLabel);
        selectPanel.add(cityField);

        JLabel currencyLabel = new JLabel("Currency:");
        currencyField = new JTextField(10);
        selectPanel.add(currencyLabel);
        selectPanel.add(currencyField);

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateData());
        selectPanel.add(updateButton);

        infoPanel = getInfoPanel(service, weatherJson, rate1, rate2);
        panel.add(infoPanel);

        frame.setVisible(true);
    }

    static private void updateData() {
        String countryName = countryField.getText();
        String cityName = cityField.getText();
        String currencyName = currencyField.getText();

        service = new Service(countryName);

        String newWeatherJson = service.getWeather(cityName);
        double newRate1 = service.getRateFor(currencyName);
        double newRate2 = service.getNBPRate();

        panel.remove(infoPanel);
        infoPanel = getInfoPanel(service, newWeatherJson, newRate1, newRate2);
        panel.add(infoPanel);
        frame.revalidate();
        frame.repaint();
    }

    static private JPanel getInfoPanel(Service service, String weatherJson, double rate1, double rate2) {
        try {
            JPanel panel = new JPanel(new GridLayout(0, 1));
            JsonObject json = (JsonObject) Jsoner.deserialize(weatherJson);
            panel.add(new JLabel(("Location: " + service.country.getDisplayCountry() + ", " + json.get("name"))));
            panel.add(new JLabel("Sky: " + ((JsonObject) ((JsonArray) json.get("weather")).get(0)).get("main")));
            panel.add(new JLabel("Temperature: " + ((JsonObject) json.get("main")).get("temp")));
            panel.add(new JLabel("Pressure: " + ((JsonObject) json.get("main")).get("pressure")));
            panel.add(new JLabel("Humidity: " + ((JsonObject) json.get("main")).get("humidity")));
            panel.add(new JLabel("Wind: " + ((JsonObject) json.get("wind")).get("speed")));
            panel.add(new JLabel(Currency.getInstance(service.country) + " to selected currency rate: " + rate1));
            panel.add(new JLabel("NBP (PLN) rate to " + Currency.getInstance(service.country) + ": " + rate2));
        return panel;
        } catch (JsonException e) {
            throw new RuntimeException(e);
        }
    }
}
