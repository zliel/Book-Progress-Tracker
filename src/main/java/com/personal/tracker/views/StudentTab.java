package com.personal.tracker.views;

import com.personal.tracker.controller.Add;
import com.personal.tracker.controller.Delete;
import com.personal.tracker.models.CompletedChapter;
import com.personal.tracker.models.Student;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.util.ListIterator;

public class StudentTab {
  public static Tab createStudentTab(TableView<Student> students, TableView<CompletedChapter> completedChapters) {
    //Make input fields for each necessary piece of information for the database
    TextField studentFirstNameField = new TextField();
    studentFirstNameField.setPromptText("Student First Name");
    studentFirstNameField.setTooltip(new Tooltip("Student First Name"));

    TextField studentLastNameField = new TextField();
    studentLastNameField.setPromptText("Student Last Name");
    studentLastNameField.setTooltip(new Tooltip("Student Last Name"));

    // Labels for testing action handlers on buttons for testing (uncomment these in testing)
//    Label firstNameLabel = new Label();
//    Label lastNameLabel = new Label();

    // Creating our submit button and giving it a style rule
    Button submitButton = new Button("Submit");
    submitButton.getStyleClass().add("submit-button");

    // Handle when the button is clicked
    submitButton.setOnAction(new EventHandler<>() {
      @Override
      public void handle(ActionEvent event) {
        String newFirstName = studentFirstNameField.getText();
        String newLastName = studentLastNameField.getText();

        // These are the labels for testing
//        firstNameLabel.setText("First Name: " + studentFirstNameField.getText());
//        lastNameLabel.setText("Last Name: " + studentLastNameField.getText());

        Add.addStudent(newFirstName, newLastName);
        Long studentId = Delete.getStudentId(newFirstName, newLastName);
        students.getItems().add(new Student(studentId, newFirstName, newLastName));
      }
    });

    // Creating a button to let the user delete things from the database
    Button deleteButton = new Button("Delete");
    deleteButton.getStyleClass().add("delete-button");

    // Adding delete functionality to our deleteButton
    deleteButton.setOnAction(e -> {
      Student selectedStudent = students.getSelectionModel().getSelectedItem();
      Long selectedStudentId = Delete.getStudentId(selectedStudent.getFirstName(), selectedStudent.getLastName());

      students.getItems().remove(selectedStudent);
      // Loop through the CompletedChapters TableView's Items and remove the ones with the same
      // studentId as the student we're deleting
      ListIterator<CompletedChapter> completedChapterIterator = completedChapters.getItems().listIterator();

      while (completedChapterIterator.hasNext()) {
        CompletedChapter currentChapter = completedChapterIterator.next();

        if(currentChapter.studentIdIsEqual(selectedStudentId)) {
          completedChapterIterator.remove();
        }
      }

      Delete.deleteStudent(selectedStudentId);
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

    // Add the GridPane to our Vbox
    vbox.getChildren().addAll(students, inputGrid);

    Tab studentTab = new Tab("Students");

    studentTab.setContent(vbox);

    return studentTab;
  }
}
