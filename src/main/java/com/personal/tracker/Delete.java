package com.personal.tracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class handles all queries related to deleting something from the database
 */
public class Delete {

  /**
   * This method deletes a chapter from the Chapter table, as well as the Student Progress table
   * if it's been completed
   *
   * @param chapterId The ID of the chapter to be removed
   */
  public static void deleteChapter(Long chapterId) {
    // Eventually I'll need to add another arg to include book id when the book table is made
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String sqlDeleteQuery = "DELETE FROM CHAPTER WHERE CHAPTER_ID = ?";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker",
        "sa", "");

      // Prepare the first query
      statement = conn.prepareStatement(sqlDeleteQuery);
      statement.setLong(1, chapterId);

      // Execute the update and count the new rows
      int newRowCount = statement.executeUpdate();
      System.out.printf("Deleting chapter %d from the Chapter table...\n", chapterId);
      System.out.printf("%d row(s) deleted.\n", newRowCount);
      System.out.println("Chapter Table successfully updated!\n");

      // Prepare the second query
      sqlDeleteQuery = "DELETE FROM STUDENT_PROGRESS WHERE CHAPTER_ID = ?";
      statement = conn.prepareStatement(sqlDeleteQuery);
      statement.setLong(1, chapterId);

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
}
