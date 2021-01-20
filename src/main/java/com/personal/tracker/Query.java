package com.personal.tracker;

// Imports
import com.personal.tracker.models.Chapter;
import com.personal.tracker.models.CompletedChapter;
import com.personal.tracker.models.Student;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class will handle any queries related to retrieving information.
 *
 * @author Zac Liel
 * @version 0.1
 * @since 12-31-2020
 */
public class Query {

  /**
   * This method makes a query to the embedded database, retrieving all of the chapters completed
   * by the student.
   *
   * @param firstName The first name of the student we're searching for.
   * @param lastName The last name of the student we're searching for.
   */
  public static void getCompletedChapters(String firstName, String lastName) {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    PreparedStatement statement;

    // Initialize the string to hold the query
    String sqlQuery =
      "SELECT sprog.COMPLETION_DATE, c.CHAPTER_NUMBER, c.CHAPTER_TITLE, c.BOOK " +
        "FROM chapter AS c, student_progress AS sprog " +
        "WHERE " +
        "(SELECT STUDENT_ID FROM student WHERE FIRST_NAME = ? " + " AND LAST_NAME = ? ) " + "= sprog.STUDENT_ID " +
        "AND c.CHAPTER_NUMBER = sprog.CHAPTER_NUMBER";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker", "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.prepareStatement(sqlQuery);
      statement.setString(1, firstName);
      statement.setString(2, lastName);

      // Make a ResultSet object to store the query results
      ResultSet results = statement.executeQuery();

      // Header containing the student's name
      System.out.printf("\n%s %s\n", firstName.toUpperCase(), lastName.toUpperCase());

      while (results.next()) {
        // Retrieve the data from each column and print out the result
        System.out.printf("%s \t", results.getDate("COMPLETION_DATE").toString());
        System.out.printf("%d \t|", results.getLong("CHAPTER_NUMBER"));
        System.out.printf("%-55s \t|", results.getString("CHAPTER_TITLE"));
        System.out.printf("%10s \n", results.getString("BOOK"));
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

  /** This method returns all of the rows in the Chapters table in the database */
  public static ArrayList<Chapter> listChapters() {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    Statement statement;

    // Make an ArrayList to store the chapters
    ArrayList<Chapter> chapters = new ArrayList<>();
    Long chapterNum;
    String chapterTitle;
    String bookTitle;

    // Initialize the string to hold the query
    String sqlQuery = "SELECT CHAPTER_NUMBER, CHAPTER_TITLE, BOOK FROM CHAPTER GROUP BY BOOK, " +
        "CHAPTER_NUMBER";

    // Try block (because jdbc methods can throw SQLException exceptions
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker", "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.createStatement();


      // Make a ResultSet object to store the query results
      ResultSet results = statement.executeQuery(sqlQuery);

      // Print out the header
      System.out.printf("%s \t", "NUMBER");
      System.out.printf("%-55s \t", "CHAPTER TITLE");
      System.out.printf("%5s \n", "BOOK");

      while (results.next()) {
        // Retrieve the data from each column and print out the result
        chapterNum = results.getLong("CHAPTER_NUMBER");
        chapterTitle = results.getString("CHAPTER_TITLE");
        bookTitle = results.getString("BOOK");
        chapters.add(new Chapter(chapterNum, chapterTitle, bookTitle));
      }

      // Cleanup - close the ResultSet object to free up resources
      results.close();

      // Cleanup - close the Statement object to free up resources
      statement.close();

      // Cleanup - close the Connection object to free up resources
      conn.close();
      return chapters;

    } catch (SQLException sqle) {
      // If there's an Exception, print out the stack trace so we can figure out what's up
      sqle.printStackTrace();
      return chapters;
    }
  }

  /** This method returns all of the rows from the Student table in the database */
  public static ArrayList<Student> listStudents() {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    Statement statement;

    // Make an arrayList to store the students
    ArrayList<Student> studentResults = new ArrayList<>();

    // Initialize the string to hold the query
    String sqlQuery = "SELECT STUDENT_ID, FIRST_NAME, LAST_NAME FROM STUDENT";

    // Try block (because jdbc methods can throw SQLException exceptions
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker", "sa", "");

      // Create variables for the Student properties
      Long studentId;
      String studentFirstName;
      String studentLastName;

      // Make a preparedStatement and fill in the blanks
      statement = conn.createStatement();


      // Make a ResultSet object to store the query results
      ResultSet results = statement.executeQuery(sqlQuery);



      while (results.next()) {
        studentId = results.getLong(1);
        studentFirstName = results.getString(2);
        studentLastName = results.getString(3);
        studentResults.add(new Student(studentId, studentFirstName, studentLastName));
      }

      // Cleanup - close the ResultSet object to free up resources
      results.close();

      // Cleanup - close the Statement object to free up resources
      statement.close();

      // Cleanup - close the Connection object to free up resources
      conn.close();

      return studentResults;

    } catch (SQLException sqle) {
      // If there's an Exception, print out the stack trace so we can figure out what's up
      sqle.printStackTrace();
      return studentResults;
    }
  }

  public static ArrayList<CompletedChapter> listCompletedChapters() {
    // Initialize the Connection and PreparedStatement objects
    Connection conn;
    Statement statement;

    // Make an ArrayList to hold the data from the database
    ArrayList<CompletedChapter> completedChapters = new ArrayList<>();
    Long studentId;
    Long chapterId;
    Date completionDate;
    String bookTitle;
    String chapterTitle;

    // Initialize the string to hold the query
    String sqlQuery =
        "SELECT STUDENT_ID, CHAPTER_NUMBER, COMPLETION_DATE, BOOK, CHAPTER_TITLE " +
        "FROM STUDENT_PROGRESS";

    // Try block (because jdbc methods can throw SQLException exceptions)
    try {
      // Make a connection to the database
      conn = DriverManager.getConnection("jdbc:h2:~/h2Databases/StudentTrackerDB/StudentTracker", "sa", "");

      // Make a preparedStatement and fill in the blanks
      statement = conn.createStatement();


      // Make a ResultSet object to store the query results
      ResultSet results = statement.executeQuery(sqlQuery);

      while (results.next()) {
        // Retrieve the data from each column and print out the result
        studentId = results.getLong("STUDENT_ID");
        chapterId = results.getLong("CHAPTER_NUMBER");
        completionDate = results.getDate("COMPLETION_DATE");
        bookTitle = results.getString("BOOK");
        chapterTitle = results.getString("CHAPTER_TITLE");
        completedChapters.add(new CompletedChapter(studentId, chapterId, completionDate,
            bookTitle, chapterTitle));
//        System.out.printf("%s \t", results.getDate("COMPLETION_DATE").toString());
//        System.out.printf("%d \t|", results.getLong("CHAPTER_NUMBER"));
//        System.out.printf("%-55s \t|", results.getString("CHAPTER_TITLE"));
//        System.out.printf("%10s \n", results.getString("BOOK"));
      }

      // Cleanup - close the ResultSet object to free up resources
      results.close();

      // Cleanup - close the Statement object to free up resources
      statement.close();

      // Cleanup - close the Connection object to free up resources
      conn.close();
      return completedChapters;

    } catch (SQLException sqle) {
      // If there's an Exception, print out the stack trace so we can figure out what's up
      sqle.printStackTrace();
      return completedChapters;
    }
  }
}
