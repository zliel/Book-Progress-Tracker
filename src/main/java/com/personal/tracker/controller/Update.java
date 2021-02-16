package com.personal.tracker.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Update {
  public static void updateStudentFirstName(String newFirstName, Long id) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String updateFirstName = "UPDATE STUDENT " + "SET FIRST_NAME = ? " + "WHERE STUDENT_ID = ?";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn =
          DriverManager.getConnection(
              "jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker", "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.prepareStatement(updateFirstName);
      statement.setString(1, newFirstName);
      statement.setLong(2, id);

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

  public static void updateStudentLastName(String newLastName, Long id) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String updateFirstName = "UPDATE STUDENT " + "SET LAST_NAME = ? " + "WHERE STUDENT_ID = ?";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn =
          DriverManager.getConnection(
              "jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker", "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.prepareStatement(updateFirstName);
      statement.setString(1, newLastName);
      statement.setLong(2, id);

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

  public static void updateChapterNumber(
      long oldChapterNumber, long newChapterNumber, String bookTitle) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String updateQuery =
        "UPDATE CHAPTER " + "SET CHAPTER_NUMBER = ? " + "WHERE CHAPTER_NUMBER = ? AND BOOK = ? ";

    String secondQuery =
        "UPDATE STUDENT_PROGRESS "
            + "SET CHAPTER_NUMBER = ? "
            + "WHERE CHAPTER_NUMBER = ? AND BOOK = ?";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn =
          DriverManager.getConnection(
              "jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker", "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.prepareStatement(updateQuery);
      statement.setLong(1, newChapterNumber);
      statement.setLong(2, oldChapterNumber);
      statement.setString(3, bookTitle);

      // Execute the update and count the new rows
      int newRowCount = statement.executeUpdate();
      System.out.printf("%d row(s) added.\n", newRowCount);
      System.out.println("Table successfully updated!");

      // Prepare our second query
      statement = conn.prepareStatement(secondQuery);
      statement.setLong(1, newChapterNumber);
      statement.setLong(2, oldChapterNumber);
      statement.setString(3, bookTitle);

      // Execute the update and count the new rows
      newRowCount = statement.executeUpdate();
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

  public static void updateChapterTitle(
      String newChapterTitle, String oldChapterTitle, String bookTitle) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String updateQuery =
        "UPDATE CHAPTER " + "SET CHAPTER_TITLE = ? " + "WHERE CHAPTER_TITLE = ? AND BOOK = ? ";

    String secondQuery =
        "UPDATE STUDENT_PROGRESS "
            + "SET CHAPTER_TITLE = ? "
            + "WHERE CHAPTER_TITLE = ? AND BOOK = ?";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn =
          DriverManager.getConnection(
              "jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker", "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.prepareStatement(updateQuery);
      statement.setString(1, newChapterTitle);
      statement.setString(2, oldChapterTitle);
      statement.setString(3, bookTitle);

      // Execute the update and count the new rows
      int newRowCount = statement.executeUpdate();
      System.out.printf("%d row(s) added.\n", newRowCount);
      System.out.println("Table successfully updated!");

      // Prepare our second query
      statement = conn.prepareStatement(secondQuery);
      statement.setString(1, newChapterTitle);
      statement.setString(2, oldChapterTitle);
      statement.setString(3, bookTitle);

      // Execute the update and count the new rows
      newRowCount = statement.executeUpdate();
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

  public static void updateBookTitle(String oldBookTitle, String newBookTitle) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String updateQuery = "UPDATE CHAPTER " + "SET BOOK = ? " + "WHERE BOOK = ? ";

    String secondQuery = "UPDATE STUDENT_PROGRESS " + "SET BOOK = ? " + "WHERE BOOK = ?";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn =
          DriverManager.getConnection(
              "jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker", "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.prepareStatement(updateQuery);
      statement.setString(1, newBookTitle);
      statement.setString(2, oldBookTitle);

      // Execute the update and count the new rows
      int newRowCount = statement.executeUpdate();
      System.out.printf("%d row(s) added.\n", newRowCount);
      System.out.println("Table successfully updated!");

      // Prepare our second query
      statement = conn.prepareStatement(secondQuery);
      statement.setString(1, newBookTitle);
      statement.setString(2, oldBookTitle);

      // Execute the update and count the new rows
      newRowCount = statement.executeUpdate();
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
