Blog Management Application

A Blog Management Application built with Spring Boot<br/>
The Application is responsible for managing blogs and major parameters users can interact with are the title and blog content.<br/>

Security in the application has been ensured through use of token for user authentication,<br/>
therefore before any operations are performed on the application, a user has to be logged in an the token verified before proceeding with any other blog operations.<br/>

-- Packages<br/>
<h2>Main Class</h3>
BlogsApplication.java<br/>
This is the main class.<br/>
The entry point for the Spring application

<h2>Other Class</h3>
Blog.java<br/>
User.java<br/>
A class responsible for the getters and setters for the application.<br/>

<h2>controller</h3>
BlogController.java<br/>
A Spring Boot Controller responsible for: <br/>
CRUD Operations of a bog and a user <br />

<h2>Exception</h3>
<b>CustomException.java</b> <br />
A Java class used to handle any exceptions that could occur during testing of the application
<br />

<br /> <br />
<h2>To run:</h2>
1. Start up SpringBoot Project
 https://spring.io/quickstart

2. Run the project

```
 .\gradlew.bat bootRun
