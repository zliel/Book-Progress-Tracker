package com.personal.tracker.views;

import com.personal.tracker.controller.Add;
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
    TextField chapterTitleField = new TextField();
    chapterTitleField.setPromptText("Chapter Title");

    //TODO possibly remove this and use the Spinner instead
    TextField chapterNumberField = new TextField();
    chapterNumberField.setPromptText("Chapter Number (whole numbers only)");

    Spinner<Long> chapterNumberSpinner = new Spinner<>(1, 99, 1);
    chapterNumberSpinner.setPromptText("Chapter Number");

    TextField bookTitleField = new TextField();
    bookTitleField.setPromptText("Book Title");

    // Labels for testing action handlers on buttons
    Label chapterTitleLabel = new Label();
    Label chapterNumberLabel = new Label();
    Label bookTitleLabel = new Label();

    // Creating our submit button and giving it a style rule
    Button submitButton = new Button("Add Row");
    submitButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white");

    // Handle when the button is clicked
    submitButton.setOnAction(new EventHandler<>() {
      @Override
      public void handle(ActionEvent event) {
        // Eventually this will add a new row to the database
        //Add.addChapter(chapterNumberSpinner.getValue(), chapterTitleField.getText(),bookTitleField.getText());
        chapterNumberLabel.setText("Chapter Number: " + chapterNumberSpinner.getValue());
        chapterTitleLabel.setText("Chapter Title: " + chapterTitleField.getText());
        bookTitleLabel.setText("Book: " + bookTitleField.getText());
      }
    });

    // TODO TESTING
    final VBox vbox = new VBox();
    vbox.setSpacing(5);
    vbox.setPadding(new Insets(10, 10, 0, 10));

    // Make a new GridPane for our test tab
    GridPane chapterInputForm = new GridPane();

    // Set the padding of our GridPane
    chapterInputForm.setPadding(new Insets(10, 10, 10, 10));

    // Set the Max and Min height of our GridPane
    chapterInputForm.setMaxSize(3840, 2160);
    chapterInputForm.setMinSize(800, 400);

    // Set the spacing between elements of the GridPane
    chapterInputForm.setHgap(5);
    chapterInputForm.setVgap(5);

    // Add contents to our GridPane
    chapterInputForm.add(chapterTitleField, 0, 0);
    chapterInputForm.add(chapterTitleLabel, 0, 1);
    chapterInputForm.add(chapterNumberSpinner, 1, 0);
    chapterInputForm.add(chapterNumberLabel, 1, 1);
    chapterInputForm.add(bookTitleField, 2, 0);
    chapterInputForm.add(bookTitleLabel, 2, 1);
    chapterInputForm.add(submitButton, 3, 0);

    // Add the GridPane to our Vbox
    vbox.getChildren().addAll(chapters, chapterInputForm);

    Tab chapterTab = new Tab("Chapters");
    chapterTab.setContent(vbox);

    return chapterTab;
  }
}
