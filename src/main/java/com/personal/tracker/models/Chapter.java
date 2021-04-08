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

  private Long chapterNum;
  private String chapterTitle;
  private String bookTitle;

  public Chapter() {}

  public Chapter(Long chapterNum, String chapterTitle, String bookTitle) {
    this.chapterNum = chapterNum;
    this.chapterTitle = chapterTitle;
    this.bookTitle = bookTitle;
  }

  public Long getChapterId() {
    return chapterId;
  }

  public void setChapterId(Long chapterId) {
    this.chapterId = chapterId;
  }

  public Long getChapterNum() {
    return chapterNum;
  }

  public void setChapterNum(Long chapterKey) {
    this.chapterNum = chapterKey;
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
        "chapterNum = " + chapterNum + ", " +
        "chapterTitle = " + chapterTitle + ", " +
        "bookTitle = " + bookTitle + ")";
  }
}
