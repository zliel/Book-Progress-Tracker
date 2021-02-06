package com.personal.tracker.views;

import com.personal.tracker.controller.Add;
import com.personal.tracker.controller.Delete;
import com.personal.tracker.models.Chapter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ChaptersTab {
  public static Tab createChaptersTab(TableView<Chapter> chapters) {
    //Make input fields for each necessary piece of information for the database
    TextField chapterTitleField = new TextField();
    chapterTitleField.setPromptText("Chapter Title");
    chapterTitleField.setTooltip(new Tooltip("Chapter Title"));

    Spinner<Integer> chapterNumberSpinner = new Spinner<>(1, 99, 1);
    chapterNumberSpinner.setEditable(true);
    chapterNumberSpinner.setTooltip(new Tooltip("Chapter Number"));

    TextField bookTitleField = new TextField();
    bookTitleField.setPromptText("Book Title");
    bookTitleField.setTooltip(new Tooltip("Book Title"));

    // Labels for testing
//    Label chapterTitleLabel = new Label();
//    Label chapterNumberLabel = new Label();
//    Label bookTitleLabel = new Label();

    // Creating our submit button and giving it a style rule
    Button submitButton = new Button("Add Row");
    submitButton.getStyleClass().add("submit-button");

    // Creating a button to let the user delete things from the database
    Button deleteButton = new Button("Delete");
    deleteButton.getStyleClass().add("delete-button");

    // Adding delete functionality to the deleteButton
    deleteButton.setOnAction(e -> {
      // Get the row that's selected in the TableView
      Chapter selectedChapter = chapters.getSelectionModel().getSelectedItem();

      // Remove the selected row from the TableView
      chapters.getItems().remove(selectedChapter);

      // Remove the Chapter from the database
      Delete.deleteChapter(selectedChapter.getChapterKey(), selectedChapter.getBookTitle());
    });

    // Handle when the button is clicked
    submitButton.setOnAction(new EventHandler<>() {
      @Override
      public void handle(ActionEvent event) {
        // Retrieve the information from the input form
        long chapterNum = chapterNumberSpinner.getValue();
        String chapterTitle = chapterTitleField.getText();
        String bookTitle = bookTitleField.getText();

        // Labels for testing
//        chapterTitleLabel.setText("Chapter Title: " + chapterTitleField.getText());
//        chapterNumberLabel.setText("Chapter Number: " + chapterNumberSpinner.getValue());
//        bookTitleLabel.setText("Book Title: " + bookTitleField.getText());

        // Add the Chapter to the database
        Add.addChapter(chapterNum, chapterTitle, bookTitle);

        // Add the chapter to the TableView (to the underlying ObservableList)
        chapters.getItems().add(new Chapter(chapterNum, chapterTitle, bookTitle));
      }
    });

    // Create a VBox to store the Tab's contents
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

    // Add contents to our GridPane (uncomment the labels for testing)
    chapterInputForm.add(chapterTitleField, 0, 0);
    //chapterInputForm.add(chapterTitleLabel, 0, 1);
    chapterInputForm.add(chapterNumberSpinner, 1, 0);
    //chapterInputForm.add(chapterNumberLabel, 1, 1);
    chapterInputForm.add(bookTitleField, 2, 0);
    //chapterInputForm.add(bookTitleLabel, 2, 1);
    chapterInputForm.add(submitButton, 3, 0);
    chapterInputForm.add(deleteButton, 4, 0);

    // Add the GridPane to our Vbox
    vbox.getChildren().addAll(chapters, chapterInputForm);

    Tab chapterTab = new Tab("Chapters");
    chapterTab.setContent(vbox);

    return chapterTab;
  }
}
