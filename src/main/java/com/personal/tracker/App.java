package com.personal.tracker;

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

public class App extends Application {

  @Override
  public void start(Stage stage) {
    // Create a TableView for our database output
    TableView<Student> databaseTable = new TableView<Student>();
    // TODO - Look into getting data from the database into the table

    // Create an observable list for reactivity later
    ObservableList<Student> list = FXCollections.observableArrayList(new Student("Jimmy", "John"));
    databaseTable.setItems(list);

    // Set the columns' titles and data (the data comes from the Student.java file in the
    // com.personal.tracker.models package
    TableColumn<Student, String> firstNameCol = new TableColumn<>("First Name");
    firstNameCol.setCellValueFactory(new PropertyValueFactory("firstName"));
    TableColumn<Student, String> lastNameCol = new TableColumn<>("Last Name");
    lastNameCol.setCellValueFactory(new PropertyValueFactory("lastName"));

    // Set the data in the table
    databaseTable.getColumns().setAll(firstNameCol, lastNameCol);

    // Create our scene
    Scene scene = new Scene(new StackPane(databaseTable), 1440, 900);
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

}