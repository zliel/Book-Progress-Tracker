package com.personal.tracker.models;

public class Student {
  private String firstName;
  private String lastName;
  private Long id;

  public Student(Long id, String firstName, String lastName) {
    this.id = id;
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
}
