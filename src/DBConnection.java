import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Method to get a connection to MySQL database
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Database URL
            String url = "jdbc:mysql://localhost:3306/bankdb?useSSL=false&serverTimezone=UTC"; // your database name
            String user = "root"; // your MySQL username
            String password = "Root@123"; // replace with your MySQL password

            // Connect to database
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL successfully!");

        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
        return connection;
    }

    // Optional: Test connection directly
    public static void main(String[] args) {
        Connection con = DBConnection.getConnection();
        if (con != null) {
            System.out.println("Connection test successful!");
        } else {
            System.out.println("Connection test failed!");
        }
    }
}
