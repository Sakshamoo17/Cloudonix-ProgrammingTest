# Cloudonix Programming Test

This is a sample project for the Cloudonix programming test.
This is a Java-based project for the Cloudonix programming test. The project contains a simple HTTP server that exposes an API endpoint for analyzing text.

## Description

The project is a Java-based web application that analyzes text input and returns the closest matching word from a pre-defined list of words. The application is built using the Vert.x framework.

# Requirements
To build and run this project, you will need:

Java 8 or later
Maven

## Built and run

Build and Run
To build and run the project, follow these steps:

1. Clone the repository to your local machine: git clone https://github.com/Sakshamoo17/Cloudonix-ProgrammingTest.git
2. Change into the project directory: cd Cloudonix-ProgrammingTest
3. Build the project with Maven: mvn package
4. Run the project: java -jar target/Cloudonix-ProgrammingTest-1.0-SNAPSHOT-fat.jar
5. The server will start and listen on port 8080. You can access it at http://localhost:8080.

## API Endpoint
The HTTP server exposes a single API endpoint at /analyze. You can send a POST request to this endpoint with a JSON payload that contains a text field. The server will analyze the text and return the closest matching word based on character value and lexical order.

# Project Strutcure
The project is structured as follows:

1. 'src/main/java': Contains the Java source code for the project
  ~'com.vertex.main': Contains the Main class which starts the HTTP server
  ~'VertextProject.Vertexproject': Contains the TextAnalyzerService class which defines the HTTP routes and implements the text analysis logic
2. 'src/test/java': Contains the JUnit test cases for the project
  ~'VertextProject.Vertexproject': Contains the AppTest class which includes a single test case for the project

## Contributors

- [Saksham Sharma](https://github.com/Sakshamoo17)

## License

This project is licensed under the [MIT License](LICENSE).
