package com.personal.tracker;

import com.personal.tracker.models.Chapter;
import com.personal.tracker.models.Student;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;

public class App extends Application {

  @Override
  public void start(Stage stage) {
    TableView studentTable = createStudentTable();
    TableView chapterTable = createChapterTable();

    /* TODO TESTING */
    // Create a tabPane to choose which part of the database to view
    TabPane tabs = new TabPane();

    // Create tabs
    Tab studentTab = new Tab("Students", studentTable);
    Tab chapterTab = new Tab("Chapters", chapterTable);
    Tab testTab = new Tab("Testing!", new Label("This is a test tab! :)"));

    // Add the tabs to the TabPane
    tabs.getTabs().add(studentTab);
    tabs.getTabs().add(chapterTab);
    tabs.getTabs().add(testTab);

    // Create our scene
    Scene scene = new Scene(new StackPane(tabs), 1440, 900);
    scene.setFill(Color.BLUEVIOLET);

    // All the stuff for the actual stage
    stage.setTitle("Book Progress Tracker");
    stage.setScene(scene);
    stage.show();

  }

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void stop() {
    // This should include anything to be done at the end of the program's lifecycle
    System.out.println("We did it");
  }

  public static TableView createStudentTable() {
    // Move what's in start() here
    // Create a TableView for our database output
    TableView<Student> studentTable = new TableView<Student>();
    // TODO - Look into getting data from the database into the table

    // Get an ArrayList with students from the database
    ArrayList<Student> students = Query.listStudents();

    // Create an observable list for reactivity later
    ObservableList<Student> list = FXCollections.observableArrayList(students);
    studentTable.setItems(list);

    // Set the columns' titles and data (the data comes from the Student.java file in the
    // com.personal.tracker.models package
    TableColumn<Student, Long> idCol = new TableColumn<>("ID");
    idCol.setCellValueFactory(new PropertyValueFactory("id"));
    TableColumn<Student, String> firstNameCol = new TableColumn<>("First Name");
    firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
    TableColumn<Student, String> lastNameCol = new TableColumn<>("Last Name");
    lastNameCol.setCellValueFactory(new PropertyValueFactory("lastName"));

    // Set the data in the table
    studentTable.getColumns().setAll(idCol, firstNameCol, lastNameCol);
    return studentTable;
  }

  public static TableView createChapterTable() {
    // Create a TableView for our database output
    TableView<Chapter> chapterTable = new TableView<>();
    // TODO - Look into getting data from the database into the table

    // Get an ArrayList with students from the database
    ArrayList<Chapter> students = Query.listChapters();

    // Create an observable list for reactivity later
    ObservableList<Chapter> list = FXCollections.observableArrayList(students);
    chapterTable.setItems(list);

    // Set the columns' titles and data (the data comes from the Student.java file in the
    // com.personal.tracker.models package

    TableColumn<Chapter, Long> chapterNumCol = new TableColumn<>("Chapter Number");
    chapterNumCol.setCellValueFactory(new PropertyValueFactory("chapterKey"));
    TableColumn<Chapter, String> chapterTitleCol = new TableColumn<>("Chapter Title");
    chapterTitleCol.setCellValueFactory(new PropertyValueFactory("chapterTitle"));
    TableColumn<Chapter, String> bookTitleCol = new TableColumn<>("Book Title");
    bookTitleCol.setCellValueFactory(new PropertyValueFactory("bookTitle"));

    // Set the data in the table
    chapterTable.getColumns().setAll(chapterNumCol, chapterTitleCol, bookTitleCol);
    return chapterTable;
  }

}