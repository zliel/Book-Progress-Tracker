package com.personal.tracker.views;

import com.personal.tracker.models.Chapter;
import com.personal.tracker.models.CompletedChapter;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.apache.commons.text.WordUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ListIterator;

public class CompletedChapterTab {

  public static Tab createCompletedChaptersTab(TableView<CompletedChapter> completedChapters,
                                               TableView<Chapter> chapters,
                                               Session session) {
    // Generate the list of books from the chapter table
    ObservableList<String> bookList = generateBookList(chapters);

    // Components for creating new completed chapters
    TextField studentFirstNameField = new TextField();
    studentFirstNameField.setPromptText("Student First Name");

    TextField studentLastNameField = new TextField();
    studentLastNameField.setPromptText("Student Last Name");

    ComboBox<String> bookTitleField = new ComboBox<>();
    bookTitleField.setItems(bookList);
    try {
      bookTitleField.setValue(bookList.get(0));
    } catch (IndexOutOfBoundsException exception) {
      System.err.println("The chapters table is empty!");
    }

    Spinner<Integer> chapterNumberSpinner = new Spinner<>(1, 99, 1);
    chapterNumberSpinner.setTooltip(new Tooltip("Chapter Number"));
    chapterNumberSpinner.setEditable(true);

    Label cannotEditLabel = new Label();
    cannotEditLabel.setText("Note: Cells in the table above can't be directly edited.");

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

    // Handle creating a new completed chapter with the submit button
    Button submitButton = new Button("Add Row");
    submitButton.getStyleClass().add("submit-button");

    submitButton.setOnAction(event -> {
      // Get the data for the new completed chapter from the input fields
      Date date = Date.valueOf(LocalDate.now());
      String studentFirstName = WordUtils.capitalizeFully(studentFirstNameField.getText());
      String studentLastName = WordUtils.capitalizeFully(studentLastNameField.getText());
      String bookTitle = bookTitleField.getValue();
      long chapterNum = chapterNumberSpinner.getValue();

      // Warn the user if one of the input fields is blank or if chapterNum <= 0
      if (studentFirstName.isBlank() || studentLastName.isBlank() || bookTitle.isBlank() || chapterNum <= 0) {
        warningLabel.setText("Fields cannot be blank and chapter number cannot be less than 0");
        fadeIn.play();
        fadeOut.play();

      } else {
        try {
          // Query the database for the chapterTitle and make sure it exists
          Query<?> completedChapterQuery = session.createQuery("select chapterTitle from " +
              "Chapter c " +
              "where " +
              "c.chapterNum=:chapterNum AND c.bookTitle=:bookTitle");
          completedChapterQuery.setParameter("chapterNum", chapterNum);
          completedChapterQuery.setParameter("bookTitle", bookTitle);
          String chapterTitle = (String) completedChapterQuery.uniqueResult();
          if (chapterTitle == null || chapterTitle.isBlank()) {
            throw new SQLException("That Chapter doesn't exist!");
          }

          // Query the database for the student's ID using their first and last name
          completedChapterQuery = session.createQuery("select id from Student s where " +
              "firstName=:firstName AND lastName=:lastName");
          completedChapterQuery.setParameter("firstName", studentFirstName);
          completedChapterQuery.setParameter("lastName", studentLastName);
          Long studentId = (Long) completedChapterQuery.uniqueResult();
          if (studentId == null) {
            throw new SQLException("That student doesn't exist!");
          }

          // Check if the chapter to be made already exists in the table and raise a flag if it does
          boolean chapterExists = false;
          ListIterator<CompletedChapter> chapterListIterator = completedChapters.getItems().listIterator();

          while (chapterListIterator.hasNext()) {
            CompletedChapter currentChapter = chapterListIterator.next();

            if (currentChapter.getStudentId().equals(studentId) && (currentChapter.getChapterTitle().equals(chapterTitle) && currentChapter.getBookTitle().equals(bookTitle))) {
              System.err.println("THAT CHAPTER ALREADY EXISTS");
              chapterExists = true;
            }
          }

          if (!chapterExists) {
            // If the chapter doesn't exist already, create a new chapter and save it in the
            // table and database
            CompletedChapter newCompletedChapter = new CompletedChapter(studentId, chapterNum, date,
                bookTitle, chapterTitle);

            session.save(newCompletedChapter);
            completedChapters.getItems().add(newCompletedChapter);
          } else {
            // Give the user a warning if they try to make a duplicate Completed Chapter
            warningLabel.setText("That chapter has already been completed!");
            fadeIn.play();
            fadeOut.play();
          }
        } catch (SQLException sqle) {
          sqle.printStackTrace();
          warningLabel.setText(sqle.getMessage());
        }
      }
    });


    Button deleteButton = new Button("Delete");
    deleteButton.getStyleClass().add("delete-button");
    deleteButton.setOnAction(e -> {
      // Get the selected completed chapter from the tableview and delete it from the table and
      // database
      CompletedChapter selectedCompletedChapter = completedChapters.getSelectionModel().getSelectedItem();
      completedChapters.getItems().remove(selectedCompletedChapter);
      session.delete(selectedCompletedChapter);
      session.getTransaction().commit();
      session.beginTransaction();
    });


    final VBox vbox = new VBox();
    vbox.setSpacing(5);
    vbox.setPadding(new Insets(10, 10, 0, 10));

    // Create and style GridPane for our user input form
    GridPane completedChapterInputForm = new GridPane();
    completedChapterInputForm.setPadding(new Insets(10, 10, 10, 10));
    completedChapterInputForm.setMaxSize(3840, 2160);
    completedChapterInputForm.setMinSize(800, 400);
    completedChapterInputForm.setHgap(5);
    completedChapterInputForm.setVgap(5);

    // Add contents to our GridPane, and add our GridPane to our Vbox
    completedChapterInputForm.add(studentFirstNameField, 0, 0);
    completedChapterInputForm.add(studentLastNameField, 1, 0);
    completedChapterInputForm.add(bookTitleField, 2, 0);
    completedChapterInputForm.add(chapterNumberSpinner, 3, 0);
    completedChapterInputForm.add(submitButton, 4, 0);
    completedChapterInputForm.add(deleteButton, 5, 0);
    completedChapterInputForm.add(warningLabel, 6, 0);
    vbox.getChildren().addAll(completedChapters, completedChapterInputForm, cannotEditLabel);


    Tab completedChaptersTab = new Tab("Completed Chapters");
    completedChaptersTab.setClosable(false);
    completedChaptersTab.setContent(vbox);

    return completedChaptersTab;
  }

  public static ObservableList<String> generateBookList(TableView<Chapter> chapterTable) {
    ArrayList<String> books = new ArrayList<>();
    // Because the creation of a new completed chapter without any chapters existing crashes the
    // program, we need to add an empty string to the list initially, and remove it if any
    // chapters already exist
    books.add("");
    for(Chapter currentChapter : chapterTable.getItems()) {
      if (!books.contains(currentChapter.getBookTitle())) {
        books.add(currentChapter.getBookTitle());
      }
      if(books.size() >= 1) {
        books.remove("");
      }
    }

    return FXCollections.observableList(books);
  }
}
