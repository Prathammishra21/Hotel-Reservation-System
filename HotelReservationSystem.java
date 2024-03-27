import jdk.jshell.SourceCodeAnalysis;

import java.sql.*;
import java.util.Scanner;

import static java.lang.System.exit;

public class HotelReservationSystem {

    private static final String url="jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "Pratham_1308";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection connection = DriverManager.getConnection(url, username, password);
            while(true){
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve your room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.println("Choose an option: ");
                int choice = Scanner.nextInt();
                switch(choice){
                    case 1:
                        reserveRoom(connection, Scanner);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        getRoomNumber(connection, scanner);
                        break;
                    case 4:
                        updateReservation(connection, scanner);
                        break;
                    case 5:
                        deleteReservation(connection, scanner);
                        break;
                    case 0:
                        exit();
                        Scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    private static void reserveRoom(Connection connection, Scanner scanner){
        try{
            System.out.println("Enter guest name: ");
            String guestName = scanner.next();
            scanner.nextLine();
            System.out.println("Enter room number: ");
            int roomNumber = scanner.nextInt();
            System.out.println("Enter contact number: ");
            String contactNumber = scanner.next();

            String sql = "Insert Into reservations (guest_name, room_number, contact_number)" +
                    "Values ('"+guestName+"',"+roomNumber+",'"+contactNumber+"')";

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation successful!");
                } else {
                    System.out.println("Reservation failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewReservations(Connection connection) throws SQLException{
        String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations";

        try(Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql)){

            System.out.println("Current Reservations:");
            System.out.println("+----------------+---------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID |     Guest     |   Room Number |    Contact Number    |    Reservation Date     |");
            System.out.println("+----------------+---------------+---------------+----------------------+-------------------------+");

            while(resultSet.next()){
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("rom_number");
                String ContactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        reservationId, guestName, roomNumber, ContactNumber, reservationDate);
            }

            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
        }
    }

    private static void getRoomNumber(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter reservation ID: ");
            int reservationId = scanner.nextInt();
            System.out.print("Enter guest name: ");
            String guestName = scanner.next();

            String sql = "SELECT room_number FROM reservations " +
                    "WHERE reservation_id = " + reservationId +
                    " AND guest_name = '" + guestName + "'";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_number");
                    System.out.println("Room number for Reservation ID " + reservationId +
                            " and Guest " + guestName + " is: " + roomNumber);
                } else {
                    System.out.println("Reservation not found for the given ID and guest name.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

