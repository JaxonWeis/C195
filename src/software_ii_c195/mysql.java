/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_ii_c195;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * this class is used to interact with the database
 * @author Admin
 */
public class mysql {
    
    /**
     * The object used to interact with the database
     */
    public static mysql database;
    
    //Editable Database data
    private final String DBUser = "U05XJp";
    private final String DBPassword = "53688633129";
    private final String DBUrl = "wgudb.ucertify.com";
    private final String DBPort = "3306";
    private final String DBName = "WJ05XJp";
    private final String DBType = "mysql";
    
    //Commection object class uses to connect to database
    private final Connection conn;                                                    
    private int verifiedUserID;
    private String verifiedUserLogin;
    
    //Contructor sets up connections and connects

    /**
     * Constructor used to open the connection to the database
     * @throws SQLException
     */
    public mysql() throws SQLException{
        System.out.print("Setting up Connection... ");
        String url = "jdbc:" + DBType + "://" + DBUrl +  ":" + DBPort + "/" + DBName + "?autoReconnect=true";
        this.conn = DriverManager.getConnection(url, DBUser, DBPassword);
        System.out.print("Done!\n");
    }

    /**
     * VerifyUser checks database for user and saves UserID and UserLogin
     * used by login menu to check user password match
     * <p>Used by Login_MenuContoller.Login_Action to check user and password from the login form
     * @param User user to send to database to retrive the password
     * @param Pass checks the password and compare with retrived password
     * @return true if login passes and false if login fails
     * @throws java.sql.SQLException throws if connection to Database fails
     */
    public boolean verifyUser(String User, String Pass) throws SQLException {
        System.out.print("\tChecking Credintials... ");
        
        //Setting up query and getting result
        String sql = "SELECT * FROM users WHERE User_Name=?;";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        ps.setString(1, User);
        ResultSet rs = ps.executeQuery();
        
        //If no result fail login
        if(!rs.next()) return false;
        
        //If User and Password match attach user name and id to object pass login
        if(Pass.matches(rs.getString("Password"))) {
            System.out.print("Done!\n");
            this.verifiedUserID = rs.getInt("User_ID");
            this.verifiedUserLogin = rs.getString("User_Name");
            System.out.println("\tUserID: " + this.verifiedUserID 
                            + " User Login: " + this.verifiedUserLogin 
                            + "... Logged in!");
            return true;
        }
        
        return false;   
    }
    
    /**
     * Populates Country List from DB runs once
     * runs after sucessfull login to fill in the countries list 
     * @throws java.sql.SQLException if connection to database fails
     */
    public void updateCountriesList() throws SQLException {
        //Sending SQL Query and getting result set
        System.out.print("\tSending SQL Query...");
        String sql = "SELECT * FROM countries;";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        //Make sure country list is empty        
        System.out.print("Done!\n\tClearing Country List...");
        Countries.countryList.clear();
        
        //each line in the result set is parsed and added as a new object in the list
        System.out.print("Done.\n\tAdding Results to Country List...\n");
        while(rs.next()) {
            Countries.countryList.add(new Countries(rs.getInt("Country_ID"), rs.getString("Country")));
        }
        System.out.println("\t\tCountry List Updated to size: " + Countries.countryList.size() + "\n\tDone!");
    }
    
    /**
     * Populates Division List from DB runs once
     * runs after the updatecountryList to match countries with divisions
     * @throws java.sql.SQLException if connection to database fails
     */
    public void updateDivisionsList() throws SQLException {
        //Sending SQL Query and getting result set
        System.out.print("\tSending SQL Query...");
        String sql = "SELECT * FROM first_level_divisions;";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        //makeing sure division list is clear
        System.out.print("Done!\n\tClearing Division List...");
        Divisions.divisionList.clear();
        
        //each line in the result set is parsed and added as a new object in the list
        System.out.print("Done.\n\tAdding Results to Division List...\n");
        while(rs.next()) {
            Divisions.divisionList.add(new Divisions(rs.getInt("Division_ID"), rs.getString("Division"), Countries.findCountryById(rs.getInt("Country_ID"))));
        }
        System.out.println("\t\tDivisions List Updated to size: " + Divisions.divisionList.size() + "\n\tDone!");
    }
    
    /**
     * Populates Contact List from DB runs once
     * runs shortly after login to populate the contacts list
     * @throws java.sql.SQLException if connection to database fails
     */
    public void updateContactsList() throws SQLException {
        //Setting up SQL query and getting result set
        System.out.print("\tSending SQL Query...");
        String sql = "SELECT * FROM contacts;";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        //Making sure contact list is clear
        System.out.print("Done!\n\tClearing Contacts List...");
        Contacts.contactList.clear();
        
        //each line in the result set is parsed and added as a new object in the list
        System.out.print("Done.\n\tAdding Results to Contacts List...\n");
        while(rs.next()) {
            Contacts.contactList.add(new Contacts(rs.getInt("Contact_ID"), rs.getString("Contact_Name"), rs.getString("Email")));
        }
        System.out.println("\t\tContacts List Updated to size: " + Contacts.contactList.size() + "\n\tDone!");
    }
    
    /**
     * Populates Customer List from DB runs every time customer table modification
     * runs after division list is updated to match the division with the customer
     * also runs whenever a customer is added/updated/or deleted to fill in the customer table
     * @throws java.sql.SQLException if connection to database fails
     */
    public void updateCustomerList() throws SQLException {
        //Setting up SQL query and getting result set
        System.out.print("\tSending SQL Query...");
        String sql = "SELECT * FROM customers;";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        //Clearing Customer List this could be full
        System.out.print("Done!\n\tClearing Customer List...");
        Customers.customerList.clear();
        
        //each line in the result set is parsed and added as a new object in the list
        System.out.print("Done.\n\tAdding Results to Customer List...\n");
        while(rs.next()) {
            Customers.customerList.add(new Customers(rs.getInt("Customer_ID"), rs.getString("Customer_Name"), rs.getString("Address"), rs.getString("Postal_Code"), rs.getString("Phone"), Divisions.findDivisionByID(rs.getInt("Division_ID"))));
        }
        System.out.println("\t\tCustomer List Updated to size: " + Customers.customerList.size() + "\n\tDone!");
    }
    
    /**
     * Adds a new customer to the DB and refreshes the list
     * used by the customer menu to add the customer created by the menu into the database
     * @param cus adds the customer to the database
     * @throws java.sql.SQLException if connection to database fails
     */
    public void addCustomer(Customers cus) throws SQLException {
        //Setting up SQL query with variables added in
        System.out.print("Making Sql Statement... ");
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        System.out.print("Done!\nFilling in variables... ");
        ps.setString(1, cus.getName());
        ps.setString(2, cus.getAddress());
        ps.setString(3, cus.getPostalCode());
        ps.setString(4, cus.getPhone());
        ps.setString(5, this.verifiedUserLogin);
        ps.setString(6, this.verifiedUserLogin);
        ps.setInt(7, cus.getDivision().getID());
        //Execute Query
        System.out.print("Done!\nExecute Query... ");
        ps.executeUpdate();
        System.out.print("Done!\nUpdate Cutomers...\n");
        //Update Customer List
        updateCustomerList();
    }
    
    /**
     * Updates an exsisting customer in the DB and refreshes the list
     * used by the customer menu to update an exsisting customer keeping the same ID number
     * @param cus updates the passed customer
     * @throws java.sql.SQLException if connection to database fails
     */
    public void updateCustomer(Customers cus) throws SQLException {
        //Setting up SQL query with variables added in        
        System.out.print("Making Sql Statement... ");
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = NOW(), Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?;";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        System.out.print("Done!\nFilling in variables... ");
        ps.setString(1, cus.getName());
        ps.setString(2, cus.getAddress());
        ps.setString(3, cus.getPostalCode());
        ps.setString(4, cus.getPhone());
        ps.setString(5, this.verifiedUserLogin);
        ps.setInt(6, cus.getDivision().getID());
        ps.setInt(7, cus.getID());
        //Execute Query
        System.out.print("Done!\nExecute Query... ");
        ps.executeUpdate();
        System.out.print("Done!\nUpdate Cutomers...\n");
        //Update Customer List
        updateCustomerList();
    }
    
    /**
     * Deletes an exsisting customer in the DB and refreshes the list
     * used by the confirmation thread to delete the customer after confirmation
     * @param cus removes passed customer from the database
     * @throws java.sql.SQLException if connection to database fails
     */
    public void deleteCustomer(Customers cus) throws SQLException {
        //First delete any appointments the customer is in
        deleteAppointmentByCustomer(cus);
        //Setting up SQL query with variables added in        
        System.out.print("Making Sql Statement... ");
        String sql = "DELETE FROM customers WHERE Customer_ID = ?;";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        System.out.print("Done!\nFilling in variables... ");
        ps.setInt(1, cus.getID());
        //Execute Query
        System.out.print("Done!\nExecute Query... ");
        ps.executeUpdate();
        System.out.print("Done!\nUpdate Cutomers...\n");
        //Update Customer List
        updateCustomerList();
    }
    
    /**
     * Updates the appointments list
     * used by the appointment menu to update an appointment to keep the ID the same
     * @throws java.sql.SQLException if connection to database fails
     */
    public void updateAppointmentList() throws SQLException {
        //Setting up SQL query        
        System.out.print("\tSending SQL Query...");
        String sql = "SELECT * FROM appointments;";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        //Execute Query and read result set
        ResultSet rs = ps.executeQuery();
        
        //Making sure Appointment list is clear
        System.out.print("Done!\n\tClearing Appointments List...");
        Appointments.appointmentList.clear();
        
        //Adding each line of the result set to appointment list
        System.out.print("Done.\n\tAdding Results to Appointments List...\n");
        while(rs.next()) {
            Appointments.appointmentList.add(new Appointments(rs.getInt("Appointment_ID"), rs.getString("Title"), rs.getString("Description"), rs.getString("Location"), Contacts.findContactByID(rs.getInt("Contact_ID")), rs.getString("Type"), rs.getString("Start"), rs.getString("End"), Customers.getCustomerByID(rs.getInt("Customer_ID"))));
        }
        System.out.println("\t\tAppointment List Updated to size: " + Customers.customerList.size() + "\n\tDone!");
    }
    
    /**
     * update appointment list within a specific time frame
     * used by the main menu to refresh the table with a new time frame
     * @param timeStart
     * @param timeEnd
     * @throws java.sql.SQLException if connection to database fails
     */
    public void updateAppointmentList(String timeStart, String timeEnd) throws SQLException {
        //Setting up SQL query with variables added in        
        System.out.print("\tSending SQL Query...");
        String sql = "SELECT * FROM appointments WHERE (End BETWEEN ? AND ?);";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        ps.setString(1, timeStart);
        ps.setString(2, timeEnd);
        //Execute Query and read result set
        ResultSet rs = ps.executeQuery();

        //Clearing out appointment list
        System.out.print("Done!\n\tClearing Appointments List...");        
        Appointments.appointmentList.clear();
        
        //Adding each line of result set into appointment list
        System.out.print("Done.\n\tAdding Results to Appointments List...\n");
        while(rs.next()) {
            Appointments.appointmentList.add(new Appointments(rs.getInt("Appointment_ID"), rs.getString("Title"), rs.getString("Description"), rs.getString("Location"), Contacts.findContactByID(rs.getInt("Contact_ID")), rs.getString("Type"), rs.getString("Start"), rs.getString("End"), Customers.getCustomerByID(rs.getInt("Customer_ID"))));
        }
        System.out.println("\t\tAppointment List Updated to size: " + Appointments.appointmentList.size() + "\n\tDone!");
    }
    
    /**
     * update appointment list within a specific time frame
     * used by the main menu controller to see if an appointment is within 15 minutes of login time
     * @param timeStart the time appointments have to be after
     * @param timeEnd the time appointments have to be before
     * @throws java.sql.SQLException if connection to database fails
     */
    public void updateAppointmentListByStart(String timeStart, String timeEnd) throws SQLException {
        //Setting up SQL query with variables added in        
        System.out.print("\tSending SQL Query...");
        System.out.print(timeStart + " ");
        String sql = "SELECT * FROM appointments WHERE (Start BETWEEN ? AND ?);";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        ps.setString(1, timeStart);
        ps.setString(2, timeEnd);
        //Execute Query and read result set
        ResultSet rs = ps.executeQuery();

        //Clearing out appointment list
        System.out.print("Done!\n\tClearing Appointments List...");        
        Appointments.appointmentList.clear();
        
        //Adding each line of result set into appointment list
        System.out.print("Done.\n\tAdding Results to Appointments List...\n");
        while(rs.next()) {
            Appointments.appointmentList.add(new Appointments(rs.getInt("Appointment_ID"), rs.getString("Title"), rs.getString("Description"), rs.getString("Location"), Contacts.findContactByID(rs.getInt("Contact_ID")), rs.getString("Type"), rs.getString("Start"), rs.getString("End"), Customers.getCustomerByID(rs.getInt("Customer_ID"))));
        }
        System.out.println("\t\tAppointment List Updated to size: " + Appointments.appointmentList.size() + "\n\tDone!");
    }
    
    /**
     * ability to check if any appoitments in time
     * used by the appointment menu to pull all appointments the same day as a new one to check for overlap
     * @param timeStart the time appointments have to be after
     * @param timeEnd the time appointments have to be before
     * @return the number of appointments in the range
     * @throws java.sql.SQLException if connection to database fails
     */
    public ObservableList<Appointments> getAppointmentList(String timeStart, String timeEnd) throws SQLException {
        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();        
        //Setting up SQL query with variables added in        
        //System.out.print("\tSending SQL Query...");
        //System.out.print(timeStart + " ");
        String sql = "SELECT * FROM appointments WHERE (Start BETWEEN ? AND ?);";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        ps.setString(1, timeStart);
        ps.setString(2, timeEnd);
        //Execute Query and read result set
        ResultSet rs = ps.executeQuery();

        //Adding each line of result set into appointment list
        //System.out.print("Done.\n\tAdding Results to Appointments List...\n");
        while(rs.next()) {
            appointmentList.add(new Appointments(rs.getInt("Appointment_ID"), rs.getString("Title"), rs.getString("Description"), rs.getString("Location"), Contacts.findContactByID(rs.getInt("Contact_ID")), rs.getString("Type"), rs.getString("Start"), rs.getString("End"), Customers.getCustomerByID(rs.getInt("Customer_ID"))));
        }
        System.out.println("Appointment List Updated to size: " + appointmentList.size() + "\n\tDone!");
        
        return appointmentList;
    }
    
    /**
     * Adds an appointment to the DB and refreshes the list at the same timeframe
     * used by the appointment menu to add an appointment after its been checked and refreshes the table
     * @param app adds the appointment into database
     * @throws java.sql.SQLException if connection to database fails 
     */
    public void addAppointment(Appointments app) throws SQLException {        
        //Setting up SQL query with variables added in        
        System.out.print("Making Sql Statement... ");
        String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?, ?, ?);";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        System.out.print("Done!\nFilling in variables... ");
        ps.setInt(1, app.getApointmentID());
        ps.setString(2, app.getTitle());
        ps.setString(3, app.getDescription());
        ps.setString(4, app.getLocation());
        ps.setString(5, app.getType());
        ps.setString(6, app.getStartTimeUTC());
        ps.setString(7, app.getEndTimeUTC());
        ps.setString(8, this.verifiedUserLogin);
        ps.setString(9, this.verifiedUserLogin);
        ps.setInt(10, app.getCustomer().getID());
        ps.setInt(11, this.verifiedUserID);
        ps.setInt(12, app.getContact().getID());
        
        //Execute Query
        System.out.print("Done!\nExecute Query... ");
        ps.executeUpdate();
        System.out.print("Done!\nUpdate Appointments...\n");
        
        //Get the begin and end times from Main Menu and refresh the appointment list
        String begin = Main_MenuController.startView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = Main_MenuController.endView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ISO_LOCAL_DATE);
        updateAppointmentList(begin, end);
    }
    
    /**
     * Deletes an Appointment in the DB and refreshes the list at the specific time frame
     * used by the main menu appointment deletion thread after the deletion was confirmed
     * @param app deletes the passed appointment
     * @throws java.sql.SQLException if connection to database fails
     */
    public void deleteAppointment(Appointments app) throws SQLException {
        //Setting up SQL query with variables added in        
        System.out.print("Making Sql Statement... ");
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?;";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        System.out.print("Done!\nFilling in variables... ");
        ps.setInt(1, app.getApointmentID());
        
        //Execute Query
        System.out.print("Done!\nExecute Query... ");
        ps.executeUpdate();
        System.out.print("Done!\nUpdate Appointments...\n");
        
        //Get the begin and end times from Main Menu and refresh the appointment list
        String begin = Main_MenuController.startView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = Main_MenuController.endView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ISO_LOCAL_DATE);
        updateAppointmentList(begin, end);
    }
    
    /**
     * Deletes all appointments from the DB by the customer ID Number
     * used by the customer side of the main menu to delete any appointments a customer has before
     * customer deletion
     * @param cus deletes all appointments for the passed customer
     * @throws java.sql.SQLException if connection to database fails
     */
    public void deleteAppointmentByCustomer(Customers cus) throws SQLException {
        //Setting up SQL query with variables added in        
        System.out.print("Making Sql Statement... ");
        String sql = "DELETE FROM appointments WHERE Customer_ID = ?;";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        System.out.print("Done!\nFilling in variables... ");
        ps.setInt(1, cus.getID());

        //Execute Query
        System.out.print("Done!\nExecute Query... ");
        ps.executeUpdate();
        System.out.print("Done!\nUpdate Appointments...\n");
        
        //Get the begin and end times from Main Menu and refresh the appointment list
        String begin = Main_MenuController.startView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = Main_MenuController.endView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ISO_LOCAL_DATE);
        updateAppointmentList(begin, end);
    }
    
    /**
     * Updates an appointment in the DB and refreshes the list
     * used by the appointment menu after a appointment update is submitted to update it in the database
     * @param app updates the database with the appointment passed
     * @throws java.sql.SQLException if connection to database fails
     */
    public void updateAppointment(Appointments app) throws SQLException {
        //Setting up SQL query with variables added in        
        System.out.print("Making Sql Statement... ");
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = NOW(), Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?;";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        System.out.print("Done!\nFilling in variables... ");
        ps.setString(1, app.getTitle());
        ps.setString(2, app.getDescription());
        ps.setString(3, app.getLocation());
        ps.setString(4, app.getType());
        ps.setString(5, app.getStartTimeUTC());
        ps.setString(6, app.getEndTimeUTC());
        ps.setString(7, this.verifiedUserLogin);
        ps.setInt(8, app.getCustomer().getID());
        ps.setInt(9, this.verifiedUserID);
        ps.setInt(10, app.getContact().getID());
        ps.setInt(11, app.getApointmentID());

        //Execute Query
        System.out.print("Done!\nExecute Query... ");
        ps.executeUpdate();
        System.out.print("Done!\nUpdate Appointments...\n");
        
        //Get the begin and end times from Main Menu and refresh the appointment list
        String begin = Main_MenuController.startView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = Main_MenuController.endView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ISO_LOCAL_DATE);
        updateAppointmentList(begin, end);
    }
    
    /**
     * Report1 runs the first report gets the result from the Database
     * used by reports page to fill in the text area
     * @return the report from the database
     * @throws SQLException if database doesnt connect 
     */
    public String report1() throws SQLException{
        //Setting up SQL query with variables added in        
        System.out.print("Making Sql Statement... ");
        String sql = "SELECT COUNT(Appointment_ID) AS \"Count\", `Type` , MONTH(`Start`) AS \"Month\", YEAR(`Start`) AS \"Year\" FROM appointments a GROUP BY Year, Month, `Type`;";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        String report = "";
        while(rs.next()){
            report += "Count: " + rs.getString(1) + ",\t\tType: " + rs.getString(2) + ",\t\tMonth: " + rs.getString(3) + ",\t\tYear: " + rs.getString(4) + "\n";
        }
        
        return report;
    }
    
    /**
     * Report2 runs the second report gets the result from the Database
     * used by the reports page to fill in the text area
     * @return the report from the database
     * @throws SQLException if the database doesnt connect
     */
    public String report2() throws SQLException{
        //Setting up SQL query with variables added in        
        System.out.print("Making Sql Statement... ");
        String sql = "SELECT Contact_Name, Appointment_ID, Title, `Type`, Description, `Start`, `End`, Customer_ID \n" +
                    "FROM appointments\n" +
                    "INNER JOIN contacts USING (Contact_ID)\n" +
                    "ORDER BY Contact_Name, Start";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        String report = "";
        while(rs.next()){
            report += "Contact Name: " + rs.getString(1) + ",\t\tAppointment ID: " + rs.getString(2) + ",\t\tTitle: " + rs.getString(3) + ",\t\tType: " + rs.getString(4) + ",\t\tDescription: " + rs.getString(5) + ",\t\tStart: " + rs.getString(6) + ",\t\tEnd: " + rs.getString(7) + ",\t\tCustomer ID: " + rs.getString(8) + "\n";
        }
        
        if(report.isEmpty()) report = "No appointments today.";
        
        return report;
    }
    
    /**
     * Report3 runs the third report gets the result from the Database
     * used by the reports page to fill in the text area
     * @return the report from the database
     * @throws SQLException if the database doesnt connect
     */
    public String report3() throws SQLException{
        //Setting up SQL query with variables added in        
        System.out.print("Making Sql Statement... ");
        String sql = "SELECT Customer_Name, Appointment_ID, Title, `Type`, Description, `Start`, `End`\n" +
                        "FROM appointments\n" +
                        "INNER JOIN customers USING (Customer_ID)\n" +
                        "WHERE Start BETWEEN ? AND ?\n" +
                        "ORDER BY Customer_Name, Start";
        PreparedStatement ps = this.conn.prepareStatement(sql);
        String year = ZonedDateTime.now(ZoneId.systemDefault()).withDayOfYear(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String nextYear = ZonedDateTime.now(ZoneId.systemDefault()).withDayOfYear(1).plusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ps.setString(1, year);
        ps.setString(2, nextYear);
        
        ResultSet rs = ps.executeQuery();
        
        String report = "";
        while(rs.next()){
            report += "Customer Name: " + rs.getString(1) + ",\t\tAppointment ID: " + rs.getString(2) + ",\t\tTitle: " + rs.getString(3) + ",\t\tType: " + rs.getString(4) + ",\t\tDescription: " + rs.getString(5) + ",\t\tStart: " + rs.getString(6) + ",\t\tEnd: " + rs.getString(7) + "\n";
        }
        
        if(report.isEmpty()) report = "No appointments this year";
        
        return report;
    }
}
