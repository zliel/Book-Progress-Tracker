package com.personal.tracker.views;

import com.personal.tracker.models.Chapter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ChaptersTab {
  public static Tab createChaptersTab(TableView<Chapter> chapters) {
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
    vbox.getChildren().addAll(chapters, myTestStacks);

    Tab chapterTab = new Tab("Chapters");
    chapterTab.setContent(vbox);

    return chapterTab;
  }
}
