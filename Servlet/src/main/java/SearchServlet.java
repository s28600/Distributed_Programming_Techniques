import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private List<Car> cars;

    @Override
    public void init() throws ServletException {
        cars = new ArrayList<>();
        // Wczytanie danych z pliku cars.csv (do zaimplementowania)
        // Przykladowe dane
        cars.add(new Car("osobowy", "Toyota", 2020, 5.6));
        cars.add(new Car("ciężarowy", "Volvo", 2019, 12.5));
        cars.add(new Car("F1", "Ferrari", 2021, 30.0));
        cars.add(new Car("dostawczy", "Ford", 2018, 8.5));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
