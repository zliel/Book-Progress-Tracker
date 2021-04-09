package com.personal.tracker.views;

import com.personal.tracker.models.Chapter;
import com.personal.tracker.models.CompletedChapter;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.apache.commons.text.WordUtils;
import org.hibernate.Session;

import java.util.ListIterator;

public class ChaptersTab {
  public static Tab createChaptersTab(TableView<Chapter> chapters,
                                      TableView<CompletedChapter> completedChapters,
                                      Session session) {
    // Components for creating new chapters
    TextField chapterTitleField = new TextField();
    chapterTitleField.setPromptText("Chapter Title");
    chapterTitleField.setTooltip(new Tooltip("Chapter Title"));

    Spinner<Integer> chapterNumberSpinner = new Spinner<>(1, 99, 1);
    chapterNumberSpinner.setEditable(true);
    chapterNumberSpinner.setTooltip(new Tooltip("Chapter Number"));

    TextField bookTitleField = new TextField();
    bookTitleField.setPromptText("Book Title");
    bookTitleField.setTooltip(new Tooltip("Book Title"));

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

    // Handle creating a new chapter with the submit button
    Button submitButton = new Button("Add Row");
    submitButton.getStyleClass().add("submit-button");

    submitButton.setOnAction(event -> {
      // Retrieve the information from the input form and check if the chapter to be made already
      // exists
      long chapterNum = chapterNumberSpinner.getValue();
      String chapterTitle = WordUtils.capitalizeFully(chapterTitleField.getText());
      String bookTitle = WordUtils.capitalizeFully(bookTitleField.getText());
      boolean chapterExists = false;

      ListIterator<Chapter> chapterListIterator = chapters.getItems().listIterator();

      while (chapterListIterator.hasNext()) {
        Chapter currentChapter = chapterListIterator.next();

        // If the input data matches a chapter that already exists, warn the user
        if (currentChapter.getChapterNum().equals(chapterNum) && currentChapter.getChapterTitle().equalsIgnoreCase(chapterTitle) && currentChapter.getBookTitle().equalsIgnoreCase(bookTitle)) {
          System.err.println("THAT CHAPTER ALREADY EXISTS");

          chapterExists = true;
        } else if (currentChapter.getChapterNum().equals(chapterNum) && currentChapter.getBookTitle().equalsIgnoreCase(bookTitle)) {
          System.err.printf("CHAPTER %d IN BOOK %s ALREADY EXISTS\n", chapterNum, bookTitle);

          chapterExists = true;
        }
      }

      if (chapterNum <= 0 || chapterTitle.isBlank() || bookTitle.isBlank()) {
        System.err.println("FIELDS CANNOT BE BLANK");
        warningLabel.setText("Fields cannot be blank");
        fadeIn.play();
        fadeOut.play();


      } else if (chapterExists) {
        warningLabel.setText("That chapter already exists");
        fadeIn.play();
        fadeOut.play();

      } else {

        Chapter newChapter = new Chapter(chapterNum, chapterTitle, bookTitle);
        session.save(newChapter);
        session.getTransaction().commit();
        session.beginTransaction();

        chapters.getItems().add(newChapter);
      }
    });


    Button deleteButton = new Button("Delete");
    deleteButton.getStyleClass().add("delete-button");

    deleteButton.setOnAction(e -> {
      // Remove the selected chapter from the table and database
      Chapter selectedChapter = chapters.getSelectionModel().getSelectedItem();
      chapters.getItems().remove(selectedChapter);
      session.delete(selectedChapter);

      // Remove completedChapters that matched the selected chapter from the completed chapters
      // table and the database
      ListIterator<CompletedChapter> completedChapterIterator = completedChapters.getItems().listIterator();

      while (completedChapterIterator.hasNext()) {
        CompletedChapter currentChapter = completedChapterIterator.next();

        if(currentChapter.getChapterNumber().equals(selectedChapter.getChapterNum()) && currentChapter.getBookTitle().equals(selectedChapter.getBookTitle())) {
          completedChapterIterator.remove();
          session.delete(currentChapter);
        }
      }

      session.getTransaction().commit();
      session.beginTransaction();
    });


    final VBox vbox = new VBox();
    vbox.setSpacing(5);
    vbox.setPadding(new Insets(10, 10, 0, 10));

    // Make and style our gridpane, which contains the inputs for adding a new chapter
    GridPane chapterInputForm = new GridPane();
    chapterInputForm.setPadding(new Insets(10, 10, 10, 10));
    chapterInputForm.setMaxSize(3840, 2160);
    chapterInputForm.setMinSize(800, 400);
    chapterInputForm.setHgap(5);
    chapterInputForm.setVgap(5);

    // Add contents to our GridPane, and add our GridPane to our Vbox
    chapterInputForm.add(chapterTitleField, 0, 0);
    chapterInputForm.add(chapterNumberSpinner, 1, 0);
    chapterInputForm.add(bookTitleField, 2, 0);
    chapterInputForm.add(submitButton, 3, 0);
    chapterInputForm.add(deleteButton, 4, 0);
    chapterInputForm.add(warningLabel, 5, 0);
    vbox.getChildren().addAll(chapters, chapterInputForm);

    Tab chapterTab = new Tab("Chapters");
    chapterTab.setClosable(false);
    chapterTab.setContent(vbox);

    return chapterTab;
  }
}
