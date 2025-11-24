# Fitness Tracker Application

A complete Java GUI-based Fitness Tracking Application using Java Swing, OOP, JDBC, and MVC architecture.

## Prerequisites

1.  **Java Development Kit (JDK)**: Ensure JDK 8 or higher is installed.
2.  **MySQL Database**: You need a running MySQL server.
3.  **MySQL JDBC Driver**: Download the `mysql-connector-j-x.x.x.jar` and add it to your project's classpath.

## Setup Instructions

1.  **Database Setup**:
    *   Open your MySQL client (Workbench, Command Line, etc.).
    *   Run the script provided in `database.sql` to create the database and tables.
    *   **Important**: Update `src/dao/DBConnection.java` with your MySQL username and password.
        ```java
        private static final String USER = "root";
        private static final String PASSWORD = "your_password";
        ```

2.  **Compilation**:
    *   Navigate to the project root directory.
    *   Compile the Java files (ensure the JDBC driver is in the classpath).
        ```bash
        javac -cp ".;path/to/mysql-connector.jar" src/Main.java src/model/*.java src/dao/*.java src/service/*.java src/ui/*.java src/ui/forms/*.java src/util/*.java
        ```

3.  **Running the Application**:
    *   Run the `Main` class.
        ```bash
        java -cp ".;path/to/mysql-connector.jar" Main
        ```

## Features

*   **Login**: Admin and User roles.
    *   **Admin Credentials**: Email: `admin@fitness.com`, Password: `admin123`
*   **Admin Dashboard**: Manage users, approve content, system settings, view stats.
*   **User Dashboard**: Log workouts, track progress, join challenges, manage profile.
*   **Multithreading**: Background tasks for logging and report generation.

## Project Structure

*   `src/model`: Data models (OOP concepts).
*   `src/dao`: Database access objects (JDBC).
*   `src/service`: Business logic and background services.
*   `src/ui`: Swing GUI classes.
*   `src/util`: Utility classes (Validator, Exceptions).
