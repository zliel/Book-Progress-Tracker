package com.personal.tracker;

import java.sql.*;

public class Update {
  public static void updateStudentFirstName(String newFirstName, String oldFirstName, String oldLastName) {
    Long studentId = Delete.getStudentId(oldFirstName, oldLastName);
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String sqlQuery =
        "UPDATE student_progress " +
            "SET FIRST_NAME = ? " +
            "WHERE STUDENT_ID = ?";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
          "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.prepareStatement(sqlQuery);
      statement.setString(1, newFirstName);
      statement.setLong(2, studentId);

      // Execute the update and count the new rows
      int newRowCount = statement.executeUpdate();
      System.out.printf("%d row(s) updated.\n", newRowCount);
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
