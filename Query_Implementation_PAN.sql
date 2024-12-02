-- 1. Enter a new team into the database (1/month)
CREATE PROCEDURE EnterNewTeam
    @name VARCHAR(50),
    @type VARCHAR(50),
    @date_formed DATE,
    @team_leader VARCHAR(100)
AS
BEGIN
    INSERT INTO Team (name, type, date_formed, team_leader)
    VALUES (@name, @type, @date_formed, @team_leader);
END;
GO


-- 2. Enter a new client into the database and associate him or her with one or more teams (1/week)
CREATE PROCEDURE EnterNewClient
    @SSN CHAR(11),
    @person_name VARCHAR(100),
    @gender BIT,
    @profession VARCHAR(50),
    @mailing_address VARCHAR(255),
    @email VARCHAR(100),
    @phone VARCHAR(15),
    @on_mailing_list BIT,
    @doctor_name VARCHAR(100),
    @doctor_phone VARCHAR(15),
    @first_assigned_date DATE,
    @team_name VARCHAR(50),
    @is_active BIT
AS
BEGIN
    -- Insert client into Person table
    INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email, phone, on_mailing_list)
    VALUES (@SSN, @person_name, @gender, @profession, @mailing_address, @email, @phone, @on_mailing_list);

    -- Insert client into Client table
    INSERT INTO Client (client_ssn, doctor_name, doctor_phone, first_assigned_date)
    VALUES (@SSN, @doctor_name, @doctor_phone, @first_assigned_date);

    -- Associate client with team in Apart_Of table (set volunteer_ssn to NULL since no volunteer is involved)
    INSERT INTO Apart_Of (volunteer_ssn, client_ssn, team_name, is_active)
    VALUES (NULL, @SSN, @team_name, @is_active);
END;
GO


-- 3. Enter a new volunteer into the database and associate him or her with one or more teams (2/month)
CREATE PROCEDURE EnterNewVolunteer
    @SSN CHAR(11),
    @person_name VARCHAR(100),
    @gender BIT,
    @profession VARCHAR(50),
    @mailing_address VARCHAR(255),
    @email VARCHAR(100),
    @phone VARCHAR(15),
    @on_mailing_list BIT,
    @first_join_date DATE,
    @most_recent_training_date DATE,
    @most_recent_training_location VARCHAR(100),
    @team_name VARCHAR(50),
    @is_active BIT
AS
BEGIN
    -- Insert volunteer into Person table
    INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email, phone, on_mailing_list)
    VALUES (@SSN, @person_name, @gender, @profession, @mailing_address, @email, @phone, @on_mailing_list);

    -- Insert volunteer into Volunteer table
    INSERT INTO Volunteer (volunteer_ssn, first_join_date, most_recent_training_date, most_recent_training_location)
    VALUES (@SSN, @first_join_date, @most_recent_training_date, @most_recent_training_location);

    -- Associate volunteer with Team in Apart_Of table (set client_ssn to NULL since no client is involved)
    INSERT INTO Apart_Of (volunteer_ssn, client_ssn, team_name, is_active)
    VALUES (@SSN, NULL, @team_name, @is_active);
END;
GO


-- 4. Enter the number of hours a volunteer worked this month for a particular team (30/month)
CREATE PROCEDURE EnterVolunteerHours
    @volunteer_ssn CHAR(11),
    @team_name VARCHAR(50),
    @hours_worked INT
AS
BEGIN
    -- Update the hours worked for the specified volunteer and team
    UPDATE Apart_Of
    SET hours_worked = @hours_worked
    WHERE volunteer_ssn = @volunteer_ssn AND team_name = @team_name;
END;
GO


-- 5. Enter a new employee into the database and associate him or her with one or more teams (1/year)
CREATE PROCEDURE EnterNewEmployee
    @SSN CHAR(11),
    @person_name VARCHAR(100),
    @gender BIT,
    @profession VARCHAR(50),
    @mailing_address VARCHAR(255),
    @email VARCHAR(100),
    @phone VARCHAR(15),
    @on_mailing_list BIT,
    @salary DECIMAL(10, 2),
    @marital_status VARCHAR(10),
    @hire_date DATE,
    @team_name VARCHAR(50),
    @description VARCHAR(255),
    @report_date DATE
AS
BEGIN
    -- Insert employee into Person table
    INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email, phone, on_mailing_list)
    VALUES (@SSN, @person_name, @gender, @profession, @mailing_address, @email, @phone, @on_mailing_list);

    -- Insert employee into Employee table
    INSERT INTO Employee (employee_ssn, salary, marital_status, hire_date)
    VALUES (@SSN, @salary, @marital_status, @hire_date);

    -- Associate employee with team in Submits_Report table
    INSERT INTO Submits_Report (team_name, employee_ssn, description, date)
    VALUES (@team_name, @SSN, @description, @report_date);
END;
GO


-- 6. Enter an expense charged by an employee (1/day)
CREATE PROCEDURE EnterEmployeeExpense
    @employee_ssn CHAR(11),
    @expense_date DATE,
    @expense_amount DECIMAL(10, 2),
    @description VARCHAR(255)
AS
BEGIN
    INSERT INTO Expense (employee_ssn, expense_date, expense_amount, description)
    VALUES (@employee_ssn, @expense_date, @expense_amount, @description);
END;
GO


-- 7. Enter a new donor and associate him or her with several donations (1/day)
CREATE PROCEDURE EnterDonation
    @SSN CHAR(11),
    @person_name VARCHAR(100),
    @gender BIT,
    @profession VARCHAR(50),
    @mailing_address VARCHAR(255),
    @email VARCHAR(100),
    @phone VARCHAR(15),
    @on_mailing_list BIT,
    @is_anonymous BIT,
    @date DATE,
    @amount DECIMAL(10, 2),
    @type VARCHAR(50),
    @fundraising_campaign VARCHAR(100)
AS
BEGIN
    -- Insert donor into Person table (if doesn't exist)
    INSERT INTO Person (SSN, person_name, gender, profession, mailing_address, email, phone, on_mailing_list)
    VALUES (@SSN, @person_name, @gender, @profession, @mailing_address, @email, @phone, @on_mailing_list);

    -- Insert donor into Donor table
    INSERT INTO Donor (donor_ssn, is_anonymous)
    VALUES (@SSN, @is_anonymous);

    -- Insert first donation for the donor
    INSERT INTO Donation (donation_ssn, date, amount, type, fundraising_campaign)
    VALUES (@SSN, @date, @amount, @type, @fundraising_campaign);
END;
GO


-- 8. Retrieve the name and phone number of the doctor of a particular client (1/week)
CREATE PROCEDURE RetrieveDoctorForClient
    @client_ssn CHAR(11)
AS
BEGIN
    SELECT doctor_name, doctor_phone
    FROM Client
    WHERE client_ssn = @client_ssn;
END;
GO


-- 9. Retrieve the total amount of expenses charged by each employee for a particular 
-- period of time. The list should be sorted by the total amount of expenses (1/month)
CREATE PROCEDURE RetrieveEmployeeExpenses
    @start_date DATE,
    @end_date DATE
AS
BEGIN
    SELECT employee_ssn, SUM(expense_amount) AS total_expenses
    FROM Expense
    WHERE expense_date BETWEEN @start_date AND @end_date
    GROUP BY employee_ssn
    ORDER BY total_expenses DESC;
END;
GO


-- 10. Retrieve the list of volunteers that are members of teams that support a particular client (4/year)
CREATE PROCEDURE RetrieveVolunteersForClient
    @client_ssn CHAR(11)
AS
BEGIN
    SELECT DISTINCT p.person_name AS volunteer_name, p.phone AS volunteer_phone
    FROM Apart_Of a1
    JOIN Apart_Of a2 ON a1.team_name = a2.team_name
    JOIN Volunteer v ON a2.volunteer_ssn = v.volunteer_ssn
    JOIN Person p ON v.volunteer_ssn = p.SSN
    WHERE a1.client_ssn = @client_ssn AND a1.is_active = 1 AND a2.is_active = 1;
END;
GO


-- 11. Retrieve the names of all teams that were founded after a particular date (1/month)
CREATE PROCEDURE RetrieveTeamsByDate
    @date_formed DATE
AS
BEGIN
    SELECT name AS team_name
    FROM Team
    WHERE date_formed > @date_formed
    ORDER BY date_formed;
END;
GO


-- 12. Retrieve the names, social security numbers, contact information, 
-- and emergency contact information of all people in the database (1/week)
CREATE PROCEDURE RetrievePersonAndEmergencyContacts
AS
BEGIN
    SELECT p.person_name, p.SSN, p.mailing_address, p.email, p.phone AS person_phone, 
           ec.contact_name AS emergency_contact_name, ec.phone AS emergency_contact_phone, ec.relationship
    FROM Person p
    LEFT JOIN Has_Contact hc ON p.SSN = hc.person_ssn
    LEFT JOIN Emergency_Contact ec ON hc.contact_phone = ec.phone;
END;
GO


-- 13. Retrieve the name and total amount donated by donors that are also employees. The list should be 
-- sorted by the total amount of the donations, and indicate if each donor wishes to remain anonymous (1/week)
CREATE PROCEDURE RetrieveEmployeeDonors
AS
BEGIN
    SELECT p.person_name AS donor_name, SUM(d.amount) AS total_donated, donor.is_anonymous
    FROM Donor donor
    JOIN Employee e ON donor.donor_ssn = e.employee_ssn
    JOIN Person p ON donor.donor_ssn = p.SSN
    JOIN Donation d ON donor.donor_ssn = d.donation_ssn
    GROUP BY p.person_name, donor.is_anonymous
    ORDER BY total_donated DESC;
END;
GO


-- 14. Increase the salary by 10% of all employees to whom more than one team must report to (1/year)
CREATE PROCEDURE IncreaseSalaryForMultiTeamManagers
AS
BEGIN
    WITH EmployeesWithMultipleTeams AS (
        SELECT employee_ssn
        FROM Submits_Report
        GROUP BY employee_ssn
        HAVING COUNT(team_name) > 1
    )
    -- Update their salary by 10%
    UPDATE Employee
    SET salary = salary * 1.10
    WHERE employee_ssn IN (SELECT employee_ssn FROM EmployeesWithMultipleTeams);
END;
GO


-- 15. Delete all clients who do not have health insurance and whose value of importance for transportation is less than 5 (4/year)
CREATE PROCEDURE DeleteClientsNoInsuranceLowTransport
AS
BEGIN
    -- First delete from Needs table to remove foreign key dependency
    DELETE FROM Needs
    WHERE client_ssn IN (
        SELECT n.client_ssn
        FROM Needs n
        LEFT JOIN Has_Insurance hi ON n.client_ssn = hi.client_ssn
        WHERE n.need = 'transportation' AND n.importance < 5 AND hi.policy_id IS NULL
    );

    -- Then delete from Client table
    DELETE FROM Client
    WHERE client_ssn IN (
        SELECT n.client_ssn
        FROM Needs n
        LEFT JOIN Has_Insurance hi ON n.client_ssn = hi.client_ssn
        WHERE n.need = 'transportation' AND n.importance < 5 AND hi.policy_id IS NULL
    );
END;
GO

CREATE PROCEDURE RetrieveMailingList
AS
BEGIN
    SELECT person_name, mailing_address
    FROM Person
    WHERE on_mailing_list = 1;
END;
GO
