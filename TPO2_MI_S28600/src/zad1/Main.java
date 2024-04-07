/**
 *
 *  @author Moskalenko Illya S28600
 *
 */

package zad1;


public class Main {
  public static void main(String[] args) {
    Service s = new Service("Italy");
    String weatherJson = s.getWeather("Rome");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();
    // ...
    // część uruchamiająca GUI
    GUI.createGUI(s, weatherJson, rate1, rate2);
  }
}
