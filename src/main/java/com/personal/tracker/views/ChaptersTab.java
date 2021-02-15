package com.personal.tracker.views;

import com.personal.tracker.controller.Add;
import com.personal.tracker.controller.Delete;
import com.personal.tracker.models.Chapter;
import com.personal.tracker.models.CompletedChapter;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.apache.commons.text.WordUtils;

import java.util.ListIterator;

public class ChaptersTab {
  public static Tab createChaptersTab(TableView<Chapter> chapters, TableView<CompletedChapter> completedChapters) {
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

      // Loop through the CompletedChapters TableView and remove the ones that match the chapter we're deleting
      ListIterator<CompletedChapter> completedChapterIterator = completedChapters.getItems().listIterator();

      while (completedChapterIterator.hasNext()) {
        CompletedChapter currentChapter = completedChapterIterator.next();

        if(currentChapter.chapterIsEqual(selectedChapter.getChapterKey(), currentChapter.getBookTitle())) {
          completedChapterIterator.remove();
        }
      }

      // Remove the Chapter from the database
      Delete.deleteChapter(selectedChapter.getChapterKey(), selectedChapter.getBookTitle());
    });

    // Handle when the button is clicked
    submitButton.setOnAction(event -> {
      // Retrieve the information from the input form
      long chapterNum = chapterNumberSpinner.getValue();
      String chapterTitle = WordUtils.capitalizeFully(chapterTitleField.getText());
      String bookTitle = WordUtils.capitalizeFully(bookTitleField.getText());
      boolean chapterExists = false;

      // Check if the chapter to be made already exists in the table
      ListIterator<Chapter> chapterListIterator = chapters.getItems().listIterator();

      while (chapterListIterator.hasNext()) {
        Chapter currentChapter = chapterListIterator.next();

        if (currentChapter.getChapterKey().equals(chapterNum) && currentChapter.getChapterTitle().equalsIgnoreCase(chapterTitle) && currentChapter.getBookTitle().equalsIgnoreCase(bookTitle)) {
          System.err.println("THAT CHAPTER ALREADY EXISTS");
          // Make a warning label for this to show the user
          chapterExists = true;
        } else if (currentChapter.getChapterKey().equals(chapterNum) && currentChapter.getBookTitle().equalsIgnoreCase(bookTitle)) {
          System.err.printf("CHAPTER %d IN BOOK %s ALREADY EXISTS\n", chapterNum, bookTitle);
          // Make a warning label for this to show the user
          chapterExists = true;
        }
      }

      if(Add.isChapterInputBlank(chapterNum, chapterTitle, bookTitle)) {
        System.err.println("FIELDS CANNOT BE BLANK");

        // Give the user a warning if the input fields are blank
        warningLabel.setText("Fields cannot be blank!");

        // Play the fade in and fade out animations for the warning
        fadeIn.play();
        fadeOut.play();

      } else if(!chapterExists) {
        // Labels for testing
//        chapterTitleLabel.setText("Chapter Title: " + chapterTitleField.getText());
//        chapterNumberLabel.setText("Chapter Number: " + chapterNumberSpinner.getValue());
//        bookTitleLabel.setText("Book Title: " + bookTitleField.getText());

        // Add the Chapter to the database
        Add.addChapter(chapterNum, chapterTitle, bookTitle);

        // Add the chapter to the TableView (to the underlying ObservableList)
        chapters.getItems().add(new Chapter(chapterNum, chapterTitle, bookTitle));
      } else {
        // Give the user a warning message when they try to create a duplicate Chapter
        warningLabel.setText("That chapter already exists!");

        // Play the fade in and fade out animations for the warning
        fadeIn.play();
        fadeOut.play();
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
    chapterInputForm.add(warningLabel, 5, 0);

    // Add the GridPane to our Vbox
    vbox.getChildren().addAll(chapters, chapterInputForm);

    Tab chapterTab = new Tab("Chapters");
    chapterTab.setClosable(false);

    chapterTab.setContent(vbox);

    return chapterTab;
  }
}
