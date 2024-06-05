public class Car {
    private String type;
    private String brand;
    private int prodYear;
    private double fuelConsumption;

    public Car(String type, String brand, int prodYear, double fuelConsumption) {
        this.type = type;
        this.brand = brand;
        this.prodYear = prodYear;
        this.fuelConsumption = fuelConsumption;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public int getProdYear() {
        return prodYear;
    }

    public double getFuelConsumption() {
        return fuelConsumption;
    }
}
