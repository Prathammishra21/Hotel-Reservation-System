import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {

    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Pratham_1308";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                 Scanner scanner = new Scanner(System.in)) {
                handleUserInput(connection, scanner);
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleUserInput(Connection connection, Scanner scanner) {
        while (true) {
            System.out.println("\nHOTEL MANAGEMENT SYSTEM");
            System.out.println("1. Reserve your room");
            System.out.println("2. View Reservations");
            System.out.println("3. Get Room Number");
            System.out.println("4. Update Reservations");
            System.out.println("5. Delete Reservations");
            System.out.println("0. Exit");
            System.out.println("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> reserveRoom(connection, scanner);
                case 2 -> viewReservations(connection);
                case 3 -> getRoomNumber(connection, scanner);
                case 4 -> updateReservation(connection, scanner);
                case 5 -> deleteReservation(connection, scanner);
                case 0 -> exit();
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void reserveRoom(Connection connection, Scanner scanner) {
        try {
            System.out.println("Enter guest name: ");
            String guestName = scanner.next();
            System.out.println("Enter room number: ");
            int roomNumber = scanner.nextInt();
            System.out.println("Enter contact number: ");
            String contactNumber = scanner.next();

            String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) VALUES (?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, guestName);
                statement.setInt(2, roomNumber);
                statement.setString(3, contactNumber);

                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Reservation successful!");
                } else {
                    System.out.println("Reservation failed.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewReservations(Connection connection) {
        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Current Reservations:");
            // Output formatting...

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void getRoomNumber(Connection connection, Scanner scanner) {
        // Method implementation goes here...
    }

    private static void updateReservation(Connection connection, Scanner scanner) {
        // Method implementation goes here...
    }

    private static void deleteReservation(Connection connection, Scanner scanner) {
        // Method implementation goes here...
    }

    public static void exit() {
        System.out.print("Exiting System");
        try {
            for (int i = 5; i > 0; i--) {
                System.out.print(".");
                Thread.sleep(1000);
            }
            System.out.println("\nThank you for using Hotel Reservation System!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("\nError while exiting: " + e.getMessage());
        }
        System.exit(0);
    }
}
