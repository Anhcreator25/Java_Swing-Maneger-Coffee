package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class to rename the `account` table to `taikhoan`.
 * Run this as a standalone program once before using the application.
 */
public class RenameAccountTable {
    public static void main(String[] args) {
        Connect_DB db = new Connect_DB();
        Connection con = db.connect();
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate("RENAME TABLE account TO taikhoan");
            System.out.println("Table 'account' successfully renamed to 'taikhoan'.");
        } catch (SQLException e) {
            System.err.println("Failed to rename table: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
