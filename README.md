# PTA-Grants-Web-App
Personal project to hone skills with request/response, variable scoping, and MVC architecture

## Background
In our PTA, it is cumbersome to search old grant applications and approvals.  Teachers need an easy and quick way to apply for PTA Grant Funds.

## Functionality
Teachers verify their identity with email or department name and can then view current grant applications or make new applications during open grant cycles.

The Committee Admin must log in and can then add, delete, and update grant cycles.  This is the back-end functionality that must be in place for teachers to successfully view or add applications.  However, the more interesting functionality is that the admin can approve grants, search all grants, and view current grants.   

## Implementation Details
The web app is built with servlets, JSP, JSTL, and EL.  It uses a mySQL database and a Tomcat 9 server.  

### Design Decisions
While I am learning Spring, I decided to move forward with JSP initially so that I could gain confidence in scoping across sessions and requests, setting up filters, and utilizing separation of business logic from data access.  Next up, I will dive more fully into Spring to take advantage of the built-in functionality that makes source code shorter, easier to read, and faster to implement.  

## Architecture

### Webcontent
Designed for WAR deployment with web content in its own subfolders of html, css, js.  There are 3 jsp pages.

### Packages within src

#### Beans
Contains pojo beans for transfer of data in an object oriented fashion.

#### DAO
Data access layer that queries the database.  Separated into several classes that handle different parts of functionality, such as Admin Cycle data access or Teacher data access.

#### Filters
Contains filters to verify sessions are set for teachers/departments or admin.
Sets request or session variables for html dropdown menu content, depending on the situation.

#### Servlets
All servlets for functionality of the web app.

#### Resources
Contains a few enums and properties files with error messages and school details.  Created for maintainability.



#### Utils
Static formatting and validation utility class and class loader for properties file.  Created for maintainability.


## Database Design
There are 7 tables for this application.

### Admin
Contains username and BCrypt hashed password for admin login verification.

### Grant_Rounds
Contains details on start date, end date and whether the round is complete.

### Teachers
Contains contact information and name for individual teachers.

### Departments
Contains contact information for the point of contact and department details.

### Projects
Contains all details on a single project/grant application like start date, future use, total cost.

### Items
Each row is linked to a particular project with a foreign key and contains details about the individual items of a project.

### Round_Submitter_Project
Links teacher or department (depending on the particular application), projects, and grant_round with foreign keys.


