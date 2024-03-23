/**
 *
 *  @author Moskalenko Illya S28600
 *
 */

package zad1;


import com.github.cliftonlabs.json_simple.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;

public class Service {
    Locale country;
    private final String WEATHER_API = "https://api.openweathermap.org/data/2.5/weather?q={city name},{country code}&appid=6aa72ca1829d3653da87b87a89438056&units=metric";
    private final String CURRENCY_API = "https://v6.exchangerate-api.com/v6/7602de87df2af764f1d1b142/latest/";
    private final String NBP_API = "http://api.nbp.pl/api/exchangerates/rates/{table}/{code}?format=json";

    public Service(String countryName) {
        for (Locale loc : Locale.getAvailableLocales()){
            if (loc.getDisplayCountry().equals(countryName))
                country = loc;
        }
        if(country == null){
            throw new RuntimeException("Wrong or unsupported country entered, please try again.");
        }
    }

    private String getJson(String API) throws IOException {
        URL url = new URL(API);
        StringBuilder out = new StringBuilder();
        BufferedReader bfin = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = bfin.readLine()) != null) {
            out.append(line);
        }
        bfin.close();
        //System.out.println(out);
        return out.toString();
    }

    public String getWeather(String cityName) {
        try {
            return getJson(WEATHER_API
                    .replace("{city name}", cityName)
                    .replace("{country code}", country.getISO3Country()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double getRateFor(String currencyName){
        try {
            String json = getJson(CURRENCY_API + Currency.getInstance(country));
            JsonObject obj = (JsonObject) Jsoner.deserialize(json);
            return Double.parseDouble(((JsonObject)obj.get("conversion_rates")).get(currencyName).toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double getNBPRate(){
        if (Currency.getInstance(country).toString().equals("PLN")){
            return 1;
        } else {
            try {
                String json = getJson(NBP_API.replace("{table}", "A")
                        .replace("{code}", Currency.getInstance(country).toString()));
                JsonObject obj = (JsonObject) Jsoner.deserialize(json);
                JsonObject rates = (JsonObject)((JsonArray)obj.get("rates")).get(0);
                return 1/Double.parseDouble(rates.get("mid").toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
