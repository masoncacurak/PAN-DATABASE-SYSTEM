import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Sample {
	
    // Database credentials
	final static String HOSTNAME = "cacu0000-sql-server.database.windows.net";
	final static String DBNAME = "cs-dsa-4513-sql-db";
	final static String USERNAME = "cacu0000";
	final static String PASSWORD = "Cacurak01312003";

    // Database connection string
    final static String URL = String.format(
        "jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
        HOSTNAME, DBNAME, USERNAME, PASSWORD
    );

    // Query templates
    // Example: "SELECT * FROM Faculty;";
    final static String QUERY_TEMPLATE_1 = "EXEC EnterNewTeam @name = ?, @type = ?, @date_formed = ?, @team_leader = ?;";
    final static String QUERY_TEMPLATE_2 = "EXEC EnterNewClient\n"
    		+ "    @SSN = ?,\n"
    		+ "    @person_name = ?,\n"
    		+ "    @gender = ?,\n"
    		+ "    @profession = ?,\n"
    		+ "    @mailing_address = ?,\n"
    		+ "    @email = ?,\n"
    		+ "    @phone = ?,\n"
    		+ "    @on_mailing_list = ?,\n"
    		+ "    @doctor_name = ?,\n"
    		+ "    @doctor_phone = ?,\n"
    		+ "    @first_assigned_date = ?,\n"
    		+ "    @team_name = ?,\n"
    		+ "    @is_active = ?;";
    final static String QUERY_TEMPLATE_3 = "EXEC EnterNewVolunteer\n"
    		+ "    @SSN = ?,\n"
    		+ "    @person_name = ?,\n"
    		+ "    @gender = ?,\n"
    		+ "    @profession = ?,\n"
    		+ "    @mailing_address = ?,\n"
    		+ "    @email = ?,\n"
    		+ "    @phone = ?,\n"
    		+ "    @on_mailing_list = ?,\n"
    		+ "    @first_join_date = ?,\n"
    		+ "    @most_recent_training_date = ?,\n"
    	    + "    @most_recent_training_location = ?,\n"
    	    + "    @team_name = ?,\n"
    		+ "    @is_active = ?;";
    final static String QUERY_TEMPLATE_4 = "EXEC EnterVolunteerHours @volunteer_ssn = ?, @team_name = ?, @hours_worked = ?;";
    final static String QUERY_TEMPLATE_5 = "EXEC EnterNewEmployee\n"
    		+ "    @SSN = ?,\n"
    		+ "    @person_name = ?,\n"
    		+ "    @gender = ?,\n"
    		+ "    @profession = ?,\n"
    		+ "    @mailing_address = ?,\n"
    		+ "    @email = ?,\n"
    		+ "    @phone = ?,\n"
    		+ "    @on_mailing_list = ?,\n"
    		+ "    @salary = ?,\n"
    		+ "    @marital_status = ?,\n"
    		+ "    @hire_date = ?,\n"
    		+ "    @team_name = ?,\n"
    		+ "    @description = ?,\n"
    		+ "    @report_date = ?;";
    final static String QUERY_TEMPLATE_6 = "EXEC EnterEmployeeExpense @employee_ssn = ?, @expense_date = ?, @expense_amount = ?, @description = ?;";
    final static String QUERY_TEMPLATE_7 = "EXEC EnterDonation\n"
    		+ "    @SSN = ?,\n"
    		+ "    @person_name = ?,\n"
    		+ "    @gender = ?,\n"
    		+ "    @profession = ?,\n"
    		+ "    @mailing_address = ?,\n"
    		+ "    @email = ?,\n"
    		+ "    @phone = ?,\n"
    		+ "    @on_mailing_list = ?,\n"
    		+ "    @is_anonymous = ?,\n"
    		+ "    @date = ?,\n"
    		+ "    @amount = ?,\n"
    		+ "    @type = ?,\n"
    		+ "    @fundraising_campaign = ?;";
    final static String QUERY_TEMPLATE_8 = "EXEC RetrieveDoctorForClient @client_ssn = ?;";
    final static String QUERY_TEMPLATE_9 = "EXEC RetrieveEmployeeExpenses @start_date = ?, @end_date = ?;";
    final static String QUERY_TEMPLATE_10 = "EXEC RetrieveVolunteersForClient @client_ssn = ?;";
    final static String QUERY_TEMPLATE_11 = "EXEC RetrieveTeamsByDate @date_formed = ?;";
    final static String QUERY_TEMPLATE_12 = "EXEC RetrievePersonAndEmergencyContacts;";
    final static String QUERY_TEMPLATE_13 = "EXEC RetrieveEmployeeDonors;";
    final static String QUERY_TEMPLATE_14 = "EXEC IncreaseSalaryForMultiTeamManagers;";
    final static String QUERY_TEMPLATE_15 = "EXEC DeleteClientsNoInsuranceLowTransport;";
    final static String QUERY_TEMPLATE_16 = "EXEC ;";
    final static String QUERY_TEMPLATE_17 = "EXEC ;";
    final static String QUERY_TEMPLATE_18 = "EXEC ;";

    // User input prompt
    final static String PROMPT = "\nPlease select one of the options below: \n" +
        "1) Enter a new team into the database; \n" +
        "2) Enter a new client into the database and associate him or her with one or more teams; \n" +
        "3) Enter a new volunteer into the database and associate him or her with one or more teams; \n" +
        "4) Enter the number of hours a volunteer worked this month for a particular team; \n" +
        "5) Enter a new employee into the database and associate him or her with one or more teams; \n" +
        "6) Enter an expense charged by an employee; \n" +
        "7) Enter a new donor and associate him or her with several donations; \n" +
        "8) Retrieve the name and phone number of the doctor of a particular client; \n" +
        "9) Retrieve the total amount of expenses charged by each employee for a particular period of time; \n" +
        "10) Retrieve the list of volunteers that are members of teams that support a particular client; \n" +
        "11) Retrieve the names of all teams that were founded after a particular date; \n" +
        "12) Retrieve the names, social security numbers, contact information, and emergency contact information of all people in the database; \n" +
        "13) Retrieve the name and total amount donated by donors that are also employees; \n" +
        "14) Increase the salary by 10% of all employees to whom more than one team must report; \n" +
        "15) Delete all clients who do not have health insurance and whose value of importance for transportation is less than 5; \n" +
        "16) Import: Enter new teams from a data file until the file is empty; \n" +
        "17) Export: Retrieve names and mailing addresses of all people on the mailing list and output them to a data file instead of screen; \n" +
        "18) Quit! \n";

    public static void main(String[] args) throws SQLException {
        System.out.println("WELCOME TO THE PATIENT ASSISTANT NETWORK DATABASE SYSTEM \n");
        final Scanner sc = new Scanner(System.in); // Scanner is used to collect user input
        String option = ""; // Initialize user option selection as nothing

        while (!option.equals("18")) { // Ask user for options until option 3 is selected
            System.out.println(PROMPT); // Print the available options
            option = sc.next(); // Read in the user option selection

            switch (option) { // Switch between different options
                case "1":
                    // Collect the new team data from the user
                    System.out.println("Please enter new team name: \n");
                    sc.nextLine();
                    final String name = sc.nextLine(); // Read in the user input of name
                    System.out.println("Please enter the type of team: \n");
                    final String type = sc.nextLine(); // Read in user input of date_formed
                    System.out.println("Please enter the date the team was formed [YYYY-MM-DD]: \n");
                    final String date_formed = sc.nextLine(); // Read in user input of date_formed
                    System.out.println("Please enter the team leader's name: \n");
                    final String team_leader = sc.nextLine(); // Read in user input of team_leader

                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_1)) {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, name);
                            statement.setString(2, type);
                            statement.setString(3, date_formed);
                            statement.setString(4, team_leader);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        }
                    }
                    break;
                    
                case "2":
                    // Collect the new client data from the user
                    System.out.println("Please enter a new person's ssn [###-##-####]: \n");
                    sc.nextLine();
                    final String client_SSN = sc.nextLine();
                    System.out.println("Please enter their name: \n");
                    final String client_person_name = sc.nextLine();
                    System.out.println("Please enter their gender [0=MALE, 1=FEMALE]: \n");
                    final int client_gender = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Please enter their profession: \n");
                    final String client_profession = sc.nextLine();
                    System.out.println("Please enter their mailing address: \n");
                    final String client_mailing_address = sc.nextLine();
                    System.out.println("Please enter their email: \n");
                    final String client_email = sc.nextLine();
                    System.out.println("Please enter their phone number [###-###-####]: \n");
                    final String client_phone = sc.nextLine();
                    System.out.println("Please enter if they are on the mailing list [0=NO, 1=YES]: \n");
                    final int client_mailing_list = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Please enter their doctor's name: \n");
                    final String doctor_name = sc.nextLine();
                    System.out.println("Please enter their doctor's phone number [###-###-####]: \n");
                    final String doctor_phone = sc.nextLine();
                    System.out.println("Please enter when they were assigned to the organization [YYYY-MM-DD]: \n");
                    final String first_assigned_date = sc.nextLine();
                    System.out.println("Please enter the name of the team they are cared for by: \n");
                    final String client_team_name = sc.nextLine();
                    System.out.println("Please enter if they are actively apart of the team [0=NO, 1=YES]: \n");
                    final int is_active = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_2)) {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, client_SSN);
                            statement.setString(2, client_person_name);
                            statement.setInt(3, client_gender);
                            statement.setString(4, client_profession);
                            statement.setString(5, client_mailing_address);
                            statement.setString(6, client_email);
                            statement.setString(7, client_phone);
                            statement.setInt(8, client_mailing_list);
                            statement.setString(9, doctor_name);
                            statement.setString(10, doctor_phone);
                            statement.setString(11, first_assigned_date);
                            statement.setString(12, client_team_name);
                            statement.setInt(13, is_active);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        }
                    }
                    break;
                    
                case "3":
                    // Collect the new volunteer data from the user
                	System.out.println("Please enter a new person's ssn [###-##-####]: \n");
                    sc.nextLine();
                    final String volunteer_SSN = sc.nextLine();
                    System.out.println("Please enter their name: \n");
                    final String volunteer_person_name = sc.nextLine();
                    System.out.println("Please enter their gender [0=MALE, 1=FEMALE]: \n");
                    final int volunteer_gender = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Please enter their profession: \n");
                    final String volunteer_profession = sc.nextLine();
                    System.out.println("Please enter their mailing address: \n");
                    final String volunteer_mailing_address = sc.nextLine();
                    System.out.println("Please enter their email: \n");
                    final String volunteer_email = sc.nextLine();
                    System.out.println("Please enter their phone number [###-###-####]: \n");
                    final String volunteer_phone = sc.nextLine();
                    System.out.println("Please enter if they are on the mailing list [0=NO, 1=YES]: \n");
                    final int volunteer_mailing_list = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Please enter the date they first joined the team [YYYY-MM-DD]: \n");
                    final String first_join_date = sc.nextLine();
                    System.out.println("Please enter their most recent traing date [YYYY-MM-DD]: \n");
                    final String most_recent_training_date = sc.nextLine();
                    System.out.println("Please enter their most recent traing location: \n");
                    final String most_recent_training_location = sc.nextLine();
                    System.out.println("Please enter the name of the team they are apart of: \n");
                    final String volunteer_team_name = sc.nextLine();
                    System.out.println("Please enter if they are actively apart of the team [0=NO, 1=YES]: \n");
                    final int volunteer_is_active = sc.nextInt();
                    sc.nextLine();
                    
                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_3)) {
                            // Populate the query template with the data collected from the user
                        	statement.setString(1, volunteer_SSN);
                            statement.setString(2, volunteer_person_name);
                            statement.setInt(3, volunteer_gender);
                            statement.setString(4, volunteer_profession);
                            statement.setString(5, volunteer_mailing_address);
                            statement.setString(6, volunteer_email);
                            statement.setString(7, volunteer_phone);
                            statement.setInt(8, volunteer_mailing_list);
                            statement.setString(9, first_join_date);
                            statement.setString(10, most_recent_training_date);
                            statement.setString(11, most_recent_training_location);
                            statement.setString(12, volunteer_team_name);
                            statement.setInt(13, volunteer_is_active);
                            
                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        }
                    }
                    break;
                    
                case "4":
                    // Collect the new hours_worked data from the user
                    System.out.println("Please enter the ssn of a volunteer: \n");
                    sc.nextLine();
                    final String volunteer_ssn = sc.nextLine();
                    System.out.println("Please enter their team's name: \n");
                    final String team_name = sc.nextLine();
                    System.out.println("Please enter their hours worked: \n");
                    final int hours_worked = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Connecting to the database...");

                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_4)) {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, volunteer_ssn);
                            statement.setString(2, team_name);
                            statement.setInt(3, hours_worked);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        }
                    }
                    break;
                    
                case "5":
                    // Collect the new employee data from the user
                    System.out.println("Please enter new employee ssn: \n");
                    sc.nextLine();
                    final String employee_SSN = sc.nextLine();
                    System.out.println("Please enter their name: \n");
                    final String employee_person_name = sc.nextLine();
                    System.out.println("Please enter their gender: \n");
                    final int employee_gender = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Please enter their profession: \n");
                    final String employee_profession = sc.nextLine();
                    System.out.println("Please enter their mailing address: \n");
                    final String employee_mailing_address = sc.nextLine();
                    System.out.println("Please enter their email: \n");
                    final String employee_email = sc.nextLine();
                    System.out.println("Please enter their phone: \n");
                    final String employee_phone = sc.nextLine();
                    System.out.println("Please enter if they are on the mailing list [0=NO, 1=YES]: \n");
                    final int employee_on_mailing_list = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Please enter their salary: \n");
                    final Double salary = sc.nextDouble();
                    sc.nextLine();
                    System.out.println("Please enter their marital status: \n");
                    final String marital_status = sc.nextLine();
                    System.out.println("Please enter their hire date [YYYY-MM-DD]: \n");
                    final String hire_date = sc.nextLine();
                    System.out.println("Please enter the name of their team: \n");
                    final String employee_team_name = sc.nextLine();
                    System.out.println("Please enter the report description: \n");
                    final String description = sc.nextLine();
                    System.out.println("Please enter the report date [YYYY-MM-DD]: \n");
                    final String report_date = sc.nextLine();

                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_5)) {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, employee_SSN);
                            statement.setString(2, employee_person_name);
                            statement.setInt(3, employee_gender);
                            statement.setString(4, employee_profession);
                            statement.setString(5, employee_mailing_address);
                            statement.setString(6, employee_email);
                            statement.setString(7, employee_phone);
                            statement.setInt(8, employee_on_mailing_list);
                            statement.setDouble(9, salary);
                            statement.setString(10, marital_status);
                            statement.setString(11, hire_date);
                            statement.setString(12, employee_team_name);
                            statement.setString(13, description);
                            statement.setString(14, report_date);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        }
                    }
                    break;
                    
                case "6":
                    // Collect the new expense data from the user
                    System.out.println("Please enter the expensing employee's ssn: \n");
                    sc.nextLine();
                    final String employee_ssn = sc.nextLine();
                    System.out.println("Please enter the expense date [YYYY-MM-DD]: \n");
                    final String expense_date = sc.nextLine();
                    System.out.println("Please enter the expense amount: \n");
                    final Double expense_amount = sc.nextDouble();
                    sc.nextLine();
                    System.out.println("Please enter the expense description: \n");
                    final String expense_description = sc.nextLine();

                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_6)) {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, employee_ssn);
                            statement.setString(2, expense_date);
                            statement.setDouble(3, expense_amount);
                            statement.setString(4, expense_description);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        }
                    }
                    break;
                    
                case "7":
                    // Collect the new donor data from the user
                	System.out.println("Please enter new donor ssn: \n");
                    sc.nextLine();
                    final String donor_SSN = sc.nextLine();
                    System.out.println("Please enter their name: \n");
                    final String donor_person_name = sc.nextLine();
                    System.out.println("Please enter their gender: \n");
                    final int donor_gender = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Please enter their profession: \n");
                    final String donor_profession = sc.nextLine();
                    System.out.println("Please enter their mailing address: \n");
                    final String donor_mailing_address = sc.nextLine();
                    System.out.println("Please enter their email: \n");
                    final String donor_email = sc.nextLine();
                    System.out.println("Please enter their phone: \n");
                    final String donor_phone = sc.nextLine();
                    System.out.println("Please enter if they are on the mailing list [0=NO, 1=YES]: \n");
                    final int donor_on_mailing_list = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Please enter if they wish to be anonymous [0=NO, 1=YES]: \n");
                    final int is_anonymous = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Please enter the date of the donation: \n");
                    final String date = sc.nextLine();
                    System.out.println("Please enter the amount: \n");
                    final Double amount = sc.nextDouble();
                    sc.nextLine();
                    System.out.println("Please enter the payment type (Check or Card): \n");
                    final String donor_type = sc.nextLine();
                    System.out.println("Please enter the fundraising campaign: \n");
                    final String fundraising_campaign = sc.nextLine();

                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_7)) {
                            // Populate the query template with the data collected from the user
                        	statement.setString(1, donor_SSN);
                            statement.setString(2, donor_person_name);
                            statement.setInt(3, donor_gender);
                            statement.setString(4, donor_profession);
                            statement.setString(5, donor_mailing_address);
                            statement.setString(6, donor_email);
                            statement.setString(7, donor_phone);
                            statement.setInt(8, donor_on_mailing_list);
                            statement.setInt(9, is_anonymous);
                            statement.setString(10, date);
                            statement.setDouble(11, amount);
                            statement.setString(12, donor_type);
                            statement.setString(13, fundraising_campaign);

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        }
                    }
                    break;
                    
                case "8":
                    // Collect the new doctor data from the user
                    System.out.println("Please enter a client's ssn: \n");
                    sc.nextLine();
                    final String client_ssn = sc.nextLine();

                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_8)) {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, client_ssn);

                            System.out.println("Dispatching the query...");
                            // Execute the query and handle the ResultSet
                            ResultSet resultSet = statement.executeQuery();

                            if (resultSet.next()) {
                                String doctorName = resultSet.getString("doctor_name");
                                String doctorPhone = resultSet.getString("doctor_phone");
                                System.out.println("Doctor Name: " + doctorName);
                                System.out.println("Doctor Phone: " + doctorPhone);
                            } else {
                                System.out.println("No doctor found for the specified client.");
                            }
                            
                            resultSet.close();
                        }
                    }
                    break;
                    
                case "9":
                    // Collect total expenses data from the user
                    System.out.println("Please enter a start date [YYYY-MM-DD]: \n");
                    sc.nextLine();
                    final String start_date = sc.nextLine();
                    System.out.println("Please enter an end date [YYYY-MM-DD]: \n");
                    final String end_date = sc.nextLine();

                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_9)) {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, start_date);
                            statement.setString(2, end_date);

                            System.out.println("Dispatching the query...");
                            // Execute the query and handle the ResultSet
                            ResultSet resultSet = statement.executeQuery();
                            
                            // Process and display the results
                            System.out.println("Employee SSN | Total Expenses");
                            System.out.println("----------------------------");
                            while (resultSet.next()) {
                                String employeeSSN = resultSet.getString("employee_ssn");
                                double totalExpenses = resultSet.getDouble("total_expenses");
                                System.out.printf("%s | %.2f%n", employeeSSN, totalExpenses);
                            }

                            resultSet.close();
                            
                        }
                    }
                    break;
                    
                case "10":
                    // Collect a list of volunteers that are members of teams that support a particular client from the user
                    System.out.println("Please enter a client's ssn: \n");
                    sc.nextLine();
                    final String volunteer_client_ssn = sc.nextLine();

                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_10)) {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, volunteer_client_ssn);

                            System.out.println("Dispatching the query...");
                            // Execute the query and handle the ResultSet
                            ResultSet resultSet = statement.executeQuery();
                            
                            // Process and display the results
                            System.out.println("Volunteer Name | Volunteer Phone");
                            System.out.println("-------------------------------");
                            while (resultSet.next()) {
                                String volunteerName = resultSet.getString("volunteer_name");
                                String volunteerPhone = resultSet.getString("volunteer_phone");
                                System.out.printf("%s | %s%n", volunteerName, volunteerPhone);
                            }

                            resultSet.close();
                        }
                    }
                    break;
                    
                case "11":
                    // Collect the names of all teams that were founded after a particular date from the user
                    System.out.println("Please enter a date [YYYY-MM-DD]: \n");
                    sc.nextLine();
                    final String team_date_formed = sc.nextLine();

                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_11)) {
                            // Populate the query template with the data collected from the user
                            statement.setString(1, team_date_formed);

                            System.out.println("Dispatching the query...");
                            // Execute the query and handle the ResultSet
                            ResultSet resultSet = statement.executeQuery();
                            
                            // Process and display the results
                            System.out.println("Team Name");
                            System.out.println("---------");
                            while (resultSet.next()) {
                                String teamName = resultSet.getString("team_name");
                                System.out.println(teamName);
                            }

                            resultSet.close();
                        }
                    }
                    break;
                    
                case "12":
                    System.out.println("Connecting to the database...");
                    
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_12)) {

                            System.out.println("Dispatching the query...");
                            /// Execute the query and handle the ResultSet
                            ResultSet resultSet = statement.executeQuery();
                            
                            // Process and display the results
                            System.out.println("Name | SSN | Mailing Address | Email | Phone | Emergency Contact | Emergency Phone | Relationship");
                            System.out.println("--------------------------------------------------------------------------------------------------");
                            while (resultSet.next()) {
                                String personName = resultSet.getString("person_name");
                                String ssn = resultSet.getString("SSN");
                                String mailingAddress = resultSet.getString("mailing_address");
                                String email = resultSet.getString("email");
                                String personPhone = resultSet.getString("person_phone");
                                String emergencyContactName = resultSet.getString("emergency_contact_name");
                                String emergencyContactPhone = resultSet.getString("emergency_contact_phone");
                                String relationship = resultSet.getString("relationship");
                                
                                System.out.printf("%s | %s | %s | %s | %s | %s | %s | %s%n", 
                                                  personName, ssn, mailingAddress, email, personPhone, 
                                                  emergencyContactName, emergencyContactPhone, relationship);
                            }

                            resultSet.close();
                        }
                    }
                    break;
                    
                case "13":
                    System.out.println("Connecting to the database...");
                    
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_13)) {

                            System.out.println("Dispatching the query...");
                            // Execute the query and handle the ResultSet
                            ResultSet resultSet = statement.executeQuery();
                            
                            // Process and display the results
                            System.out.println("Donor Name | Total Donated | Anonymous");
                            System.out.println("----------------------------------------");
                            while (resultSet.next()) {
                                String donorName = resultSet.getString("donor_name");
                                double totalDonated = resultSet.getDouble("total_donated");
                                boolean isAnonymous = resultSet.getBoolean("is_anonymous");

                                System.out.printf("%s | %.2f | %s%n", 
                                                  donorName, totalDonated, 
                                                  isAnonymous ? "Yes" : "No");
                            }

                            resultSet.close();
                        }
                    }
                    break;
                    
                case "14":
                    System.out.println("Connecting to the database...");
                    
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_14)) {
                            
                        	System.out.println("Dispatching the query...");
                            /// Execute the query as an update, since it modifies data
                            int rowsUpdated = statement.executeUpdate();

                            System.out.println("Done. Salaries updated for " + rowsUpdated + " employees.");
                        }
                    } catch (SQLException e) {
                        System.out.println("An error occurred while updating salaries: " + e.getMessage());
                    }
                    break;
                    
                case "15":
                    System.out.println("Connecting to the database...");
                    
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_15)) {
                            
                        	System.out.println("Dispatching the query...");
                        	// Execute the query as an update, since it modifies data
                            int rowsDeleted = statement.executeUpdate();

                            System.out.println("Done. " + rowsDeleted + " clients deleted.");
                        }
                    } catch (SQLException e) {
                        System.out.println("An error occurred while deleting clients: " + e.getMessage());
                    }
                    break;
                    
                case "16":
                    System.out.println("Please enter the input file name: ");
                    String inputFileName = sc.nextLine();

                    System.out.println("Connecting to the database...");
                    try (Connection connection = DriverManager.getConnection(URL);
                         PreparedStatement statement = connection.prepareStatement("{call EnterNewTeam(?, ?, ?, ?)}")) {
                        
                        BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
                        String line;
                        
                        while ((line = reader.readLine()) != null) {
                            String[] teamData = line.split(","); // Assuming CSV format

                            if (teamData.length == 4) {
                                statement.setString(1, teamData[0].trim()); // Team name
                                statement.setString(2, teamData[1].trim()); // Team type
                                statement.setDate(3, Date.valueOf(teamData[2].trim())); // Date formed (YYYY-MM-DD format)
                                statement.setString(4, teamData[3].trim()); // Team leader

                                statement.executeUpdate();
                                System.out.println("Inserted team: " + teamData[0]);
                            } else {
                                System.out.println("Skipping invalid line: " + line);
                            }
                        }
                        
                        reader.close();
                        System.out.println("All teams imported successfully.");
                    } catch (SQLException | IOException e) {
                        System.out.println("An error occurred while importing teams: " + e.getMessage());
                    }
                    break;
                    
                case "17":
                    System.out.println("Please enter the output file name: ");
                    String outputFileName = sc.nextLine();

                    System.out.println("Connecting to the database...");
                    try (Connection connection = DriverManager.getConnection(URL);
                         PreparedStatement statement = connection.prepareStatement("{call RetrieveMailingList}");
                         BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                        
                        ResultSet resultSet = statement.executeQuery();
                        
                        // Writing header
                        writer.write("Name, Mailing Address\n");

                        while (resultSet.next()) {
                            String name1 = resultSet.getString("person_name");
                            String mailingAddress = resultSet.getString("mailing_address");
                            writer.write(name1 + ", " + mailingAddress + "\n");
                        }

                        resultSet.close();
                        System.out.println("Mailing list exported successfully to " + outputFileName);
                    } catch (SQLException | IOException e) {
                        System.out.println("An error occurred while exporting the mailing list: " + e.getMessage());
                    }
                    break;
                    
                case "18":
                    System.out.println("Exiting the program...");
                    System.exit(0);
                    break;
                    
            }
        }
        sc.close(); // Close the scanner before exiting the application
    }
}
       