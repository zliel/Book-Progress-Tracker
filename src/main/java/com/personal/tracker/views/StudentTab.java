package com.personal.tracker.views;

import com.personal.tracker.models.CompletedChapter;
import com.personal.tracker.models.Student;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.util.ListIterator;
import org.apache.commons.text.WordUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class StudentTab {
  public static Tab createStudentTab(TableView<Student> students,
                                     TableView<CompletedChapter> completedChapters,
                                     Session session) {
    // Make input fields for each necessary piece of information for the database
    TextField studentFirstNameField = new TextField();
    studentFirstNameField.setPromptText("Student First Name");
    studentFirstNameField.setTooltip(new Tooltip("Student First Name"));

    TextField studentLastNameField = new TextField();
    studentLastNameField.setPromptText("Student Last Name");
    studentLastNameField.setTooltip(new Tooltip("Student Last Name"));

    // This is a warning label that will show when the user tries to create
    Label warningLabel = new Label();
    warningLabel.getStyleClass().add("warning-label");

    FadeTransition fadeIn = new FadeTransition();
    fadeIn.setDuration(Duration.millis(100));
    fadeIn.setFromValue(0.0);
    fadeIn.setToValue(10d);
    fadeIn.setNode(warningLabel);

    FadeTransition fadeOut = new FadeTransition();
    fadeOut.setDuration(Duration.millis(100));
    fadeOut.setFromValue(10d);
    fadeOut.setToValue(0.0d);
    fadeOut.setDelay(Duration.seconds(2d));
    fadeOut.setNode(warningLabel);

    // Labels for testing action handlers on buttons for testing (uncomment these in testing)
//    Label firstNameLabel = new Label();
//    Label lastNameLabel = new Label();

    // Creating our submit button and giving it a style rule
    Button submitButton = new Button("Submit");
    submitButton.getStyleClass().add("submit-button");

    // Handle when the button is clicked
    submitButton.setOnAction(event -> {
      String newFirstName = WordUtils.capitalizeFully(studentFirstNameField.getText());
      String newLastName = WordUtils.capitalizeFully(studentLastNameField.getText());

      boolean canCreateStudent = true;

      Query studentQuery = session.createQuery("select id from Student s where " +
          "s.firstName=:firstName AND s.lastName=:lastName");
      studentQuery.setParameter("firstName", newFirstName);
      studentQuery.setParameter("lastName", newLastName);
      Long id = (Long) studentQuery.uniqueResult();
      // If the student ID exists in the database, don't create a new Student (throws a SQLException)
      if(newFirstName.isBlank() || newLastName.isBlank()) {
        System.err.println("FIELDS CANNOT BE BLANK");

        // Give the user a warning if the input fields are blank
        warningLabel.setText("Fields cannot be blank!");

        // Play the fade in and fade out animations for the warning
        fadeIn.play();
        fadeOut.play();
        canCreateStudent = false;

      } else if(id != null && id != 0) {
        System.err.println("THAT STUDENT ALREADY EXISTS");

        // Give the user a warning if they try to create a duplicate Student
        warningLabel.setText("That student already exists!");

        // Play the fade in and fade out animations for the warning
        fadeIn.play();
        fadeOut.play();
        canCreateStudent = false;
      }

      ListIterator<Student> studentListIterator = students.getItems().listIterator();

      while (studentListIterator.hasNext()) {
        Student currentStudent = studentListIterator.next();

        if (currentStudent.getFirstName().equalsIgnoreCase(newFirstName) && currentStudent.getLastName().equalsIgnoreCase(newLastName)) {
          warningLabel.setText("That student already exists!");

          fadeIn.play();
          fadeOut.play();

          canCreateStudent = false;
        }
      }

      if(canCreateStudent) {
        // These are the labels for testing
//        firstNameLabel.setText("First Name: " + studentFirstNameField.getText());
//        lastNameLabel.setText("Last Name: " + studentLastNameField.getText());
        Student newStudent = new Student(newFirstName, newLastName);
        session.save(newStudent);
        students.getItems().add(newStudent);
      }
    });

    // Creating a button to let the user delete things from the database
    Button deleteButton = new Button("Delete");
    deleteButton.getStyleClass().add("delete-button");

    // Adding delete functionality to our deleteButton
    deleteButton.setOnAction(e -> {
      Student selectedStudent = students.getSelectionModel().getSelectedItem();
      Long selectedStudentId = selectedStudent.getId();
      System.out.println("SELECTED STUDENT ID: " + selectedStudentId);

      students.getItems().remove(selectedStudent);
      // Loop through the CompletedChapters TableView's Items and remove the ones with the same
      // studentId as the student we're deleting
      ListIterator<CompletedChapter> completedChapterIterator = completedChapters.getItems().listIterator();

      while (completedChapterIterator.hasNext()) {
        CompletedChapter currentChapter = completedChapterIterator.next();

        if(selectedStudentId != null && currentChapter.getStudentId().equals(selectedStudentId)) {
          completedChapterIterator.remove();
        }
      }

      session.delete(selectedStudent);
    });

    // Create a VBox to store the Tab's contents
    final VBox vbox = new VBox();
    vbox.setSpacing(5);
    vbox.setPadding(new Insets(10, 10, 0, 10));

    // Make a new GridPane for our test tab
    GridPane inputGrid = new GridPane();

    // Set the padding of our GridPane
    inputGrid.setPadding(new Insets(10, 10, 10, 10));

    // Set the Max and Min height of our GridPane
    inputGrid.setMaxSize(3840, 2160);
    inputGrid.setMinSize(800, 400);

    // Set the spacing between elements of the GridPane
    inputGrid.setHgap(5);
    inputGrid.setVgap(5);

    // Add contents to our GridPane (uncomment the labels when testing)
    inputGrid.add(studentFirstNameField, 0, 0);
    //inputGrid.add(firstNameLabel, 0, 1);
    inputGrid.add(studentLastNameField, 1, 0);
    //inputGrid.add(lastNameLabel, 1, 1);
    inputGrid.add(submitButton, 2, 0);
    inputGrid.add(deleteButton, 3, 0);
    inputGrid.add(warningLabel, 4, 0);

    // Add the GridPane to our Vbox
    vbox.getChildren().addAll(students, inputGrid);

    Tab studentTab = new Tab("Students");
    studentTab.setClosable(false);

    studentTab.setContent(vbox);

    return studentTab;
  }
}
