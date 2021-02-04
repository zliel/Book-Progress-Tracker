package com.personal.tracker.views;

import com.personal.tracker.models.CompletedChapter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class CompletedChapterTab {
  public static Tab createCompletedChaptersTab(TableView<CompletedChapter> completedChapters) {
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

    // Create a VBox to store all of our elements in
    final VBox vbox = new VBox();
    vbox.setSpacing(5);
    vbox.setPadding(new Insets(10, 10, 0, 10));

    // Make a new GridPane for our user input form
    GridPane myTestStacks = new GridPane();

    // Set the padding of our GridPane
    myTestStacks.setPadding(new Insets(10, 10, 10, 10));

    // Set the Max and Min height of our GridPane
    myTestStacks.setMaxSize(3840, 2160);
    myTestStacks.setMinSize(800, 400);

    // Set the spacing between elements of the GridPane
    myTestStacks.setHgap(5);
    myTestStacks.setVgap(5);

    // Add contents to our GridPane
    myTestStacks.add(studentFirstNameField, 0, 0);
    myTestStacks.add(firstNameLabel, 0, 1);
    myTestStacks.add(studentLastNameField, 1, 0);
    myTestStacks.add(lastNameLabel, 1, 1);
    myTestStacks.add(submitButton, 2, 0);

    // Add the GridPane to our Vbox
    vbox.getChildren().addAll(completedChapters, myTestStacks);

    // Create our new Tab
    Tab completedChaptersTab = new Tab("Completed Chapters");
    // Set the content of our Tab to what's in the VBox
    completedChaptersTab.setContent(vbox);

    // Return our newly created Tab
    return completedChaptersTab;
  }
}
