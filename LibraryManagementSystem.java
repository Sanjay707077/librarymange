import java.sql.*;
import java.util.Scanner;

public class LibraryManagementSystem {

    private static final String URL = "jdbc:mysql://localhost:3306/librarydb";
    private static final String USER = "root";  // your MySQL username
    private static final String PASSWORD = "Yaswanth@123"; // <-- change this

    public static void main(String[] args) {
        try {
            // Load the MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL JDBC Driver Loaded Successfully!");

            // Connect to the database
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to Database Successfully!");

            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.println("\n--- Library Management System ---");
                System.out.println("1. Add Book");
                System.out.println("2. Issue Book");
                System.out.println("3. Return Book");
                System.out.println("4. View Books");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter Book Title: ");
                        String title = sc.nextLine();
                        System.out.print("Enter Author: ");
                        String author = sc.nextLine();
                        addBook(con, title, author);
                        break;
                    case 2:
                        System.out.print("Enter Book ID to Issue: ");
                        int issueId = sc.nextInt();
                        issueBook(con, issueId);
                        break;
                    case 3:
                        System.out.print("Enter Book ID to Return: ");
                        int returnId = sc.nextInt();
                        returnBook(con, returnId);
                        break;
                    case 4:
                        viewBooks(con);
                        break;
                    case 5:
                        con.close();
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addBook(Connection con, String title, String author) throws SQLException {
        String sql = "INSERT INTO books (title, author) VALUES (?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, author);
        ps.executeUpdate();
        System.out.println("✅ Book Added Successfully!");
    }

    private static void issueBook(Connection con, int id) throws SQLException {
        String sql = "UPDATE books SET isIssued = TRUE WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        int rows = ps.executeUpdate();
        if (rows > 0)
            System.out.println("✅ Book Issued Successfully!");
        else
            System.out.println("❌ Book ID not found!");
    }

    private static void returnBook(Connection con, int id) throws SQLException {
        String sql = "UPDATE books SET isIssued = FALSE WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        int rows = ps.executeUpdate();
        if (rows > 0)
            System.out.println("✅ Book Returned Successfully!");
        else
            System.out.println("❌ Book ID not found!");
    }

    private static void viewBooks(Connection con) throws SQLException {
        String sql = "SELECT * FROM books";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println("\nID\tTitle\t\tAuthor\t\tIssued");
        while (rs.next()) {
            System.out.println(rs.getInt("id") + "\t" + rs.getString("title") + "\t" + rs.getString("author") + "\t" + rs.getBoolean("isIssued"));
        }
    }
}
