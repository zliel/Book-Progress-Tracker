package com.personal.tracker.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Student {
  @Id
  private Long id;
  private String firstName;
  private String lastName;

  public Student(Long studentId, String firstName, String lastName) {
    this.id = studentId;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Long getId() { return id; }

  public void setId(Long id) { this.id = id; }

  @Override
  public String toString() {
    return "Student(" +
        "id = " + id + ", " +
        "firstName = " + firstName + ", " +
        "lastName = " + lastName + ")";
  }
}
