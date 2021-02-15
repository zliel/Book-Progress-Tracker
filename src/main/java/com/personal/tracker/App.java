package com.personal.tracker;

import com.personal.tracker.controller.Add;
import com.personal.tracker.controller.Query;
import com.personal.tracker.controller.Update;
import com.personal.tracker.models.Chapter;
import com.personal.tracker.models.CompletedChapter;
import com.personal.tracker.models.Student;
import com.personal.tracker.views.ChaptersTab;
import com.personal.tracker.views.CompletedChapterTab;
import com.personal.tracker.views.StudentTab;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.converter.LongStringConverter;
import org.apache.commons.text.WordUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class App extends Application {

  @Override
  public void start(Stage stage) {
    // Check the config file to see if the database exists
    initialize();

    // Create our initial tables
    TableView<Student> studentTable = createStudentTable();
    studentTable.setEditable(true);
    TableView<CompletedChapter> completedChaptersTable = createCompletedChaptersTable();
    completedChaptersTable.setEditable(true);
    TableView<Chapter> chapterTable = createChapterTable(completedChaptersTable);
    chapterTable.setEditable(true);

    // Create a tabPane to choose which part of the database to view
    TabPane tabs = new TabPane();

    // Create tabs
    Tab studentTab = StudentTab.createStudentTab(studentTable, completedChaptersTable);
    studentTab.setOnSelectionChanged(event -> {
      studentTab.setContent(StudentTab.createStudentTab(studentTable, completedChaptersTable).getContent());
    });

    Tab chapterTab = ChaptersTab.createChaptersTab(chapterTable, completedChaptersTable);
    chapterTab.setOnSelectionChanged(event -> {
      chapterTab.setContent(ChaptersTab.createChaptersTab(chapterTable, completedChaptersTable).getContent());
    });

    Tab completedChaptersTab = CompletedChapterTab.createCompletedChaptersTab(completedChaptersTable, chapterTable);

    // Reset the content of the Completed Chapters Tab each time the tab is selected so that if a user enters a new book into
    // the Chapters table, it will be added to the ComboBox in the Completed Chapters Tab
    completedChaptersTab.setOnSelectionChanged(event -> {
      completedChaptersTab.setContent(CompletedChapterTab.createCompletedChaptersTab(completedChaptersTable, chapterTable).getContent());
    });

    // Add the tabs to the TabPane
    tabs.getTabs().add(studentTab);
    tabs.getTabs().add(chapterTab);
    tabs.getTabs().add(completedChaptersTab);

    // Create our scene
    Scene scene = new Scene(new StackPane(tabs), 1440, 900);

    // Add our CSS file to our scene (found in resources/com.personal.tracker/style.css)
    scene.getStylesheets().add("com.personal.tracker/styles.css");

    // All the stuff for the actual stage
    stage.setTitle("Book Progress Tracker");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }

  public static void initialize() {
    try {
      File config = new File("settings.txt");
      if(!config.exists()) {
        throw new FileNotFoundException("The file doesn't exist.");
      }
      Scanner configReader = new Scanner(config);

      while(configReader.hasNextLine()) {
        String data = configReader.nextLine();
        if(data.equals("database_exists==False")) {
          Add.createDatabase();
        }
        System.out.println(data);
      }
    } catch (FileNotFoundException fnf) {
      File newConfig = new File("settings.txt");

      try {
        Add.createDatabase();
        FileWriter configWriter = new FileWriter(newConfig);
        configWriter.write("database_exists==True");
        configWriter.flush();
        configWriter.close();

      } catch (IOException ioe) {
        System.err.println("There was an IOException: " + ioe);
      }
    }
  }

  @SuppressWarnings("unchecked")
  public static TableView<Student> createStudentTable() {
    // Move what's in start() here
    // Create a TableView for our database output
    TableView<Student> studentTable = new TableView<>();

    // Get an ArrayList with students from the database
    ArrayList<Student> students = Query.listStudents();

    // Create an observable list for reactivity later
    ObservableList<Student> list = FXCollections.observableList(students);
    studentTable.setItems(list);

    // Set the columns' titles and data (the data comes from the Student.java file in the
    // com.personal.tracker.models package
    TableColumn<Student, Long> idCol = new TableColumn<>("ID");
    idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

    TableColumn<Student, String> firstNameCol = new TableColumn<>("First Name");
    firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    firstNameCol.setOnEditCommit(event -> {
      Student studentToChange = studentTable.getSelectionModel().getSelectedItem();
      String updatedFirstName = WordUtils.capitalizeFully(event.getNewValue());

      Update.updateStudentFirstName(updatedFirstName, studentToChange.getId());
      studentToChange.setFirstName(updatedFirstName);
      studentTable.setItems(createStudentTable().getItems());
    });

    TableColumn<Student, String> lastNameCol = new TableColumn<>("Last Name");
    lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    lastNameCol.setOnEditCommit(event -> {
      Student studentToChange = studentTable.getSelectionModel().getSelectedItem();
      String updatedLastName = WordUtils.capitalizeFully(event.getNewValue());

      Update.updateStudentLastName(updatedLastName, studentToChange.getId());
      studentToChange.setLastName(updatedLastName);
      studentTable.setItems(createStudentTable().getItems());
    });

    // Set the data in the table
    studentTable.getColumns().setAll(idCol, firstNameCol, lastNameCol);
    return studentTable;
  }

  @SuppressWarnings("unchecked")
  public static TableView<Chapter> createChapterTable(TableView<CompletedChapter> completedChapterTable) {
    // Create a TableView for our database output
    TableView<Chapter> chapterTable = new TableView<>();

    // Get an ArrayList with students from the database
    ArrayList<Chapter> students = Query.listChapters();

    // Create an observable list for reactivity later
    ObservableList<Chapter> list = FXCollections.observableList(students);
    chapterTable.setItems(list);

    // Set the columns' titles and data (the data comes from the Student.java file in the
    // com.personal.tracker.models package

    TableColumn<Chapter, Long> chapterNumCol = new TableColumn<>("Chapter Number");
    chapterNumCol.setCellValueFactory(new PropertyValueFactory<>("chapterKey"));
    chapterNumCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
    chapterNumCol.setOnEditCommit(event -> {
      // This is the flag we'll use to determine whether or not the user's input is valid
      boolean inputIsValid = true;

      Chapter chapterToChange = chapterTable.getSelectionModel().getSelectedItem();
      long updatedChapterNum = event.getNewValue();
      long oldChapterNum = event.getOldValue();

      // Make sure the chapter number doesn't already exist
      for(Chapter currentChapter : list) {
        if((!currentChapter.equals(chapterToChange)) && currentChapter.getChapterKey().equals(updatedChapterNum) && currentChapter.getBookTitle().equalsIgnoreCase(chapterToChange.getBookTitle())) {
          System.err.println("THAT CHAPTER NUMBER ALREADY EXISTS");
          inputIsValid = false;

          chapterTable.setItems(createChapterTable(completedChapterTable).getItems());
        }
      }

      // If our input is valid, we update the database and tables
      if(inputIsValid) {
        Update.updateChapterNumber(oldChapterNum, updatedChapterNum, chapterToChange.getBookTitle());
        chapterToChange.setChapterKey(updatedChapterNum);
        chapterTable.setItems(createChapterTable(completedChapterTable).getItems());
        completedChapterTable.setItems(createCompletedChaptersTable().getItems());
      }
    });


    TableColumn<Chapter, String> chapterTitleCol = new TableColumn<>("Chapter Title");
    chapterTitleCol.setCellValueFactory(new PropertyValueFactory<>("chapterTitle"));
    chapterTitleCol.setCellFactory(TextFieldTableCell.forTableColumn());
    chapterTitleCol.setOnEditCommit(event -> {
      // This is the flag we'll use to determine whether or not the user's input is valid
      boolean inputIsValid = true;

      Chapter chapterToChange = chapterTable.getSelectionModel().getSelectedItem();
      String updatedChapterTitle = WordUtils.capitalizeFully(event.getNewValue());
      String oldChapterTitle = event.getOldValue();

      // Make sure the chapter number doesn't already exist
      for(Chapter currentChapter : list) {
        if((!currentChapter.equals(chapterToChange)) && currentChapter.getChapterTitle().equals(updatedChapterTitle) && currentChapter.getBookTitle().equalsIgnoreCase(chapterToChange.getBookTitle())) {
          System.err.println("THAT CHAPTER NUMBER ALREADY EXISTS");
          inputIsValid = false;

          chapterTable.setItems(createChapterTable(completedChapterTable).getItems());
        }
      }

      // If our input is valid, we update the database and tables
      if(inputIsValid) {
        Update.updateChapterTitle(updatedChapterTitle, oldChapterTitle, chapterToChange.getBookTitle());
        chapterToChange.setChapterTitle(updatedChapterTitle);
        chapterTable.setItems(createChapterTable(completedChapterTable).getItems());
      }
    });

    TableColumn<Chapter, String> bookTitleCol = new TableColumn<>("Book Title");
    bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
    bookTitleCol.setCellFactory(TextFieldTableCell.forTableColumn());
    TextFieldTableCell<String, String> test = new TextFieldTableCell<>();

    bookTitleCol.setOnEditCommit(event -> {
      // This is the flag we'll use to determine whether or not the user's input is valid
      boolean inputIsValid = true;

      Chapter chapterToChange = chapterTable.getSelectionModel().getSelectedItem();
      String updatedBookTitle = WordUtils.capitalizeFully(event.getNewValue());
      String oldBookTitle = event.getOldValue();

      // Make sure the chapter number doesn't already exist
      for(Chapter currentChapter : list) {
        if((!currentChapter.equals(chapterToChange)) && currentChapter.getBookTitle().equalsIgnoreCase(oldBookTitle)) {
          System.err.println("THAT CHAPTER NUMBER ALREADY EXISTS");
          inputIsValid = false;

          chapterTable.setItems(createChapterTable(completedChapterTable).getItems());
        }
      }

      // If our input is valid, we update the database and tables
      if(inputIsValid) {
        Update.updateBookTitle(oldBookTitle, updatedBookTitle);
        chapterToChange.setBookTitle(updatedBookTitle);
        chapterTable.setItems(createChapterTable(completedChapterTable).getItems());
      }
    });

    // Set the data in the table
    chapterTable.getColumns().setAll(chapterNumCol, chapterTitleCol, bookTitleCol);
    return chapterTable;
  }

  @SuppressWarnings("unchecked")
  public static TableView<CompletedChapter> createCompletedChaptersTable() {
    // Create a TableView for our database output
    TableView<CompletedChapter> completedChaptersTable = new TableView<>();

    // Get an ArrayList with students from the database
    ArrayList<CompletedChapter> completedChapters = Query.listCompletedChapters();

    // Create an observable list for reactivity later
    ObservableList<CompletedChapter> list = FXCollections.observableArrayList(completedChapters);
    completedChaptersTable.setItems(list);

    // Set the columns' titles and data (the data comes from the Student.java file in the
    // com.personal.tracker.models package
    TableColumn<CompletedChapter, Long> studentIdCol = new TableColumn<>("Student ID");
    studentIdCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));
    TableColumn<CompletedChapter, Long> chapterNumCol = new TableColumn<>("Chapter Number");
    chapterNumCol.setCellValueFactory(new PropertyValueFactory<>("chapterNumber"));
    TableColumn<CompletedChapter, Date> completedDateCol = new TableColumn<>("Completion Date");
    completedDateCol.setCellValueFactory(new PropertyValueFactory<>("completionDate"));
    TableColumn<CompletedChapter, String> chapterTitleCol = new TableColumn<>("Chapter Title");
    chapterTitleCol.setCellValueFactory(new PropertyValueFactory<>("chapterTitle"));
    TableColumn<CompletedChapter, String> bookTitleCol = new TableColumn<>("Book Title");
    bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

    // Set the data in the table
    completedChaptersTable.getColumns().setAll(studentIdCol, chapterNumCol, chapterTitleCol, bookTitleCol, completedDateCol);
    return completedChaptersTable;
  }

  @Override
  public void stop() {
    // This should include anything to be done at the end of the program's lifecycle
    System.out.println("We did it");
  }
}