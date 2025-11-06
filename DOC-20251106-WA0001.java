import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* ------------------ PART A: LOGIN SERVLET ------------------ */
@WebServlet("/LoginServlet")
public class WebAppServlets extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if ("dev_deswal".equals(username) && "DB@30dec2004".equals(password)) {
            out.println("<h2>Welcome, " + username + "!</h2>");
            out.println("<p>You have logged in successfully.</p>");
        } else {
            out.println("<h2>Invalid login!</h2>");
            out.println("<p>Username or password is incorrect.</p>");
        }

        out.close();
    }

    /* ------------------ PART B: EMPLOYEE DISPLAY SERVLET ------------------ */
    @WebServlet("/EmployeeServlet")
    public static class EmployeeServlet extends HttpServlet {
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "password");

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Employee");

                out.println("<h2>Employee List</h2>");
                out.println("<table border='1'><tr><th>EmpID</th><th>Name</th><th>Salary</th></tr>");

                while (rs.next()) {
                    out.println("<tr><td>" + rs.getInt("EmpID") + "</td><td>" +
                            rs.getString("Name") + "</td><td>" + rs.getDouble("Salary") + "</td></tr>");
                }

                out.println("</table>");
                con.close();
            } catch (Exception e) {
                out.println("Error: " + e.getMessage());
            }

            out.close();
        }
    }

    /* ------------------ PART C: ATTENDANCE SUBMISSION SERVLET ------------------ */
    @WebServlet("/AttendanceServlet")
    public static class AttendanceServlet extends HttpServlet {
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            String studentId = request.getParameter("studentId");
            String date = request.getParameter("date");
            String status = request.getParameter("status");

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "password");

                String query = "INSERT INTO Attendance (StudentID, Date, Status) VALUES (?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, studentId);
                pst.setString(2, date);
                pst.setString(3, status);

                pst.executeUpdate();
                con.close();

                out.println("<h2>Attendance Submitted Successfully!</h2>");
            } catch (Exception e) {
                out.println("Error: " + e.getMessage());
            }

            out.close();
        }
    }
}
