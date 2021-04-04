package com.personal.tracker.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Chapter {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long chapterId;

  private Long chapterKey;
  private String chapterTitle;
  private String bookTitle;

  public Chapter(Long chapterKey, String chapterTitle, String bookTitle) {
    this.chapterKey = chapterKey;
    this.chapterTitle = chapterTitle;
    this.bookTitle = bookTitle;
  }

  public Long getChapterId() {
    return chapterId;
  }

  public void setChapterId(Long chapterId) {
    this.chapterId = chapterId;
  }

  public Long getChapterKey() {
    return chapterKey;
  }

  public void setChapterKey(Long chapterKey) {
    this.chapterKey = chapterKey;
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

  @Override
  public String toString() {
    return "Chapter(" +
        "chapterId = " + chapterId + ", " +
        "chapterKey = " + chapterKey + ", " +
        "chapterTitle = " + chapterTitle + ", " +
        "bookTitle = " + bookTitle + ")";
  }
}
