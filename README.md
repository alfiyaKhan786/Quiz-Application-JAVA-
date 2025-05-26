Introduction

The Online Quiz Application is a Java-based system that allows users to take quizzes on various topics. The application supports multiple-choice questions, tracks user progress, and provides feedback on quiz performance.

System Requirements

Java 8 or later
JavaFX or Swing for GUI
SQLite or MySQL database
JDBC driver for database interaction
Setup Instructions

Clone the repository or download the source code.
Install the required dependencies, including JavaFX or Swing and the JDBC driver.
Create a database schema using the provided SQL script.
Configure the database connection properties in the db.properties file.
Run the application using the Main class.
User Guide

User Authentication
Create an account by providing a username, password, and email address.
Log in with existing credentials.
Quiz Management
Administrators can create quizzes with multiple-choice questions.
Each question has a title, options, and correct answer(s).
Administrators can edit and delete quizzes.
Quiz Taking
Users can select and take quizzes from the available list of topics.
Display one question at a time with options for the user to select the answer(s).
Provide feedback on each question (correct/incorrect) immediately after the user submits their answer.
Scoring and Progress Tracking
Calculate and display the user's score at the end of each quiz.
Track user progress by recording quiz attempts and scores.
Allow users to view their past quiz attempts and scores.
Code Documentation

The code is organized into the following packages:

com.quizapp.model: contains the data models for users, quizzes, questions, and quiz results.
com.quizapp.dao: contains the data access objects for database interaction.
com.quizapp.service: contains the business logic for quiz management and user authentication.
com.quizapp.ui: contains the user interface components for the application.
Assumptions and Limitations

The application assumes that the database schema is created and configured correctly.
The application does not support advanced quiz features, such as timed quizzes or quiz categories.
The application does not have a robust error handling mechanism.
Database Schema

The database schema consists of the following tables:

users: stores user information, including username, password, and email address.
quizzes: stores quiz information, including title, description, and questions.
questions: stores question information, including title, options, and correct answer(s).
quiz_results: stores quiz results, including user ID, quiz ID, and score.
Error Handling and Validation

The application uses try-catch blocks to handle exceptions and provides informative error messages to users. The application also validates user input to prevent errors and ensure data integrity.

Security Considerations

The application uses password hashing and salting to securely store user passwords. The application also uses HTTPS to encrypt data transmitted between the client and server.

Leaderboard

The leaderboard is an optional feature that displays top scorers for each quiz or overall. The leaderboard is implemented using a separate database table that stores user scores and quiz IDs.

SQL Script

The SQL script to create the database schema is as follows:

sql
Run
Copy code
CREATE TABLE users (
  id INT PRIMARY KEY,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL
);

CREATE TABLE quizzes (
  id INT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT NOT NULL
);

CREATE TABLE questions (
  id INT PRIMARY KEY,
  quiz_id INT NOT NULL,
  title VARCHAR(255) NOT NULL,
  options TEXT NOT NULL,
  correct_answer VARCHAR(255) NOT NULL
);

CREATE TABLE quiz_results (
  id INT PRIMARY KEY,
  user_id INT NOT NULL,
  quiz_id INT NOT NULL,
  score INT NOT NULL
);

CREATE TABLE leaderboard (
  id INT PRIMARY KEY,
  user_id INT NOT NULL,
  quiz_id INT NOT NULL,
  score INT NOT NULL
);
