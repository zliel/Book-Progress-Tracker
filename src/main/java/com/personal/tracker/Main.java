package com.personal.tracker;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    // Initialize Scanner object for user input
    Scanner input = new Scanner( System.in );

    System.out.println("Hello! What would you like to do today? Enter here:  ");
    // Sample: add
    // call add

    String userCmd = input.next();

    switch(userCmd) {
      case "help" -> help();
      case "add" -> add(input);
      case "list" -> Query.listChapters();
      case "delete" -> delete(input);
    }
    input.close();
  }

  public static void help() {
    // Print out all available commands
    // Add chapter
    // Add completed chapter
    // Add student
    // Find student
    // list chapters
    System.out.println("Possible commands: ");
    System.out.println("add: Add a student, chapter, or completed chapter to the tracker.");
    System.out.println("\nlist: List all chapters.");
    System.out.println("\ndelete: (not implemented yet) Delete a student, chapter, or completed " +
      "chapter from the tracker.");
  }

  public static void add(Scanner addScanner) {
    String firstName;
    String lastName;
    String addInput;

    System.out.println("Sick, so what do you wanna add? ");
    addInput = addScanner.next();

    while (addScanner.hasNext()) {
      addInput += addScanner.next();
    }
    // student, chapter, or completed chapter
    // do something when we have a function for adding a chapter
    switch (addInput) {
      case "student" -> {
        // Get user input for the student's name
        System.out.println("What is the student's name? First please: ");
        firstName = addScanner.next();
        System.out.println("Last name please: ");
        lastName = addScanner.next();

        Query.getCompletedChapters(firstName, lastName);
      }
      // CHAPTER AND COMPLETED CHAPTER DON'T WORK (IT'S DOING THE NEWLINE EATING THING)
      case "chapter" -> {
        System.out.println("Not implemented yet, sorry ;~;");
        addScanner.next();
        addScanner.close();
      }
      case "completed chapter" -> {
        // Get the student's first and last name
        System.out.println("What is the student's name? First name please: ");
        firstName = addScanner.next();
        System.out.println("Last name please: ");
        lastName = addScanner.next();

        System.out.println("Chapter number please: ");
        Long chapterId = addScanner.nextLong();
        InsertCompletedChapter.addCompletedChapter(firstName, lastName, chapterId);
      }
    }
  }

  public static void delete(Scanner deleteScanner) {
    // call some delete method from the class
    System.out.println("What would you like to delete? ");
    String deleteInput = deleteScanner.next();

    // I'll have to make queries to remove students from the students table, but also from the
    // student_progress table (same for chapters)
  }
}