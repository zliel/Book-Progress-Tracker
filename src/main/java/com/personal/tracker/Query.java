package com.personal.tracker;

// Imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This program will query my h2 database to track a student's progress (although currently I am
 * the only student)
 *
 * @author Zac Liel
 * @version 0.1
 * @since 12-31-2020
 */
public class Query {
  public static void getCompletedChapters(String firstName, String lastName) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String sqlQuery =
      "SELECT sprog.COMPLETION_DATE, c.CHAPTER_ID, c.CHAPTER_TITLE " +
        "FROM chapter AS c, student_progress AS sprog " +
        "WHERE " +
        "(SELECT STUDENT_ID FROM student WHERE FIRST_NAME = ? " + " AND LAST_NAME = ? ) " + "= sprog.STUDENT_ID " +
        "AND c.CHAPTER_ID = sprog.CHAPTER_ID";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker", "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.prepareStatement(sqlQuery);
      statement.setString(1, firstName);
      statement.setString(2, lastName);

      // Make a ResultSet object to store the query results
      // Error was found here -> you don't pass the sqlQuery string to executeQuery with a
      // PreparedStatement
      ResultSet results = statement.executeQuery();

      // Header containing the student's name
      System.out.printf("\n%s %s\n", firstName.toUpperCase(), lastName.toUpperCase());

      while (results.next()) {
        // Retrieve the data from each column and print out the result
        System.out.printf("%s \t", results.getDate("COMPLETION_DATE").toString());
        System.out.printf("%d \t", results.getLong("CHAPTER_ID"));
        System.out.printf("%s \n", results.getString("CHAPTER_TITLE"));
      }

      // Cleanup - close the ResultSet object to free up resources
      results.close();

      // Cleanup - close the Statement object to free up resources
      statement.close();

      // Cleanup - close the Connection object to free up resources
      conn.close();

    } catch (SQLException sqle) {
      // If there's an Exception, print out the stack trace so we can figure out what's up
      sqle.printStackTrace();
    }
  }

  public static void listChapters() {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    Statement statement;

    // Initialize the string to hold the query
    String sqlQuery = "SELECT CHAPTER_ID, CHAPTER_TITLE FROM CHAPTER";

    // Try block (because jdbc methods can throw SQLException exceptions
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker", "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.createStatement();


      // Make a ResultSet object to store the query results
      ResultSet results = statement.executeQuery(sqlQuery);

      while (results.next()) {
        // Retrieve the data from each column and print out the result
        System.out.printf("%d \t", results.getLong("CHAPTER_ID"));
        System.out.printf("%s \n", results.getString("CHAPTER_TITLE"));
      }

      // Cleanup - close the ResultSet object to free up resources
      results.close();

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
