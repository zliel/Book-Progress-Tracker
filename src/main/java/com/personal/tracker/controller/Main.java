package com.personal.tracker.controller;

import java.util.Scanner;
// DEPRECATED - WON'T BE USED IN THE GUI VERSION

/** This class executes the program, handling user input. */
public class Main {
  public static void primary(String[] args) {
    // Initialize Scanner object for user input
    Scanner input = new Scanner(System.in);

    System.out.println("Hello! What would you like to do today? Enter here:  ");

    String userCmd = input.next();

    // Handle user input
    switch (userCmd) {
      case "help" -> help();
      case "add" -> add(input);
      case "list" -> list(input);
      case "delete" -> delete(input);
    }
    // Close the scanner when we're done
    input.close();
  }

  /** This method handles the "help" command, showing the user what possible commands there are */
  public static void help() {
    // Print out all available commands
    System.out.println("Possible commands: ");
    System.out.println("\tadd: Add a student, chapter, or completed chapter to the tracker.");
    System.out.println("\n\tlist: List all chapters.");
    System.out.println(
        "\n\tdelete: Delete a student, chapter, or completed chapter from the tracker.");
    // main(new String[]{""}); //uncommenting this will make help() restart the program; it'd
    // need to be tested
  }

  /**
   * This method handles the "add" command, letting the user add chapters, students, or completed
   * chapters based on input.
   *
   * @param addScanner The Scanner object passed in by the main() method.
   */
  public static void add(Scanner addScanner) {
    String firstName;
    String lastName;
    String addInput;

    System.out.println("Sick, so what do you wanna add? ");
    addInput = addScanner.next();

    switch (addInput) {
      case "student" -> {
        // Get user input for the student's name
        System.out.println("What is the student's name? First please: ");
        firstName = addScanner.next();
        System.out.println("Last name please: ");
        lastName = addScanner.next();

        Add.addStudent(firstName, lastName);
      }

      case "chapter" -> {
        System.out.println("would you like to add a new chapter or a completed chapter? ");
        String answer = addScanner.next();
        if (answer.equalsIgnoreCase("new")) {
          String book;
          int chapterId;
          String chapterTitle;

          System.out.println("Which book is the chapter from? ");
          addScanner.nextLine();
          book = addScanner.nextLine();

          System.out.println("What is the chapter number? ");
          chapterId = addScanner.nextInt();

          System.out.println("What is the chapter name? ");
          addScanner.nextLine();
          chapterTitle = addScanner.nextLine();

          Add.addChapter(chapterId, chapterTitle, book);

        } else if (answer.equals("completed")) {
          // Get the student's first and last name
          System.out.println("What is the student's name? First name please: ");
          firstName = addScanner.next();
          System.out.println("Last name please: ");
          lastName = addScanner.next();

          System.out.println("Book name please: ");
          addScanner.nextLine();
          String book = addScanner.nextLine();
          System.out.println("Chapter number please: ");
          int chapterId = addScanner.nextInt();

          Add.addCompletedChapter(firstName, lastName, chapterId, book);
        }
      }
    }
  }

  /**
   * This method handles the "delete" command, letting the user remove chapters from the database.
   *
   * @param deleteScanner The Scanner object passed in by the main() method.
   */
  public static void delete(Scanner deleteScanner) {
    // Get user input for what they want to delete (currently only "chapter" works)
    System.out.println("What would you like to delete? ");
    String deleteInput = deleteScanner.next();

    if (deleteInput.equals("chapter")) {
      // Get input for which chapter should be deleted
      String book;
      Long chapterId;

      System.out.println("Enter the title of the book the chapter is from: ");
      deleteScanner.nextLine();
      book = deleteScanner.nextLine();
      System.out.println("Enter the chapter number of the chapter you'd like to delete: ");
      chapterId = deleteScanner.nextLong();

      Delete.deleteChapter(chapterId, book);
    } else if (deleteInput.equalsIgnoreCase("student")) {

      System.out.println("Which student would you like to delete? Enter their first name: ");
      String studentFirstName = deleteScanner.next();
      System.out.println("Enter their last name: ");
      String studentLastName = deleteScanner.next();
      Long studentId = Delete.getStudentId(studentFirstName, studentLastName);
      if (studentId == null) {
        System.out.println("Sorry, the student wasn't found.");
      } else {
        Delete.deleteStudent(studentId);
      }
    } else if (deleteInput.equalsIgnoreCase("completed")) {
      String firstName;
      String lastName;
      Long studentId;
      String book;
      Long chapterNumber;

      System.out.println("Student's first name, please: ");
      firstName = deleteScanner.next();

      System.out.println("Student's last name, please: ");
      lastName = deleteScanner.next();

      studentId = Delete.getStudentId(firstName, lastName);

      System.out.println("Which book is the chapter from? ");
      deleteScanner.nextLine();
      book = deleteScanner.nextLine();

      System.out.println("Enter the chapter number of the chapter you'd like to delete: ");
      chapterNumber = deleteScanner.nextLong();

      Delete.deleteCompletedChapter(studentId, book, chapterNumber);
    }
  }

  /**
   * This method handles the "list" command, letting the user list either the students or chapters
   * in the database.
   *
   * @param listScanner The Scanner object passed in by the main() method
   */
  public static void list(Scanner listScanner) {
    // Get user input for what they would like to list
    System.out.println("Would you like to list the chapters or students?");
    String answer = listScanner.next();

    // Handle the response accordingly
    if (answer.equalsIgnoreCase("chapters")) {
      Query.listChapters();
    } else if (answer.equalsIgnoreCase("students")) {
      Query.listStudents();
    } else if (answer.equalsIgnoreCase("completed")) {
      System.out.println("Enter the student's first name: ");
      String firstName = listScanner.next();
      System.out.println("Enter the student's last name: ");
      String lastName = listScanner.next();
      Query.getCompletedChapters(firstName, lastName);
    }
  }
}
