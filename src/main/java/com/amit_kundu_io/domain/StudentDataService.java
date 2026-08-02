package com.amit_kundu_io.domain;

import java.sql.SQLException;
import java.util.List;

/**
 * Contract for anything that can store/retrieve Student data.
 *
 * Why this exists:
 * StudentManagementUI should not know or care HOW students are persisted
 * (MySQL today, maybe PostgreSQL, a REST API, or an in-memory list tomorrow).
 * It only knows about this interface. As long as some class implements
 * these five methods, the UI can use it without any code changes.
 *
 * StudentDAO is the current implementation (MySQL via JDBC), but you could
 * write another implementation (e.g. InMemoryStudentDataService for tests)
 * and swap it in without touching StudentManagementUI at all.
 */
public interface StudentDataService {

    /**
     * Ensures the underlying storage (e.g. the `students` table) exists.
     * Safe to call every time the app starts — should do nothing if it
     * already exists.
     */

    /**
     * Saves a brand new student record.
     * @param student the student to insert
     */
    void insert(Student student) ;

    /**
     * Overwrites an existing student record (matched by ID) with new values.
     * @param student the student data to save; student.getId() identifies which record to update
     */
    void update(Student student) ;

    /**
     * Removes a student record by ID.
     * @param id the student ID to delete
     */
    void delete(String id) ;

    /**
     * Looks up a single student by ID.
     * @param id the student ID to search for
     * @return the matching Student, or null if no student has that ID
     */
    Student findById(String id) ;

    /**
     * Retrieves every student record currently stored.
     * @return a list of all students (empty list if there are none)
     */
    List<Student> findAll() ;
}