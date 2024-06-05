import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/response")
public class ResponseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Car> cars = (List<Car>) req.getAttribute("cars");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        resp.getWriter().println("<html><body><h2>Lista samochodów</h2>");
        resp.getWriter().println("<table border='1'><tr><th>Rodzaj</th><th>Marka</th><th>Rok produkcji</th><th>Zużycie paliwa</th></tr>");

        for (Car car : cars) {
            resp.getWriter().println("<tr>");
            resp.getWriter().println("<td>" + car.getType() + "</td>");
            resp.getWriter().println("<td>" + car.getBrand() + "</td>");
            resp.getWriter().println("<td>" + car.getProdYear() + "</td>");
            resp.getWriter().println("<td>" + car.getFuelConsumption() + "</td>");
            resp.getWriter().println("</tr>");
        }

        resp.getWriter().println("</table></body></html>");
    }
}
