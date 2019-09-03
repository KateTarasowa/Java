package chatdemo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class ChatDemo extends HttpServlet {
    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ServletException(e);
        }

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/mybd", "postgres", "5502639");

            PreparedStatement st1 = connection.prepareStatement("SELECT  * FROM messages");
            ResultSet messages = st1.executeQuery();
            StringBuilder responseText = new StringBuilder("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Chat</title>\n" +
                    "<script>document.addEventListener('DOMContentLoaded', function() {setTimeout(function(){location.reload(true)}, 5000);});</script>" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div>\n");
            while (messages.next()) {
                responseText.append("<p>" + messages.getString("textmessages") + "<p>");
            }
            responseText.append("</div></body></html>");
            OutputStreamWriter out = new OutputStreamWriter(resp.getOutputStream(), StandardCharsets.UTF_8);
            out.write(responseText.toString());
            out.flush();
            connection.close();
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

}
