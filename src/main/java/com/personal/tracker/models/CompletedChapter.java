package com.personal.tracker.models;

import java.util.Date;

public class CompletedChapter {
  private Long studentId;
  private Long chapterNumber;
  private Date completionDate;
  private String chapterTitle;
  private String bookTitle;

  public CompletedChapter(
      Long studentId,
      Long chapterNumber,
      Date completionDate,
      String bookTitle,
      String chapterTitle) {
    this.studentId = studentId;
    this.chapterNumber = chapterNumber;
    this.completionDate = completionDate;
    this.chapterTitle = chapterTitle;
    this.bookTitle = bookTitle;
  }

  public Long getStudentId() {
    return studentId;
  }

  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  public Long getChapterNumber() {
    return chapterNumber;
  }

  public void setChapterNumber(Long chapterNumber) {
    this.chapterNumber = chapterNumber;
  }

  public Date getCompletionDate() {
    return completionDate;
  }

  public void setCompletionDate(Date completionDate) {
    this.completionDate = completionDate;
  }

  public String getChapterTitle() {
    return chapterTitle;
  }

  public void setChapterTitle(String chapterTitle) {
    this.chapterTitle = chapterTitle;
  }

  public String getBookTitle() {
    return bookTitle;
  }

  public void setBookTitle(String bookTitle) {
    this.bookTitle = bookTitle;
  }

  public boolean studentIdIsEqual(Long studentId) {
    return studentId.equals(getStudentId());
  }

  public boolean chapterIsEqual(Long chapterNum, String book) {
    return (chapterNum.equals(getChapterNumber()) && book.equals(getBookTitle()));
  }
}
