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
    // Components for creating new chapters
    TextField studentFirstNameField = new TextField();
    studentFirstNameField.setPromptText("Student First Name");
    studentFirstNameField.setTooltip(new Tooltip("Student First Name"));

    TextField studentLastNameField = new TextField();
    studentLastNameField.setPromptText("Student Last Name");
    studentLastNameField.setTooltip(new Tooltip("Student Last Name"));


    // Warn the user if they try to enter invalid data
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

    // Handle creating a new student with the submit button
    Button submitButton = new Button("Submit");
    submitButton.getStyleClass().add("submit-button");
    submitButton.setOnAction(event -> {
      String newFirstName = WordUtils.capitalizeFully(studentFirstNameField.getText());
      String newLastName = WordUtils.capitalizeFully(studentLastNameField.getText());

      boolean canCreateStudent = true;

      // Query the database to ensure that the student being created doesn't already exist
      Query<Long> studentQuery = session.createQuery("select id from Student s where " +
          "s.firstName=:firstName AND s.lastName=:lastName");
      studentQuery.setParameter("firstName", newFirstName);
      studentQuery.setParameter("lastName", newLastName);
      Long id = studentQuery.uniqueResult();

      // If an input is blank or if the student already exists, don't create the new student
      if(newFirstName.isBlank() || newLastName.isBlank()) {
        System.err.println("FIELDS CANNOT BE BLANK");
        warningLabel.setText("Fields cannot be blank!");
        fadeIn.play();
        fadeOut.play();
        canCreateStudent = false;

      } else if(id != null && id != 0) {
        System.err.println("THAT STUDENT ALREADY EXISTS");
        warningLabel.setText("That student already exists!");
        fadeIn.play();
        fadeOut.play();
        canCreateStudent = false;
      }

      if(canCreateStudent) {
        Student newStudent = new Student(newFirstName, newLastName);
        session.save(newStudent);
        students.getItems().add(newStudent);
      }
    });

    // Handle deleting students with the delete button
    Button deleteButton = new Button("Delete");
    deleteButton.getStyleClass().add("delete-button");
    deleteButton.setOnAction(e -> {
      // Remove the selected student from both the table and the database, as well as any
      // completed chapters with a matching student id
      Student selectedStudent = students.getSelectionModel().getSelectedItem();
      Long selectedStudentId = selectedStudent.getId();
      students.getItems().remove(selectedStudent);

      ListIterator<CompletedChapter> completedChapterIterator = completedChapters.getItems().listIterator();
      while (completedChapterIterator.hasNext()) {
        CompletedChapter currentChapter = completedChapterIterator.next();

        if(currentChapter.getStudentId().equals(selectedStudentId)) {
          completedChapterIterator.remove();
        }
      }

      session.delete(selectedStudent);
    });


    final VBox vbox = new VBox();
    vbox.setSpacing(5);
    vbox.setPadding(new Insets(10, 10, 0, 10));

    // Create and style GridPane for our user input form
    GridPane inputGrid = new GridPane();
    inputGrid.setPadding(new Insets(10, 10, 10, 10));
    inputGrid.setMaxSize(3840, 2160);
    inputGrid.setMinSize(800, 400);
    inputGrid.setHgap(5);
    inputGrid.setVgap(5);

    // Add contents to our GridPane, and add our GridPane to our Vbox
    inputGrid.add(studentFirstNameField, 0, 0);
    inputGrid.add(studentLastNameField, 1, 0);
    inputGrid.add(submitButton, 2, 0);
    inputGrid.add(deleteButton, 3, 0);
    inputGrid.add(warningLabel, 4, 0);
    vbox.getChildren().addAll(students, inputGrid);

    Tab studentTab = new Tab("Students");
    studentTab.setClosable(false);
    studentTab.setContent(vbox);

    return studentTab;
  }
}
