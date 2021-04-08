package com.personal.tracker.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class handles all queries related to deleting something from the database.
 *
 * @deprecated
 * In the next complete version of the tracker, it will have migrated to Hibernate
 * rather than JDBC
 */
@Deprecated(forRemoval = true)
public class Delete {

  /**
   * This method deletes a chapter from the <code>Chapter</code> table, as well as from the
   * Student Progress table if it's been completed.
   *
   * @param chapterId The ID of the chapter to be removed. This is used in the query to select the
                      book to be removed.
   * @param book The title of the book to be removed. This is used in the query to select the book
                 to be removed.
   */
  public static void deleteChapter(Long chapterId, String book) {

    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String sqlDeleteQuery = "DELETE FROM CHAPTER WHERE CHAPTER_NUMBER = ? AND BOOK = ?";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
        "sa", "");

      // Prepare the first query
      statement = conn.prepareStatement(sqlDeleteQuery);
      statement.setLong(1, chapterId);
      statement.setString(2, book);

      // Execute the update and count the new rows
      int newRowCount = statement.executeUpdate();
      System.out.printf("Deleting chapter %d from the Chapter table...\n", chapterId);
      System.out.printf("%d row(s) deleted.\n", newRowCount);
      System.out.println("Chapter Table successfully updated!\n");

      // Prepare the second query
      sqlDeleteQuery = "DELETE FROM STUDENT_PROGRESS WHERE CHAPTER_NUMBER = ? AND BOOK = ?";
      statement = conn.prepareStatement(sqlDeleteQuery);
      statement.setLong(1, chapterId);
      statement.setString(2, book);

      // Execute the update and count the new rows
      newRowCount = statement.executeUpdate();
      System.out.printf("Deleting chapter %d from the Student Progress table...\n", chapterId);
      System.out.printf("%d row(s) deleted.\n", newRowCount);
      System.out.println("Student Progress Table successfully updated!");

      // Cleanup - close the Statement object to free up resources
      statement.close();

      // Cleanup - close the Connection object to free up resources
      conn.close();

    } catch (SQLException sqle) {
      // If there's an Exception, print out the stack trace so we can figure out what's up
      sqle.printStackTrace();
    }
  }

  /**
   * This method deletes a student from the Student table, as well as from the Student Progress table
   * if the student is present in that table.
   *
   * @param studentId The ID of the student being deleted. This is used in the query to select the
                      student to be deleted.
   */
  public static void deleteStudent(Long studentId) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String sqlDeleteQuery = "DELETE FROM STUDENT WHERE STUDENT_ID = ?";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
          "sa", "");

      // Prepare the first query
      statement = conn.prepareStatement(sqlDeleteQuery);
      statement.setLong(1, studentId);

      // Execute the update and count the new rows
      int newRowCount = statement.executeUpdate();
      System.out.printf("Deleting Student %d from the Student table...\n", studentId);
      System.out.printf("%d row(s) deleted.\n", newRowCount);
      System.out.println("Student Table successfully updated!\n");

      // Cleanup - close the Statement object to free up resources
      statement.close();

      // Cleanup - close the Connection object to free up resources
      conn.close();

    } catch (SQLException sqle) {
      // If there's an Exception, print out the stack trace so we can figure out what's up
      sqle.printStackTrace();
    }
  }

  /**
   * This is a helper function to get the ID of a student in the database.
   *
   * @param firstName The first name of the student we're searching for. This is used in the query
                      to select the student whose ID we want.
   * @param lastName The last name of the student we're searching for. This is used in the query to
                     select the student whose ID we want.
   * @return The ID if the student is found, null if the student isn't found.
   */
  public static Long getStudentId(String firstName, String lastName) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;
    Long studentId = null;

    // Initialize the string to hold the query
    String sqlDeleteQuery = "SELECT (SELECT STUDENT_ID FROM STUDENT WHERE FIRST_NAME = ? AND LAST_NAME = ?) AS STUDENT_ID";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
          "sa", "");

      // Prepare the first query
      statement = conn.prepareStatement(sqlDeleteQuery);
      statement.setString(1, firstName);
      statement.setString(2, lastName);

      // Execute the update and count the new rows
      ResultSet results = statement.executeQuery();
      results.next();

      // Retrieve the corresponding ID
      studentId = results.getLong("STUDENT_ID");

      // Cleanup - close the Statement object to free up resources
      statement.close();

      // Cleanup - close the Connection object to free up resources
      conn.close();

      return studentId;
    } catch (SQLException sqle) {
      // If there's an Exception, print out the stack trace so we can figure out what's up
      //sqle.printStackTrace();
      System.err.println("There was an sqle exception");
      return null;
    } finally {
      return studentId;
    }
  }

  /**
   * This method handles deletes a completed chapter from the <code>Student_Progress</code> table
   * in the database.
   *
   * @param studentId - ID of the student who we're removing the completed chapter from. This is
   *                  used in the query to select the Completed Chapter to be removed.
   * @param book - Book title of the book we're removing the completed chapter from. This is
   *               used in the query to select the Completed Chapter to be removed.
   * @param chapterNumber - The Chapter number of the chapter we're removing. This is
   *                        used in the query to select the Completed Chapter to be removed.
   */
  public static void deleteCompletedChapter(Long studentId, String book, Long chapterNumber) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String sqlDeleteQuery = "DELETE FROM STUDENT_PROGRESS " +
        "WHERE STUDENT_ID = ?" +
        "AND BOOK = ?" +
        "AND CHAPTER_NUMBER = ?";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
          "sa", "");

      statement = conn.prepareStatement(sqlDeleteQuery);
      statement.setLong(1, studentId);
      statement.setString(2, book);
      statement.setLong(3, chapterNumber);

      // Execute the update and count the new rows
      int newRowCount = statement.executeUpdate();
      System.out.printf("Deleting chapter %d from the Student Progress table...\n", studentId);
      System.out.printf("%d row(s) deleted.\n", newRowCount);
      System.out.println("Student Progress Table successfully updated!");

      // Cleanup - close the Statement object to free up resources
      statement.close();

      // Cleanup - close the Connection object to free up resources
      conn.close();

    } catch (SQLException sqle) {
      // If there's an Exception, print out the stack trace so we can figure out what's up
      sqle.printStackTrace();
    }
  }
}
