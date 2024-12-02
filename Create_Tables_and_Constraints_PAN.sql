-- Drop tables in reverse dependency order
DROP TABLE IF EXISTS Made_By_Card;
DROP TABLE IF EXISTS Made_By_Check;
DROP TABLE IF EXISTS Donation;
DROP TABLE IF EXISTS Donor;
DROP TABLE IF EXISTS Expense;
DROP TABLE IF EXISTS Apart_Of;
DROP TABLE IF EXISTS Submits_Report;
DROP TABLE IF EXISTS Team;
DROP TABLE IF EXISTS Employee;
DROP TABLE IF EXISTS Volunteer;
DROP TABLE IF EXISTS Has_Insurance;
DROP TABLE IF EXISTS Insurance_Policy;
DROP TABLE IF EXISTS Needs;
DROP TABLE IF EXISTS Client;
DROP TABLE IF EXISTS Has_Contact;
DROP TABLE IF EXISTS Emergency_Contact;
DROP TABLE IF EXISTS Person;

CREATE TABLE Person (
    SSN CHAR(11) PRIMARY KEY,
    person_name VARCHAR(100),
    gender BIT, -- Changed gender from CHAR --> BIT
    profession VARCHAR(50),
    mailing_address VARCHAR(255),
    email VARCHAR(100),
    phone VARCHAR(15),
    on_mailing_list BIT
);

CREATE TABLE Emergency_Contact (
    phone VARCHAR(15) PRIMARY KEY,
    contact_name VARCHAR(100),
    relationship VARCHAR(50)
);

CREATE TABLE Has_Contact (
    person_ssn CHAR(11),
    contact_phone VARCHAR(15),
    PRIMARY KEY (person_ssn, contact_phone),
    FOREIGN KEY (person_ssn) REFERENCES Person(SSN),
    FOREIGN KEY (contact_phone) REFERENCES Emergency_Contact(phone)
);

CREATE TABLE Client (
    client_ssn CHAR(11),
    doctor_name VARCHAR(100),
    doctor_phone VARCHAR(15),
    first_assigned_date DATE,
    PRIMARY KEY (client_ssn),
    FOREIGN KEY (client_ssn) REFERENCES Person(SSN)
);

CREATE TABLE Needs (
    client_ssn CHAR(11),
    need VARCHAR(255),
    importance INT CHECK (importance BETWEEN 1 AND 10),
    PRIMARY KEY (client_ssn, need),
    FOREIGN KEY (client_ssn) REFERENCES Client(client_ssn)
);

CREATE TABLE Insurance_Policy (
    policy_id INT PRIMARY KEY,
    provider_name VARCHAR(100),
    provider_address VARCHAR(255),
    policy_type VARCHAR(50)
);

CREATE TABLE Has_Insurance (
    client_ssn CHAR(11),
    policy_id INT,
    PRIMARY KEY (client_ssn, policy_id),
    FOREIGN KEY (client_ssn) REFERENCES Client(client_ssn),
    FOREIGN KEY (policy_id) REFERENCES Insurance_Policy(policy_id)
);

CREATE TABLE Volunteer (
    volunteer_ssn CHAR(11),
    first_join_date DATE,
    most_recent_training_date DATE,
    most_recent_training_location VARCHAR(100),
    PRIMARY KEY (volunteer_ssn),
    FOREIGN KEY (volunteer_ssn) REFERENCES Person(SSN)
);

CREATE TABLE Employee (
    employee_ssn CHAR(11),
    salary DECIMAL(10, 2),
    marital_status VARCHAR(10),
    hire_date DATE,
    PRIMARY KEY(employee_ssn),
    FOREIGN KEY (employee_ssn) REFERENCES Person(SSN)
);

CREATE TABLE Team (
    name VARCHAR(50) PRIMARY KEY,
    type VARCHAR(50),
    date_formed DATE,
    team_leader VARCHAR(100)
);

CREATE TABLE Submits_Report (
    team_name VARCHAR(50),
    employee_ssn CHAR(11),
    description VARCHAR(255),
    date DATE,
    PRIMARY KEY (team_name, employee_ssn),
    FOREIGN KEY (team_name) REFERENCES Team(name),
    FOREIGN KEY (employee_ssn) REFERENCES Employee(employee_ssn)
);

CREATE TABLE Apart_Of (
    apart_of_id INT IDENTITY PRIMARY KEY, -- Surrogate primary key
    volunteer_ssn CHAR(11),
    client_ssn CHAR(11),
    team_name VARCHAR(50) NOT NULL,
    is_active BIT,
    hours_worked INT,
    FOREIGN KEY (volunteer_ssn) REFERENCES Volunteer(volunteer_ssn),
    FOREIGN KEY (client_ssn) REFERENCES Client(client_ssn),
    FOREIGN KEY (team_name) REFERENCES Team(name),
    UNIQUE (volunteer_ssn, client_ssn, team_name) -- Unique constraint for the three columns
);

CREATE TABLE Expense (
    employee_ssn CHAR(11),
    expense_date DATE,
    expense_amount DECIMAL(10, 2),
    description VARCHAR(255),
    PRIMARY KEY (employee_ssn, expense_date),
    FOREIGN KEY (employee_ssn) REFERENCES Employee(employee_ssn)
);

CREATE TABLE Donor (
    donor_ssn CHAR(11),
    is_anonymous BIT,
    PRIMARY KEY (donor_ssn),
    FOREIGN KEY (donor_ssn) REFERENCES Person(SSN)
);

CREATE TABLE Donation (
    donation_ssn CHAR(11),
    date DATE,
    amount DECIMAL(10, 2),
    type VARCHAR(50),
    fundraising_campaign VARCHAR(100),
    PRIMARY KEY (donation_ssn, date),
    FOREIGN KEY (donation_ssn) REFERENCES Donor(donor_ssn)
);

CREATE TABLE Made_By_Check (
    check_ssn CHAR(11),
    date DATE,
    check_number VARCHAR(20),
    PRIMARY KEY (check_ssn, date),
    FOREIGN KEY (check_ssn, date) REFERENCES Donation(donation_ssn, date)
);

CREATE TABLE Made_By_Card (
    card_ssn CHAR(11),
    date DATE,
    card_number VARCHAR(20),
    card_type VARCHAR(20),
    expiration_date DATE,
    PRIMARY KEY (card_ssn, date),
    FOREIGN KEY (card_ssn, date) REFERENCES Donation(donation_ssn, date)
);
