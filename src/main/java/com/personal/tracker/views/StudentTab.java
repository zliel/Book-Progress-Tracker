package com.personal.tracker.views;

import com.personal.tracker.models.Student;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class StudentTab {
  public static Tab createStudentTab(TableView<Student> students) {
    //Make text fields for each tab
    TextField studentFirstNameField = new TextField("Student First Name");
    TextField studentLastNameField = new TextField("Student Last Name");

    // Labels for testing action handlers on buttons
    Label firstNameLabel = new Label();
    Label lastNameLabel = new Label();

    // Creating our submit button and giving it a style rule
    Button submitButton = new Button("Submit");
    submitButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white");

    // Handle when the button is clicked
    submitButton.setOnAction(new EventHandler<>() {
      @Override
      public void handle(ActionEvent event) {
        // Eventually this will add a new row to the database
        firstNameLabel.setText("First Name: " + studentFirstNameField.getText());
        lastNameLabel.setText("Last Name: " + studentLastNameField.getText());
      }
    });

    // TODO TESTING
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

    // Add contents to our GridPane
    inputGrid.add(studentFirstNameField, 0, 0);
    inputGrid.add(firstNameLabel, 0, 1);
    inputGrid.add(studentLastNameField, 1, 0);
    inputGrid.add(lastNameLabel, 1, 1);
    inputGrid.add(submitButton, 2, 0);

    // Add the GridPane to our Vbox
    vbox.getChildren().addAll(students, inputGrid);

    Tab studentTab = new Tab("Students");

    studentTab.setContent(vbox);

    return studentTab;
  }
}
