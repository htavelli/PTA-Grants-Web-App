# PTA-Grants-Web-App
Personal project to hone skills with request/response, variable scoping, and MVC architecture

## Background
In our PTA, it is cumbersome to search old grant applications and approvals.  Teachers need an easy and quick way to apply for PTA Grant Funds.

## Functionality
Teachers verify their identity with email or department name and can then view current grant applications or make new applications during open grant cycles.

The Committee Admin must log in and can then add, delete, and update grant cycles.  This is the back-end functionality that must be in place for teachers to successfully view or add applications.  However, the more interesting functionality is that the admin can approve grants, search all grants, and view current grants.   

## Implementation Details
The web app is built with servlets, JSP, JSTL, and EL.  The current SQL database is a mySQL database connected with a jconnector and the server is a Tomcat 9 server.  

### Design Decisions
While I am learning Spring, I decided to move forward with JSP initially so that I could gain confidence in scoping across sessions and requests, setting up filters, and utilizing separation of business logic from data access.  Next up, I will dive more fully into Spring to take advantage of the built-in functionality that makes source code shorter, easier to read, and faster to implement.  

