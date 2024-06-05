import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private List<Car> cars;

    @Override
    public void init() throws ServletException {
        cars = new ArrayList<>();
        try {
            loadCarsFromFile();
        } catch (IOException e) {
            throw new ServletException("Failed to load car data", e);
        }
    }

    private void loadCarsFromFile() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("cars.csv");
        if (inputStream == null) {
            throw new IOException("File not found: cars.csv");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            reader.readLine(); // skip header line
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String type = data[0];
                    String brand = data[1];
                    int year = Integer.parseInt(data[2]);
                    double fuelConsumption = Double.parseDouble(data[3]);
                    cars.add(new Car(type, brand, year, fuelConsumption));
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String carType = (String) req.getAttribute("carType");
        List<Car> result = new ArrayList<>();

        for (Car car : cars) {
            if (car.getType().equalsIgnoreCase(carType)) {
                result.add(car);
            }
        }

        req.setAttribute("cars", result);
        req.getRequestDispatcher("/response").forward(req, resp);
    }
}
