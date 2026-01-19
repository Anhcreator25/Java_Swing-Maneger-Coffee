package dao;
import javax.swing.JOptionPane;


import java.sql.*;

public class Connect_DB {
    Connection con;
    Statement stmt = null;

    public Connection connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/dbjava";
            String username = "root";
            String password = "251207";

            con = DriverManager.getConnection(url, username, password);


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ket noi that bai !!!");
            throw new RuntimeException(e);
        }
        return con;
    }
//UPDATE,INSERT.DELETE
    public int executeDB(String query) {
        int result = 0;

        try {
            stmt = con.createStatement();
            result = stmt.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }
//SELECT
    public ResultSet getDB(String query) {
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return rs;
    }

}