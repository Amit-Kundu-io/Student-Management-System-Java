package com.amit_kundu_io.data;

import com.amit_kundu_io.data.database.DatabaseConfig;
import com.amit_kundu_io.domain.Student;
import com.amit_kundu_io.domain.StudentDataService;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class StudentDataServiceImpl implements StudentDataService {

    private Connection connection;

    public StudentDataServiceImpl() {
        this.connection = DatabaseConfig.getInstance();
    }


    @Override
    public void insert(Student student) {
        var sql = """
                
                """;

        try {

            Statement statement = connection.createStatement();
            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Student student) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Student findById(String id) {
        return null;
    }

    @Override
    public List<Student> findAll() {
        return List.of();
    }
}
