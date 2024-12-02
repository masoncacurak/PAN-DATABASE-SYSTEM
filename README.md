# Patient Assistance Network Database System

This project implements a **Patient Assistance Network (PAN) Database System**, developed as part of my Database Management Systems course. PAN is a non-profit organization that provides support and care for patients. This project focuses on designing, implementing, and testing a database system to efficiently manage data related to PAN's operations, including clients, volunteers, employees, teams, and donors.

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [ER Diagram](#er-diagram)
- [Relational Database](#relational-database)
- [Queries](#queries)
- [Usage](#usage)
- [Technologies Used](#technologies-used)
- [Setup](#setup)

## Introduction
The PAN Database System is designed to:
- Track multiple categories of people (clients, volunteers, employees, donors).
- Manage teams and their activities.
- Maintain information about clients' needs and insurance policies.
- Log donations and expenses.
- Generate reports and manage queries efficiently.

## Features
1. **Entity Management**:
   - Add and manage clients, volunteers, employees, teams, and donors.
   - Track emergency contacts, insurance policies, and client needs.

2. **Query Execution**:
   - Predefined SQL queries for frequent operations such as retrieving doctor details, calculating expenses, and generating team reports.
   - Support for importing/exporting data to/from files.

3. **Database Integration**:
   - Implemented using Azure SQL Database for scalability and reliability.

4. **Java Program**:
   - A menu-driven Java application to interface with the database using JDBC.

## ER Diagram
The Entity-Relationship diagram provides a detailed representation of the PAN database. It includes:
- Relationships between clients, teams, volunteers, employees, and donors.
- Key attributes and constraints.

## Relational Database
The relational database schemas derived from the ER diagram include:
- Tables with appropriate constraints.
- Indexes optimized for query performance.

## Queries
The project supports 15 predefined queries, including:
1. Entering a new team into the database
2. Entering a new client into the database and associating him or her with one or more teams
3. Entering a new volunteer into the database and associating him or her with one or more teams
4. Entering the number of hours a volunteer worked this month for a particular team
5. Entering a new employee into the database and associating him or her with one or more teams
6. Entering an expense charged by an employee
7. Entering a new donor and associating him or her with several donations
8. Retrieving the name and phone number of the doctor of a particular client
9. Retrieving the total amount of expenses charged by each employee for a particular period of time. The list should be sorted by the total amount of expenses
10. Retrieving the list of volunteers that are members of teams that support a particular client
11. Retrieving the names of all teams that were founded after a particular date
12. Retrieving the names, social security numbers, contact information, and emergency contact information of all people in the database
13. Retrieving the name and total amount donated by donors that are also employees. The list should be sorted by the total amount of the donations, and indicate if each donor wishes to remain anonymous
14. Increasing the salary by 10% of all employees to whom more than one team must report
15. Deleting all clients who do not have health insurance and whose value of importance for transportation is less than 5

## Usage
1. Execute the **SQL scripts** to create and populate the database on Azure SQL Database.
2. Run the **Java application** to interact with the database.
3. Use the menu options to perform operations such as adding entries, querying data, and exporting results.

## Technologies Used
- **Database**: Azure SQL Database
- **Programming Language**: SQL & Java
- **Frameworks**: JDBC
- **Tools**: Azure Portal & IntelliJ IDEA or Eclipse

## Setup
1. Clone this repository:
   ```bash
   git clone https://github.com/your-username/patient-assistance-network.git
   ```
2. Setup Azure SQL Database and execute the SQL scripts provided in the `sql` directory.
3. Compile and run the Java application:
   ```bash
   javac -cp <path-to-jdbc-driver> Main.java
   java -cp .:<path-to-jdbc-driver> Main
   ```
4. Follow the menu options to perform operations.
