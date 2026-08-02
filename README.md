# 🎓 Student Management System (Java)

A simple **Student Management System** built with **Java Swing**, **JDBC**, and **MySQL**. This project demonstrates CRUD (Create, Read, Update, Delete) operations with a clean desktop user interface.

## 📸 Screenshot

<p align="center">
  <img src="https://github.com/Amit-Kundu-io/Student-Management-System-Java/blob/main/Images/Img01.png" alt="Student Management System" width="900"/>
</p>

---

## ✨ Features

- ➕ Add Student
- ✏️ Update Student
- ❌ Delete Student
- 🔍 Search Student by ID
- 📋 View All Students
- 💾 MySQL Database Integration
- 🖥️ Java Swing Desktop UI

---

## 🛠️ Tech Stack

- Java
- Swing
- JDBC
- MySQL
- Maven



---

## 🗄️ Database

```sql
CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    course VARCHAR(50)
);
```

---

## 🚀 Getting Started

1. Clone the repository

```bash
git clone https://github.com/Amit-Kundu-io/Student-Management-System-Java.git
```

2. Create the MySQL database.

3. Update your database credentials in `DatabaseConfig.java`.

```java
private static final String URL = "jdbc:mysql://localhost:3306/Students";
private static final String USER = "root";
private static final String PASSWORD = "your_password";
```

4. Run the project.

---

## 📖 What I Learned

- Java Swing UI Development
- JDBC CRUD Operations
- MySQL Integration
- PreparedStatement
- ResultSet Mapping
- Service Layer Architecture
- Object-Oriented Programming

---

## 👨‍💻 Author

**Amit Kundu**

GitHub: https://github.com/Amit-Kundu-io
