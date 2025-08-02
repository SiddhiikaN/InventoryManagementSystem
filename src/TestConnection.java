import java.sql.Connection;
import util.DBConnection;

public class TestConnection {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("Connected to database!");
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
