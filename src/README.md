# 💊 Pharma App — CSI DevOps Project

A lightweight Spring Boot web application for a pharmaceutical company, showcasing product information like drug usage, safety precautions, and contact details. Built with Java 17, Thymeleaf, and styled with a modern UI.

---

## 🗂️ Project Structure

```bash
pharma-app/
├── src/
│   ├── main/
│   │   ├── java/com/pharma/
│   │   │   ├── model/Drug.java
│   │   │   ├── service/DrugService.java
│   │   │   ├── controller/DrugController.java
│   │   │   ├── controller/ContactController.java
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── index.html
│   │       │   ├── detail.html
│   │       │   ├── contact.html
│   │       ├── static/
│   │       │   └── style.css
│   └── test/
│       └── java/com/pharma/
│           ├── service/DrugServiceTest.java
│           └── controller/DrugControllerTest.java
├── pom.xml
├── Dockerfile

```

---

## 🚀 How to Run Locally (Manual)

### ✅ Prerequisites

* Java 17+
* Maven 3.6+

### 🔄 Build & Run

```bash
# Navigate to project root
cd pharma-app

# Build the application
mvn clean install

# Run the application
mvn spring-boot:run
```

### 📲 Access the UI

Once running, open in your browser:

```
http://localhost:8080
```

---

## 🚧 How to Run via Docker

> Ensure Docker is installed and running on your machine.

### 🔧 Build Docker Image

```bash
docker build -t pharma-app:latest .
```

### ▶️ Run Container

```bash
docker run -d -p 8080:8080 --name pharma-ui pharma-app:latest
```

### 📲 Access the App

```
http://localhost:8080
```

---

## 📊 Features

* View all available drugs with name, category, and manufacturer
* Detailed drug info including usage, safety instructions
* Static contact page
* Search functionality
* Fully styled UI with orangered theme and responsive layout

---

## 📚 Tech Stack

* Java 17
* Spring Boot
* Maven
* Thymeleaf
* CSS (Custom)
* Docker

---

## ✨ Author

**Vikas** — *CSI DevOps Project*

---
