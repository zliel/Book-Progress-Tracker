# Book-Progress-Tracker
This is a progress tracker I'm making to track my progress throughout different books.
<br> <br>
![Java CI with Gradle](https://github.com/zliel/Book-Progress-Tracker/workflows/Java%20CI%20with%20Gradle/badge.svg)

## Table of Contents
* [Pictures!](#pictures)
* [Tech Used](#tech-used)
* [Table Structure](#table-structure)
  * [Student Table](#student-table)
  * [Chapter Table](#chapter-table)
  * [Student_Progress](#student_progress)

## Pictures!
Here's the student tab!
![Image of the Student Tab](media/Student_Screen.png)

Here's the chapter tab!
![Image of the Chapters Tab](media/Chapters_Screen.png)

And here's the completed chapter tab!
![Image of the Completed Chapters Tab](media/Completed_Chapter_Screen.png)

## Tech Used
- Core Java 15
- GUI Framework: JavaFX 15
- Database: H2 Embedded
- Data Persistence: Hibernate

## Table Structure
The top levels have the column names and associated types

### Student Table
This table keeps track of students, including their IDs and Names
| ID(Long) | FIRSTNAME(String) | LASTNAME(String) |
| :---: | :---: | :---: |
| 1 | ExampleFirst | ExampleLast |

### Chapter Table
This table keeps track of how many chapters there are, and their associated titles. 
| CHAPTERTITLE(String) | CHAPTERID(Long) | CHAPTERNUM | BOOKTITLE |
| :---: | :---: | :---: | :---: |
Chapter 1: Example | 1 | 1 | The Example Book |

### Student_Progress
This table tracks students' progress, containing the ID of the student who completed the chapter, the ID of the chapter that's been completed, and the date that the chapter has been completed on. 
| COMPLETEDCHAPTERID | STUDENTID(Long) | CHAPTERNUMBER(Long) | COMPLETIONDATE(Date) | BOOKTITLE | CHAPTERTITLE |
| :---: | :---: | :---: | :---: | :---: | :---: |
| 1 | 1 | 1 | 2021-1-3 | The Example Book | Chapter 1: Example
