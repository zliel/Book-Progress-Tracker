# Book-Progress-Tracker
This is a progress tracker I'm making to track my progress throughout different books.

## Table of Contents
* [Table Structure](#table-structure)
  * [Student Table](#student-table)
  * [Chapter Table](#chapter-table)
  * [Student_Progress](#student_progress)


## Table Structure
The top levels have the column names and associated types

### Student Table
This table keeps track of students, including their IDs and Names
| STUDENT_ID(Long) | FIRST_NAME(String) | LAST_NAME(String) |
| :---: | :---: | :---: |
| 1 | ExampleFirst | ExampleLast |

### Chapter Table
This table keeps track of how many chapters there are and their associated titles. 
| CHAPTER_ID(Long) | CHAPTER_TITLE(String) |
| :---: | :---: |
| 1 | Chapter 1: Example |

### Student_Progress
This table tracks students' progress, containing the ID of the student who completed the chapter, the ID of the chapter that's been completed, and the date that the chapter has been completed on. 
| STUDENT_ID(Long) | CHAPTER_ID(Long) | COMPLETION_DATE(Date) |
| :---: | :---: | :---: |
| 1 | 1 | 2021-1-3 |
