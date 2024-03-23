package zad1;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import javax.swing.*;
import java.awt.*;

public class GUI{
    static void createGUI(Service service, String weatherJson, double rate1, double rate2){
        JFrame frame = new JFrame("TPO C02");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 1000, 500);
        frame.setVisible(true);

        JPanel panel = new JPanel(new FlowLayout());
        frame.add(panel);
        JPanel weatherPanel = weatherPanel(service, weatherJson);
        panel.add(weatherPanel);
    }

    static private JPanel weatherPanel(Service service, String weatherJson){
        try {
            JPanel panel = new JPanel(new GridLayout(0,1));
            JsonObject json = (JsonObject) Jsoner.deserialize(weatherJson);
            panel.add(new JLabel(("Location: " + service.country.getDisplayCountry() + ", " + json.get("name"))));
            panel.add(new JLabel("Sky: " + ((JsonObject)((JsonArray)json.get("weather")).get(0)).get("main")));
            panel.add(new JLabel("Temperature: " + ((JsonObject)json.get("main")).get("temp")));
            panel.add(new JLabel("Pressure: " + ((JsonObject)json.get("main")).get("pressure")));
            panel.add(new JLabel("Humidity: " + ((JsonObject)json.get("main")).get("humidity")));
            panel.add(new JLabel("Wind: " + ((JsonObject)json.get("wind")).get("speed")));
            return panel;
        } catch (JsonException e) {
            throw new RuntimeException(e);
        }
    }

    
}
