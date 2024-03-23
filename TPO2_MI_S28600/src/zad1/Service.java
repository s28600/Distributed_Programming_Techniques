/**
 *
 *  @author Moskalenko Illya S28600
 *
 */

package zad1;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class Service {
    String countryName;
    String countryISOcode;
    private final String WEATHER_API_LINK = "https://api.openweathermap.org/data/2.5/weather?q={city name},{country code}&appid=6aa72ca1829d3653da87b87a89438056";

    public Service(String countryName) {
        this.countryName = countryName;

        Locale temp;
        for (String code : Locale.getISOCountries()){
            temp = new Locale("EN", code);
            if (temp.getDisplayCountry().equals(countryName)) countryISOcode = code;
        }
    }

    public String getWeather(String cityName) {
        StringBuilder out = new StringBuilder();
        try {
            URL url = new URL(WEATHER_API_LINK
                    .replace("{city name}", cityName)
                    .replace("{country code}", countryISOcode));
            BufferedReader bfin = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = bfin.readLine()) != null) {
                out.append(line);
            }
            bfin.close();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        System.out.println(out);
        return out.toString();
    }

    public double getRateFor(String currencyName){
        return 0;
    }

    public double getNBPRate(){
        return 0;
    }
}
