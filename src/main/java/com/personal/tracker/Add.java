package com.personal.tracker;

import java.sql.*;
import java.time.LocalDate;

/**
 * This program will insert a row into my h2 database to mark a chapter as having been completed
 *
 * @author Zac Liel
 * @version 0.1
 * @since 12-31-2020
 */
public class Add {
  public static void addCompletedChapter(String firstName, String lastName, Long chapterId) {
    LocalDate date = LocalDate.now();
    /* ARGS
    1. Student's first name
    2. Student's last name
    3. Chapter Number (ID) of the chapter that's been completed
     */

    //    String studentFirstName = firstName;
    //    String studentLastName = lastName;
    //    Long chapterId = chapter;

    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String sqlQuery =
      "INSERT INTO student_progress " +
        "(STUDENT_ID, CHAPTER_ID, COMPLETION_DATE) " +
        "VALUES " +
        "((SELECT STUDENT_ID FROM student WHERE FIRST_NAME = ? AND LAST_NAME = ?), ?, ?)";

    // Try block (because jdbc methods can throw SQLException exceptions
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
        "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.prepareStatement(sqlQuery);
      statement.setString(1, firstName);
      statement.setString(2, lastName);
      statement.setLong(3, chapterId);
      statement.setDate(4, Date.valueOf(date));

      // Execute the update and count the new rows
      int newRowCount = statement.executeUpdate();
      System.out.printf("%d row(s) added.\n", newRowCount);
      System.out.println("Table successfully updated!");

      // Cleanup - close the Statement object to free up resources
      statement.close();

      // Cleanup - close the Connection object to free up resources
      conn.close();

    } catch (SQLException sqle) {
      // If there's an Exception, print out the stack trace so we can figure out what's up
      sqle.printStackTrace();
    }
  }

  public static void addChapter(Long chapterId, String chapterTitle) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String sqlQuery =
      "INSERT INTO CHAPTER" +
        "(CHAPTER_ID, CHAPTER_TITLE)" +
        "VALUES" +
        "(?, ?)";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
        "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.prepareStatement(sqlQuery);
      statement.setLong(1, chapterId);
      statement.setString(2, chapterTitle);

      // Execute the update and count the new rows
      int newRowCount = statement.executeUpdate();
      System.out.printf("%d row(s) added.\n", newRowCount);
      System.out.println("Table successfully updated!");

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
