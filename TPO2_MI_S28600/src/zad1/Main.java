/**
 *
 *  @author Moskalenko Illya S28600
 *
 */

package zad1;


public class Main {
  public static void main(String[] args) {
    Service s = new Service("France");
    String weatherJson = s.getWeather("Paris");
    System.out.println(weatherJson);
    Double rate1 = s.getRateFor("USD");
    System.out.println(rate1);
    Double rate2 = s.getNBPRate();
    System.out.println(rate2);
    // ...
    // część uruchamiająca GUI
    GUI.createGUI(s, weatherJson, rate1, rate2);
  }
}
