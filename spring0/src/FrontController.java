import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/*") 

public class FrontController extends HttpServlet{

@Override

protected void doGet(HttpServletRequest request, HttpServletResponse response)
throws IOException{

handle(request, response);
}

@Override

protected void doPost(HttpServletRequest request , HttpServletResponse response)
throws IOException{

handle (request, response);
}

private void handle(HttpServletRequest request, HttpServletResponse response)
throws IOException{
    response.setContentType("text/html; charset=UTF-8");

    PrintWriter out = response.getWriter();

     out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>spring0</title></head>");
        out.println("<body>");
        out.println("<h1>Bonjour depuis spring0 !</h1>");


        out.println("<p>Vous avez demandé : " + request.getRequestURI() + "</p>");

        out.println("</body>");
        out.println("</html>");
}
}
