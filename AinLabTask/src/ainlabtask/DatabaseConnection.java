package ainlabtask;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/studentdb";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Retrieve all students
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM student";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("birth_date"),
                        rs.getString("age"),
                        rs.getString("address"),
                        rs.getString("allergies")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    // Add a new student
    public void addStudent(String name, String birthDate, String age, String address, String allergies) {
        String query = "INSERT INTO student (name, birth_date, age, address, allergies) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);

            // Convert the date string to java.sql.Date format
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = format.parse(birthDate);
            stmt.setDate(2, new java.sql.Date(parsedDate.getTime()));

            stmt.setString(3, age);
            stmt.setString(4, address);
            stmt.setString(5, allergies);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use 'yyyy-MM-dd'");
        }
    }

    // Update an existing student
   public void updateStudent(Student student) {
    String query = "UPDATE student SET name = ?, birth_date = ?, age = ?, address = ?, allergies = ? WHERE id = ?";
    try (Connection conn = connect(); 
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, student.getName());
        stmt.setString(2, student.getBirth_date());
        stmt.setString(3, student.getAge());
        stmt.setString(4, student.getAddress());
        stmt.setString(5, student.getAllergies());
        stmt.setInt(6, student.getID());  // Use the student's ID to locate the record to update
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//delete student
   public void deleteStudent(int studentId) {
    String query = "DELETE FROM student WHERE id = ?";
    try (Connection conn = connect(); 
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, studentId);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    // Search for students by criteria
    public List<Student> searchStudents(String name, String birthDate, String age, String address, String allergies) {
        List<Student> students = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM student WHERE 1=1");

        if (!name.isEmpty()) {
            query.append(" AND name LIKE ? ");
        }
        if (!birthDate.isEmpty()) {
            query.append(" AND birth_date = ? ");
        }
        if (!age.isEmpty()) {
            query.append(" AND age = ? ");
        }
        if (!address.isEmpty()) {
            query.append(" AND address LIKE ? ");
        }
        if (!allergies.isEmpty()) {
            query.append(" AND allergies LIKE ? ");
        }

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int index = 1;
            if (!name.isEmpty()) stmt.setString(index++, "%" + name + "%");
            if (!birthDate.isEmpty()) stmt.setString(index++, birthDate);
            if (!age.isEmpty()) stmt.setString(index++, age);
            if (!address.isEmpty()) stmt.setString(index++, "%" + address + "%");
            if (!allergies.isEmpty()) stmt.setString(index++, "%" + allergies + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("birth_date"),
                            rs.getString("age"),
                            rs.getString("address"),
                            rs.getString("allergies")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
}
