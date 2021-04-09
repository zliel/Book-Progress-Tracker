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

  Session persistenceSession;

  @Override
  public void start(Stage stage) {
    persistenceSession = initialize();
    persistenceSession.beginTransaction();

    TableView<Student> studentTable = createStudentTable(persistenceSession);
    studentTable.setEditable(true);
    TableView<CompletedChapter> completedChaptersTable = createCompletedChaptersTable(persistenceSession);
    completedChaptersTable.setEditable(true);
    TableView<Chapter> chapterTable = createChapterTable(completedChaptersTable, persistenceSession);
    chapterTable.setEditable(true);

    TabPane tabs = new TabPane();

    // Create tabs and add them to the TabPane
    Tab studentTab = StudentTab.createStudentTab(studentTable, completedChaptersTable, persistenceSession);
    Tab chapterTab = ChaptersTab.createChaptersTab(chapterTable, completedChaptersTable, persistenceSession);
    Tab completedChaptersTab =
        CompletedChapterTab.createCompletedChaptersTab(completedChaptersTable, chapterTable, studentTable, persistenceSession);
    tabs.getTabs().addAll(studentTab, chapterTab, completedChaptersTab);

    // Create and style our scene
    Scene scene = new Scene(new StackPane(tabs), 1440, 900);
    scene.getStylesheets().add("com.personal.tracker/styles.css");

    stage.setTitle("Book Progress Tracker");
    stage.setScene(scene);
    stage.show();

  }

  public static void main(String[] args) {
    launch();
  }

  public static Session initialize() {
    // Configure and build the registry for our Session objects using the hibernate.cfg.xml file
    StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
    MetadataSources sources = new MetadataSources(registry);

    // Configure our MetadataSources and SessionFactory, and return a newly opened Session
    sources.addAnnotatedClasses(Student.class, Chapter.class, CompletedChapter.class).getMetadataBuilder().build();
    SessionFactory sessionFactory = sources.buildMetadata().buildSessionFactory();
    return sessionFactory.openSession();
  }

  @SuppressWarnings("unchecked")
  // Because the type isn't known at compile time, an unchecked assignment warning will always show
  public static <E> ObservableList<E> createObservableList(Class<E> type, Session session) {
    System.out.println("Type: " + type);
    System.out.println("Type Class Simple Name: " + type.getSimpleName());
    Query<E> query = session.createQuery("from " + type.getSimpleName());

    return FXCollections.observableList(query.list());
  }

  @SuppressWarnings("unchecked") // Using the addAll() method for TableColumns will always result
  // in an "Unchecked generics array creation for varargs parameter" warning
  public static TableView<Student> createStudentTable(Session session) {
    TableView<Student> studentTable = new TableView<>();

    // Query the database for all students to fill the table
    ObservableList<Student> list = createObservableList(Student.class, session);
    studentTable.setItems(list);

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
    });

    TableColumn<Student, String> lastNameCol = new TableColumn<>("Last Name");
    lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    lastNameCol.setOnEditCommit(event -> {
      Student studentToChange = studentTable.getSelectionModel().getSelectedItem();
      String updatedLastName = WordUtils.capitalizeFully(event.getNewValue());

      studentToChange.setLastName(updatedLastName);
      session.save(studentToChange);
    });

    // Set up the columns in the table
    studentTable.getColumns().setAll(idCol, firstNameCol, lastNameCol);
    return studentTable;
  }

  @SuppressWarnings("unchecked") // Using the addAll() method for TableColumns will always result
  // in an "Unchecked generics array creation for varargs parameter" warning
  public static TableView<Chapter> createChapterTable(TableView<CompletedChapter> completedChapterTable, Session session) {
    TableView<Chapter> chapterTable = new TableView<>();

    // Query the database for all chapters to fill the table
    ObservableList<Chapter> list = createObservableList(Chapter.class, session);
    chapterTable.setItems(list);

    TableColumn<Chapter, Long> chapterNumCol = new TableColumn<>("Chapter Number");
    chapterNumCol.setCellValueFactory(new PropertyValueFactory<>("chapterNum"));
    chapterNumCol.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
    chapterNumCol.setOnEditCommit(event -> {
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
        Query<CompletedChapter> completedChapterBookQuery = session.createQuery("from CompletedChapter cc " +
            "where " +
            "chapterNumber=:oldNum AND bookTitle=:bookTitle");
        completedChapterBookQuery.setParameter("oldNum", oldChapterNum);
        completedChapterBookQuery.setParameter("bookTitle", chapterToChange.getBookTitle());

        // Loop through the returned completed chapters and update their book titles accordingly
        ArrayList<CompletedChapter> completedChapters =
            new ArrayList<>(completedChapterBookQuery.list());

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
      boolean inputIsValid = true;

      Chapter chapterToChange = chapterTable.getSelectionModel().getSelectedItem();
      String updatedChapterTitle = WordUtils.capitalizeFully(event.getNewValue());
      String oldChapterTitle = event.getOldValue();

      if(updatedChapterTitle.isBlank()) {
        System.err.println("THE NEW CHAPTER TITLE CAN'T BE BLANK");
        inputIsValid = false;
      }

      // If our input is valid, we update the database and tables
      if(inputIsValid) {
        chapterToChange.setChapterTitle(updatedChapterTitle);
        session.save(chapterToChange);

        Query<CompletedChapter> completedChapterBookQuery = session.createQuery("from CompletedChapter cc " +
            "where " +
            "chapterTitle=:oldTitle");
        completedChapterBookQuery.setParameter("oldTitle", oldChapterTitle);

        ArrayList<CompletedChapter> completedChapters =
            new ArrayList<>(completedChapterBookQuery.list());

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

    bookTitleCol.setOnEditCommit(event -> {
      boolean inputIsValid = true;

      String updatedBookTitle = WordUtils.capitalizeFully(event.getNewValue());
      String oldBookTitle = event.getOldValue();

      // Make sure the new title isn't an empty string
      if(updatedBookTitle.isBlank()) {
        System.err.println("THE NEW TITLE CAN'T BE BLANK");
        inputIsValid = false;

        chapterTable.setItems(createChapterTable(completedChapterTable, session).getItems());
      }

      // If our input is valid, we update the database and tables
      if(inputIsValid) {
        for (Chapter currentChapter : list) {
          if (currentChapter.getBookTitle().equals(oldBookTitle)) {
            currentChapter.setBookTitle(updatedBookTitle);
            session.save(currentChapter);
          }
        }

        // Loop through the completed chapters in the database and change matching old book
        // titles to the new one
        Query<CompletedChapter> completedChapterBookQuery = session.createQuery("from CompletedChapter cc " +
            "where " +
            "bookTitle=:oldTitle");
        completedChapterBookQuery.setParameter("oldTitle", oldBookTitle);

        ArrayList<CompletedChapter> completedChapters = new ArrayList<>(completedChapterBookQuery.list());

        for (CompletedChapter currentCC : completedChapters) {
          currentCC.setBookTitle(updatedBookTitle);
          session.save(currentCC);
        }

        session.getTransaction().commit();
        session.beginTransaction();

        completedChapterTable.refresh();
      }

      chapterTable.refresh();
    });

    // Set the data in the table
    chapterTable.getColumns().setAll(chapterNumCol, chapterTitleCol, bookTitleCol);
    return chapterTable;
  }

  @SuppressWarnings("unchecked") // Using the addAll() method for TableColumns will always result
  // in an "Unchecked generics array creation for varargs parameter" warning
  public static TableView<CompletedChapter> createCompletedChaptersTable(Session session) {
    TableView<CompletedChapter> completedChaptersTable = new TableView<>();

    // Query the database for all completed chapters to fill the table
    ObservableList<CompletedChapter> list = createObservableList(CompletedChapter.class, session);
    completedChaptersTable.setItems(list);

    // Set the columns' titles and data
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

    completedChaptersTable.getColumns().setAll(studentIdCol, chapterNumCol, chapterTitleCol, bookTitleCol, completedDateCol);
    return completedChaptersTable;
  }

  @Override
  public void stop() {
    persistenceSession.getTransaction().commit();
    persistenceSession.close();
  }
}