import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

	public DBConnect() {

	}

	public Connection getConnection() {
		String username = "UserName";
		String password = "EnterPassWord";
		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:1234/", username, password);
			System.out.println("Connected to database");
		} catch (SQLException | ClassNotFoundException e) {
			throw new RuntimeException("Cannot connect the database!", e);
		}

		return conn;
	}

}
