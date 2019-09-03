import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class Chat extends HttpServlet {
    static int nikId;

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
        OutputStreamWriter out = new OutputStreamWriter(resp.getOutputStream(), StandardCharsets.UTF_8);
        out.write("<!DOCTYPE html><html lang=\"en\"><meta charset=\"UTF-8\"><title>Chat</title></head><body><div><form method='POST'><input type='text' name='new_msg_text'><button type='submit'>Отправить</button></form></div><div><form method='GET'><input type='tex' name='jj'><button type='submit'>Отправить</button></form></div></body></html>");
        out.flush();
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/mybd", "postgres", "5502639");
            PreparedStatement st1 = connection.prepareStatement("SELECT  * FROM messages");
            ResultSet messages = st1.executeQuery();
            int k = 0;
            while (messages.next()) {
                if (!messages.getString("fio").equals(req.getParameter("jj"))) {
                    k = 1;
                }
            }
            if (k == 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(fio) VALUES (?)");
                preparedStatement.setString(1, req.getParameter("jj"));
                preparedStatement.executeUpdate();
                connection.close();
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/mybd", "postgres", "5502639");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO messages(textmessages, idsend, idget) VALUES (?, ?, ?)");
            preparedStatement.setString(1, req.getParameter("new_msg_text"));
            preparedStatement.setInt(2, 1);
            preparedStatement.setInt(3, 2);
            preparedStatement.executeUpdate();
            connection.close();
            doGet(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
