package com.personal.tracker;

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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Date;

public class App extends Application {

  // We configure and build the registry for our Session objects using the hibernate.cfg.xml file
  private final static StandardServiceRegistry registry =
      new StandardServiceRegistryBuilder().configure().build();
  private final static MetadataSources sources = new MetadataSources(registry);
  private static Session persistenceSession;

  @Override
  public void start(Stage stage) {
    // Check the config file to see if the database exists
    persistenceSession = initialize();
    persistenceSession.beginTransaction();

    // Create our initial tables
    TableView<Student> studentTable = createStudentTable(persistenceSession);
    studentTable.setEditable(true);
    TableView<CompletedChapter> completedChaptersTable = createCompletedChaptersTable(persistenceSession);
    completedChaptersTable.setEditable(true);
    TableView<Chapter> chapterTable = createChapterTable(completedChaptersTable, persistenceSession);
    chapterTable.setEditable(true);

    // Create a tabPane to choose which part of the database to view
    TabPane tabs = new TabPane();

    // Create tabs
    Tab studentTab = StudentTab.createStudentTab(studentTable, completedChaptersTable, persistenceSession);
    studentTab.setOnSelectionChanged(event -> {
      studentTab.setContent(StudentTab.createStudentTab(studentTable, completedChaptersTable,
          persistenceSession).getContent());
    });

    Tab chapterTab = ChaptersTab.createChaptersTab(chapterTable, completedChaptersTable, persistenceSession);
    chapterTab.setOnSelectionChanged(event -> {
      chapterTab.setContent(ChaptersTab.createChaptersTab(chapterTable, completedChaptersTable,
          persistenceSession).getContent());
    });

    Tab completedChaptersTab =
        CompletedChapterTab.createCompletedChaptersTab(completedChaptersTable, chapterTable,
            studentTable, persistenceSession);

    // Reset the content of the Completed Chapters Tab each time the tab is selected so that if a user enters a new book into
    // the Chapters table, it will be added to the ComboBox in the Completed Chapters Tab
    completedChaptersTab.setOnSelectionChanged(event -> {
      completedChaptersTab.setContent(CompletedChapterTab.createCompletedChaptersTab(completedChaptersTable, chapterTable,studentTable, persistenceSession).getContent());
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

  public static Session initialize() {
    // Configure our MetadataSources and SessionFactory, and return our newly opened Session
    sources.addAnnotatedClasses(Student.class, Chapter.class, CompletedChapter.class).getMetadataBuilder().build();
    SessionFactory sessionFactory = sources.buildMetadata().buildSessionFactory();
    return sessionFactory.openSession();
  }

  @SuppressWarnings("unchecked")
  public static TableView<Student> createStudentTable(Session session) {
    // Create a TableView for our database output
    TableView<Student> studentTable = new TableView<>();

    //HQL Query to retrieve all students in the database
    Query studentQuery = session.createQuery("from Student");

    // Get an ArrayList with students from the database
    ArrayList<Student> students = new ArrayList<>(studentQuery.list());

    // Create an observable list for reactivity later
    ObservableList<Student> list = FXCollections.observableList(students);
    studentTable.setItems(list);

    // Set the columns' titles and data (the data comes from the Student.java file in the
    // com.personal.tracker.models package)
    TableColumn<Student, Long> idCol = new TableColumn<>("ID");
    idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

    TableColumn<Student, String> firstNameCol = new TableColumn<>("First Name");
    firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    firstNameCol.setOnEditCommit(event -> {
      Student studentToChange = studentTable.getSelectionModel().getSelectedItem();
      String updatedFirstName = WordUtils.capitalizeFully(event.getNewValue());

      studentToChange.setFirstName(updatedFirstName);
      session.save(studentToChange);
      studentTable.setItems(createStudentTable(session).getItems());
    });

    TableColumn<Student, String> lastNameCol = new TableColumn<>("Last Name");
    lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    lastNameCol.setOnEditCommit(event -> {
      Student studentToChange = studentTable.getSelectionModel().getSelectedItem();
      String updatedLastName = WordUtils.capitalizeFully(event.getNewValue());

      studentToChange.setLastName(updatedLastName);
      session.save(studentToChange);
      studentTable.setItems(createStudentTable(session).getItems());
    });

    // Set the data in the table
    studentTable.getColumns().setAll(idCol, firstNameCol, lastNameCol);
    return studentTable;
  }

  @SuppressWarnings("unchecked")
  public static TableView<Chapter> createChapterTable(TableView<CompletedChapter> completedChapterTable, Session session) {
    // Create a TableView for our database output
    TableView<Chapter> chapterTable = new TableView<>();

    // HQL Query to retrieve all chapters from the database
    Query chapterQuery = session.createQuery("from Chapter");

    // Get an ArrayList with chapters from the database
    ArrayList<Chapter> chapters = new ArrayList<Chapter>(chapterQuery.list());

    // Create an observable list for reactivity later
    ObservableList<Chapter> list = FXCollections.observableList(chapters);
    chapterTable.setItems(list);

    // Set the columns' titles and data (the data comes from the Chapter.java file in the
    // com.personal.tracker.models package

    TableColumn<Chapter, Long> chapterNumCol = new TableColumn<>("Chapter Number");
    chapterNumCol.setCellValueFactory(new PropertyValueFactory<>("chapterNum"));
    chapterNumCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
    chapterNumCol.setOnEditCommit(event -> {
      // This is the flag we'll use to determine whether or not the user's input is valid
      boolean inputIsValid = true;

      Chapter chapterToChange = chapterTable.getSelectionModel().getSelectedItem();
      long updatedChapterNum = event.getNewValue();
      long oldChapterNum = event.getOldValue();

      // Make sure the chapter number doesn't already exist
      for(Chapter currentChapter : list) {
        if((!currentChapter.equals(chapterToChange)) && currentChapter.getChapterNum().equals(updatedChapterNum) && currentChapter.getBookTitle().equalsIgnoreCase(chapterToChange.getBookTitle())) {
          System.err.println("THAT CHAPTER NUMBER ALREADY EXISTS");
          inputIsValid = false;
        }
      }

      // If our input is valid, we update the database and tables
      if(inputIsValid) {
        chapterToChange.setChapterNum(updatedChapterNum);
        session.save(chapterToChange);

        // Retrieve all completed chapters with the same book title as the one being changed
        Query completedChapterBookQuery = session.createQuery("from CompletedChapter cc " +
            "where " +
            "chapterNumber=:oldNum AND bookTitle=:bookTitle");
        completedChapterBookQuery.setParameter("oldNum", oldChapterNum);
        completedChapterBookQuery.setParameter("bookTitle", chapterToChange.getBookTitle());

        // Loop through the returned completed chapters and update their book titles accordingly
        ArrayList<CompletedChapter> completedChapters = new ArrayList<>(completedChapterBookQuery.list());
        for(CompletedChapter currentCC : completedChapters) {
          currentCC.setChapterNumber(updatedChapterNum);
          session.save(currentCC);
        }

        completedChapterTable.refresh();

      }

      chapterTable.refresh();
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
      if(updatedChapterTitle.isBlank()) {
        System.err.println("THE NEW CHAPTER TITLE CAN'T BE BLANK");
        inputIsValid = false;
      }

      // If our input is valid, we update the database and tables
      if(inputIsValid) {
        chapterToChange.setChapterTitle(updatedChapterTitle);
        session.save(chapterToChange);

        Query completedChapterBookQuery = session.createQuery("from CompletedChapter cc " +
            "where " +
            "chapterTitle=:oldTitle");
        completedChapterBookQuery.setParameter("oldTitle", oldChapterTitle);

        ArrayList<CompletedChapter> completedChapters = new ArrayList<>(completedChapterBookQuery.list());

        for(CompletedChapter currentCC : completedChapters) {
          currentCC.setChapterTitle(updatedChapterTitle);
          session.save(currentCC);
        }

        completedChapterTable.refresh();
      }

      chapterTable.refresh();
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

      // Make sure the new title isn't an empty string
      for(Chapter currentChapter : list) {
        if(updatedBookTitle.isBlank()) {
          System.err.println("THE NEW TITLE CAN'T BE BLANK");
          inputIsValid = false;

          chapterTable.setItems(createChapterTable(completedChapterTable, session).getItems());
        }
      }

      // If our input is valid, we update the database and tables
      if(inputIsValid) {
        // Loop through the chapters in the table and change all of the matching book titles to
        // the new one
        for(Chapter currentChapter : list) {
          if(currentChapter.getBookTitle().equals(oldBookTitle)) {
            currentChapter.setBookTitle(updatedBookTitle);
            session.save(currentChapter);
          }
        }

        // Loop through the completed chapters in the database and change matching old book
        // titles to the new one
        Query completedChapterBookQuery = session.createQuery("from CompletedChapter cc where bookTitle=:oldTitle");
        completedChapterBookQuery.setParameter("oldTitle", oldBookTitle);

        ArrayList<CompletedChapter> completedChapters = new ArrayList<>(completedChapterBookQuery.list());

        for(CompletedChapter currentCC : completedChapters) {
          currentCC.setBookTitle(updatedBookTitle);
          session.save(currentCC);
        }

        // Commit the transaction to ensure the objects are no longer in the persistence state
        // and have fully saved to the database
        session.getTransaction().commit();
        session.beginTransaction();

        // Refresh the tables to reflect the changes made
        completedChapterTable.refresh();
      }

      chapterTable.refresh();
    });

    // Set the data in the table
    chapterTable.getColumns().setAll(chapterNumCol, chapterTitleCol, bookTitleCol);
    return chapterTable;
  }

  @SuppressWarnings("unchecked")
  public static TableView<CompletedChapter> createCompletedChaptersTable(Session session) {
    // Create a TableView for our database output
    TableView<CompletedChapter> completedChaptersTable = new TableView<>();

    // HQL Query to retrieve all completed chapters
    Query completedChapterQuery = session.createQuery("from CompletedChapter");

    // Get an ArrayList with students from the database
    ArrayList<CompletedChapter> completedChapters = new ArrayList<>(completedChapterQuery.list());


    // Create an observable list for reactivity later
    ObservableList<CompletedChapter> list = FXCollections.observableArrayList(completedChapters);
    completedChaptersTable.setItems(list);

    // Set the columns' titles and data (the data comes from the CompletedChapter.java file in the
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
    persistenceSession.getTransaction().commit();
    persistenceSession.close();
    // This should include anything to be done at the end of the program's lifecycle
    System.out.println("Mission Success");
  }
}