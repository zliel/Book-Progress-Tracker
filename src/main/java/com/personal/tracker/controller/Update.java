package com.personal.tracker.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class handles all queries related to updating existing data in the database.
 *
 * @deprecated
 * In the next complete version of the tracker, it will have migrated to Hibernate
 * rather than JDBC
 */
@Deprecated(forRemoval = true)
public class Update {

  /**
   * This method handles updating a student's first name in the <code>Student</code> and
   * <code>Completed Chapters</code> tables. Before being passed to this method, the
   * <code>newFirstName</code> param is validated and capitalized when updating the Student
   * TableView in the Student Tab.
   *
   * @param newFirstName The new first name of the student. This is retrieved from the Student
   *                     Tab's cell upon updating the TableView.
   * @param id The ID of the student whose information we're updating, which will be retrieved
   *           using the Delete.getStudentId() method
   */
  public static void updateStudentFirstName(String newFirstName, Long id) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String updateFirstName =
        "UPDATE STUDENT " +
            "SET FIRST_NAME = ? " +
            "WHERE STUDENT_ID = ?";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
          "sa", "");

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

  /**
   * This method handles updating a student's first name in the <code>Student</code> and
   * <code>Completed Chapters</code> tables. Before being passed to this method, the
   * <code>newLastName</code> param is validated and capitalized when updating the Student
   * TableView in the Student Tab.
   *
   * @param newLastName The new last name of the student. This is retrieved from the Student
   *                     Tab's cell upon updating the TableView.
   * @param id The ID of the student whose information we're updating, which will be retrieved
   *           using the Delete.getStudentId() method
   */
  public static void updateStudentLastName(String newLastName, Long id) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String updateFirstName =
        "UPDATE STUDENT " +
            "SET LAST_NAME = ? " +
            "WHERE STUDENT_ID = ?";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
          "sa", "");

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

  /**
   * This method handles updating chapter number for a specified chapter in the
   * <code>Chapter</code> table in the database. Before being passed to this method, the
   * <code>oldChapterNumber</code> and <code>newChapterNumber</code> params are validated when
   * updating the chapter number in the TableView in the Chapter Tab.
   * <br><br>
   * <strong>NOTE</strong>: This method updates the chapter number in both the <code>Chapter</code>
   * and <code>Student_Progress</code> tables in the database.
   *
   * @param oldChapterNumber The chapter number previously associated with the chapter being
   *                         changed. This is used in the query to select the specific chapter to
   *                         be updated.
   * @param newChapterNumber The chapter number replacing the old chapter number.
   * @param bookTitle The title of the book the chapter belongs to. This is used in the query to
   *                  select the specific chapter to be updated.
   */
  public static void updateChapterNumber(long oldChapterNumber, long newChapterNumber, String bookTitle) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String updateQuery =
        "UPDATE CHAPTER " +
            "SET CHAPTER_NUMBER = ? " +
            "WHERE CHAPTER_NUMBER = ? AND BOOK = ? ";

    String secondQuery =
        "UPDATE STUDENT_PROGRESS " +
            "SET CHAPTER_NUMBER = ? " +
            "WHERE CHAPTER_NUMBER = ? AND BOOK = ?";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
          "sa", "");

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

  /**
   * This method handles updating chapter title for a specified chapter in the
   * <code>Chapter</code> table in the database. Before being passed to this method, the
   * <code>newChapterTitle</code> and <code>oldChapterTitle</code> params are validated and
   * capitalized when updating the chapter title in the TableView in the Chapter Tab.
   * <br><br>
   * <strong>NOTE</strong>: This method updates the chapter title in both the <code>Chapter</code>
   * and <code>Student_Progress</code> tables in the database.
   *
   * @param newChapterTitle The chapter title replacing the old chapter title.
   * @param oldChapterTitle The chapter title previously associated with the chapter. This is
   *                        used in the query to select the specific chapter to be updated.
   * @param bookTitle The title of the book the chapter belongs to. This is used in the query to
   *                  select the specific chapter to be updated.
   */
  public static void updateChapterTitle(String newChapterTitle, String oldChapterTitle, String bookTitle) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String updateQuery =
        "UPDATE CHAPTER " +
            "SET CHAPTER_TITLE = ? " +
            "WHERE CHAPTER_TITLE = ? AND BOOK = ? ";

    String secondQuery =
        "UPDATE STUDENT_PROGRESS " +
            "SET CHAPTER_TITLE = ? " +
            "WHERE CHAPTER_TITLE = ? AND BOOK = ?";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
          "sa", "");

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

  /**
   * This method handles updating book title for a specified book in the
   * <code>Chapter</code> table in the database. Before being passed to this method, the
   * <code>oldBookTitle</code> and <code>newBookTitle</code> params are validated and
   * capitalized when updating the chapter title in the TableView in the Chapter Tab.
   * <br><br>
   * <strong>NOTE</strong>: This method updates the book title in both the <code>Chapter</code>
   * and <code>Student_Progress</code> tables in the database.
   *
   * @param oldBookTitle The book title previously associated with the book. This
   *                     is used in the query to select the specific book to be updated.
   * @param newBookTitle The book title replacing the old book title.
   */
  public static void updateBookTitle(String oldBookTitle, String newBookTitle) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String updateQuery =
        "UPDATE CHAPTER " +
            "SET BOOK = ? " +
            "WHERE BOOK = ? ";

    String secondQuery =
        "UPDATE STUDENT_PROGRESS " +
            "SET BOOK = ? " +
            "WHERE BOOK = ?";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
          "sa", "");

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
