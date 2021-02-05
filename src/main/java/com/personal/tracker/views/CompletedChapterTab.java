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
    TextField studentFirstNameField = new TextField();
    studentFirstNameField.setPromptText("Student First Name");

    TextField studentLastNameField = new TextField();
    studentLastNameField.setPromptText("Student Last Name");

    TextField bookTitleField = new TextField();
    bookTitleField.setPromptText("Chapter Title");

    Spinner<Long> chapterNumberSpinner = new Spinner<>(1, 99, 1);
    chapterNumberSpinner.setPromptText("Chapter Number");

    // Labels for testing action handlers on buttons
    Label firstNameLabel = new Label();
    Label lastNameLabel = new Label();
    Label bookTitleLabel = new Label();
    Label chapterNumberLabel = new Label();

    // Creating our submit button and giving it a style rule
    Button submitButton = new Button("Add Row");
    submitButton.getStyleClass().add("submit-button");

    // Handle when the button is clicked
    submitButton.setOnAction(new EventHandler<>() {
      @Override
      public void handle(ActionEvent event) {
        // Eventually this will add a new row to the database
        firstNameLabel.setText("First Name: " + studentFirstNameField.getText());
        lastNameLabel.setText("Last Name: " + studentLastNameField.getText());
        bookTitleLabel.setText("Book Title: " + bookTitleField.getText());
        chapterNumberLabel.setText("Chapter Number: " + chapterNumberSpinner.getValue());
      }
    });

    // Create a VBox to store all of our elements in
    final VBox vbox = new VBox();
    vbox.setSpacing(5);
    vbox.setPadding(new Insets(10, 10, 0, 10));

    // Make a new GridPane for our user input form
    GridPane completedChapterInputForm = new GridPane();

    // Set the padding of our GridPane
    completedChapterInputForm.setPadding(new Insets(10, 10, 10, 10));

    // Set the Max and Min height of our GridPane
    completedChapterInputForm.setMaxSize(3840, 2160);
    completedChapterInputForm.setMinSize(800, 400);

    // Set the spacing between elements of the GridPane
    completedChapterInputForm.setHgap(5);
    completedChapterInputForm.setVgap(5);

    // Add contents to our GridPane
    completedChapterInputForm.add(studentFirstNameField, 0, 0);
    completedChapterInputForm.add(firstNameLabel, 0, 1);
    completedChapterInputForm.add(studentLastNameField, 1, 0);
    completedChapterInputForm.add(lastNameLabel, 1, 1);
    completedChapterInputForm.add(bookTitleField, 2, 0);
    completedChapterInputForm.add(bookTitleLabel, 2, 1);
    completedChapterInputForm.add(chapterNumberSpinner, 3, 0);
    completedChapterInputForm.add(chapterNumberLabel, 3, 1);
    completedChapterInputForm.add(submitButton, 4, 0);

    // Add the GridPane to our Vbox
    vbox.getChildren().addAll(completedChapters, completedChapterInputForm);

    // Create our new Tab
    Tab completedChaptersTab = new Tab("Completed Chapters");
    // Set the content of our Tab to what's in the VBox
    completedChaptersTab.setContent(vbox);

    // Return our newly created Tab
    return completedChaptersTab;
  }
}
