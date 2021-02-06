package com.personal.tracker.controller;

import java.sql.*;
import java.time.LocalDate;

/**
 * This class handles all queries related to adding things to the database.
 *
 * @author Zac Liel
 * @version 0.1
 * @since 12-31-2020
 */
public class Add {

  /**
   * This method adds a completed chapter to the Student_Progress table.
   *
   * @param firstName The first name of the student who completed the chapter.
   * @param lastName The last name of the student who completed the chapter.
   * @param chapterNumber The ID of the chapter that's been completed.
   */
  public static void addCompletedChapter(String firstName, String lastName, long chapterNumber,
                                         String book) {
    LocalDate date = LocalDate.now();

    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String sqlQuery =
      "INSERT INTO student_progress " +
        "(STUDENT_ID, CHAPTER_NUMBER, COMPLETION_DATE, BOOK, CHAPTER_TITLE) " +
        "VALUES " +
        "((SELECT STUDENT_ID FROM student WHERE FIRST_NAME = ? AND LAST_NAME = ?), ?, ?, ?, " +
          "(SELECT CHAPTER_TITLE FROM CHAPTER WHERE CHAPTER_NUMBER = ? AND BOOK = ?))";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
        "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.prepareStatement(sqlQuery);
      statement.setString(1, firstName);
      statement.setString(2, lastName);
      statement.setLong(3, chapterNumber);
      statement.setDate(4, Date.valueOf(date));
      statement.setString(5, book);
      statement.setLong(6, chapterNumber);
      statement.setString(7, book);

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
   * This method adds a chapter to the Chapter table in the database.
   *
   * @param chapterNum The ID of the chapter to add.
   * @param chapterTitle The title of the chapter to add.
   */
  public static void addChapter(long chapterNum, String chapterTitle, String book) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String sqlQuery =
      "INSERT INTO CHAPTER" +
        "(CHAPTER_NUMBER, CHAPTER_TITLE, BOOK)" +
        "VALUES" +
        "(?, ?, ?)";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
        "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.prepareStatement(sqlQuery);
      statement.setLong(1, chapterNum);
      statement.setString(2, chapterTitle);
      statement.setString(3, book);

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
   * This method adds a student to the Student table in the database.
   *
   * @param firstName The first name of the new student.
   * @param lastName The last name of the new student.
   */
  public static void addStudent(String firstName, String lastName) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String sqlQuery =
        "INSERT INTO STUDENT" +
            "(FIRST_NAME, LAST_NAME)" +
            "VALUES" +
            "(?, ?)";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
          "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.prepareStatement(sqlQuery);
      statement.setString(1, firstName);
      statement.setString(2, lastName);


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

  public static void createDatabase() {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    Statement statement;

    // Initialize the string to hold the query
    String studentTableQuery =
        "CREATE TABLE STUDENT (" +
            "STUDENT_ID LONG AUTO_INCREMENT PRIMARY KEY," +
            "FIRST_NAME varchar(255) NOT NULL," +
            "LAST_NAME varchar(255) NOT NULL" +
            ")";

    String chapterTableQuery =
        "CREATE TABLE CHAPTER (" +
            "CHAPTER_ID LONG AUTO_INCREMENT PRIMARY KEY," +
            "CHAPTER_NUMBER LONG NOT NULL," +
            "CHAPTER_TITLE varchar(255) NOT NULL," +
            "BOOK varchar(255) NOT NULL" +
            ")";

    String completedChapterTableQuery =
        "CREATE TABLE STUDENT_PROGRESS (" +
            "STUDENT_ID LONG NOT NULL," +
            "CHAPTER_NUMBER LONG NOT NULL," +
            "CHAPTER_TITLE varchar(255) NOT NULL," +
            "BOOK varchar(255) NOT NULL," +
            "COMPLETION_DATE DATE NOT NULL," +
            "FOREIGN KEY (STUDENT_ID) REFERENCES STUDENT(STUDENT_ID) ON DELETE CASCADE" +
            ")";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
          "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.createStatement();


      // Execute the update and count the new rows
      int newRowCount = statement.executeUpdate(studentTableQuery);
      System.out.printf("%d row(s) added.\n", newRowCount);
      System.out.println("Table successfully updated!");

      newRowCount = statement.executeUpdate(chapterTableQuery);
      System.out.printf("%d row(s) added.\n", newRowCount);
      System.out.println("Table successfully updated!");

      newRowCount = statement.executeUpdate(completedChapterTableQuery);
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
