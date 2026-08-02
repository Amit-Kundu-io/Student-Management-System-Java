package com.amit_kundu_io.data;

import com.amit_kundu_io.data.database.DatabaseConfig;
import com.amit_kundu_io.domain.Student;
import com.amit_kundu_io.domain.StudentDataService;
import com.mysql.cj.protocol.ResultsetRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDataServiceImpl implements StudentDataService {

    private Connection connection;

    public StudentDataServiceImpl() {
        this.connection = DatabaseConfig.getInstance();
    }


    @Override
    public void insert(Student student) {

        String sql = """
                INSERT INTO students ( name, email, course)
                VALUES ( ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            statement.setString(3, student.getCourse());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert student", e);
        }
    }
    @Override
    public void update(Student student) {

        String sql = """
            UPDATE students
            SET name = ?, email = ?, course = ?
            WHERE id = ?
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            System.out.println("Student not found." +  student.getId());

            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            statement.setString(3, student.getCourse());
            statement.setInt(4, student.getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("Student not found.");
            } else {
                System.out.println("Student updated successfully.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update student", e);
        }
    }

    @Override
    public void delete(String id) {

        String sql = """
            DELETE FROM students
            WHERE id = ?
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("Student not found.");
            } else {
                System.out.println("Student deleted successfully.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete student", e);
        }
    }

    @Override
    public Student findById(String id) {

        String sql = """
            SELECT * FROM students
            WHERE id = ?
            """;

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();

            // ResultSet starts before the first row, so move to the first row before reading.
            // Since 'id' is unique, we expect at most one result.
            if (rs.next()) {
                return toStudent(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Student> findAll() {

        String sql = "SELECT * FROM students";

        List<Student> students = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Move through each row until there are no more rows.
            while (resultSet.next()) {
                students.add(toStudent(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch students", e);
        }

        return students;
    }


    Student toStudent(ResultSet rs) throws SQLException {
        Student student = new Student();

        student.setId(rs.getInt("id"));
        student.setName(rs.getString("name"));
        student.setEmail(rs.getString("email"));
        student.setCourse(rs.getString("course"));

        return student;
    }

}
