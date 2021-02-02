/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_ii_c195;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

/**
 *
 * @author Admin
 */

public class mysql {
    
    public static mysql database;                                               //database object all pages use to interact with the database
    
    //Editable Database data
    private final String DBUser = "U05XJp";
    private final String DBPassword = "53688633129";
    private final String DBUrl = "wgudb.ucertify.com";
    private final String DBPort = "3306";
    private final String DBName = "WJ05XJp";
    private final String DBType = "mysql";
    
    private final Connection conn;                                                    
    private int verifiedUserID;                                                 //keep verified user on hand ID and Login
    private String verifiedUserLogin;
    
    //Contructor sets up connections and connects
    public mysql() throws SQLException{
        System.out.print( "Setting up Connection... " );
        String url = "jdbc:" + DBType + "://" + DBUrl +  ":" + DBPort + "/" + DBName + "?autoReconnect=true";
        conn = DriverManager.getConnection( url, DBUser, DBPassword );
        System.out.print( "Done!\n" );
    }

    //VerifyUser checks database for user and saves UserID and UserLogin
    public boolean verifyUser( String User, String Pass ) throws SQLException {
        System.out.print( "\tChecking Credintials... " );
        
        //Setting up query and getting result
        String sql = "SELECT * FROM users WHERE User_Name=?;";
        PreparedStatement ps = conn.prepareStatement( sql );
        ps.setString( 1, User );
        ResultSet rs = ps.executeQuery();
        
        //If no result fail login
        if( !rs.next() ) return false;
        
        //If User and Password match attach user name and id to object pass login
        if( Pass.matches( rs.getString( "Password" ) ) ) {
            System.out.print( "Done!\n" );
            this.verifiedUserID = rs.getInt( "User_ID" );
            this.verifiedUserLogin = rs.getString( "User_Name" );
            System.out.println( "\tUserID: " + database.verifiedUserID 
                            + " User Login: " + database.verifiedUserLogin 
                            + "... Logged in!" );
            return true;
        }
        
        return false;   
    }
    
    //Populates Country List from DB runs once
    public void updateCountriesList() throws SQLException {
        //Sending SQL Query and getting result set
        System.out.print( "\tSending SQL Query..." );
        String sql = "SELECT * FROM countries;";
        PreparedStatement ps = conn.prepareStatement( sql );
        ResultSet rs = ps.executeQuery();
        
        //Make sure country list is empty        
        System.out.print( "Done!\n\tClearing Country List..." );
        Countries.countryList.clear();
        
        //each line in the result set is parsed and added as a new object in the list
        System.out.print( "Done.\n\tAdding Results to Country List...\n" );
        while( rs.next() ) {
            Countries.countryList.add( new Countries( rs.getInt( "Country_ID" ), rs.getString( "Country" ) ) );
        }
        System.out.println( "\t\tCountry List Updated to size: " + Countries.countryList.size() + "\n\tDone!" );
    }
    
    //Populates Division List from DB runs once
    public void updateDivisionsList() throws SQLException {
        //Sending SQL Query and getting result set
        System.out.print( "\tSending SQL Query..." );
        String sql = "SELECT * FROM first_level_divisions;";
        PreparedStatement ps = conn.prepareStatement( sql );
        ResultSet rs = ps.executeQuery();
        
        //makeing sure division list is clear
        System.out.print( "Done!\n\tClearing Division List..." );
        Divisions.divisionList.clear();
        
        //each line in the result set is parsed and added as a new object in the list
        System.out.print( "Done.\n\tAdding Results to Division List...\n" );
        while( rs.next() ) {
            Divisions.divisionList.add( new Divisions( rs.getInt( "Division_ID" ), rs.getString( "Division" ), Countries.findCountry( rs.getInt( "Country_ID" ) ) ) );
        }
        System.out.println( "\t\tDivisions List Updated to size: " + Divisions.divisionList.size() + "\n\tDone!" );
    }
    
    //Populates Division List from DB runs once
    public void updateContactsList() throws SQLException {
        //Setting up SQL query and getting result set
        System.out.print( "\tSending SQL Query..." );
        String sql = "SELECT * FROM contacts;";
        PreparedStatement ps = conn.prepareStatement( sql );
        ResultSet rs = ps.executeQuery();
        
        //Making sure contact list is clear
        System.out.print( "Done!\n\tClearing Contacts List..." );
        Contacts.contactList.clear();
        
        //each line in the result set is parsed and added as a new object in the list
        System.out.print( "Done.\n\tAdding Results to Contacts List...\n" );
        while( rs.next() ) {
            Contacts.contactList.add( new Contacts( rs.getInt( "Contact_ID" ), rs.getString( "Contact_Name" ), rs.getString( "Email" ) ) );
        }
        System.out.println("\t\tContacts List Updated to size: " + Contacts.contactList.size() + "\n\tDone!");
    }
    
    //Populates Customer List from DB runs every time customer table modification
    public void updateCustomerList() throws SQLException {
        //Setting up SQL query and getting result set
        System.out.print( "\tSending SQL Query..." );
        String sql = "SELECT * FROM customers;";
        PreparedStatement ps = conn.prepareStatement( sql );
        ResultSet rs = ps.executeQuery();
        
        //Clearing Customer List this could be full
        System.out.print( "Done!\n\tClearing Customer List..." );
        Customers.customerList.clear();
        
        //each line in the result set is parsed and added as a new object in the list
        System.out.print( "Done.\n\tAdding Results to Customer List...\n" );
        while( rs.next() ) {
            Customers.customerList.add( new Customers( rs.getInt( "Customer_ID" ), rs.getString( "Customer_Name" ), rs.getString( "Address" ), rs.getString( "Postal_Code" ), rs.getString( "Phone" ), Divisions.findDivision( rs.getInt( "Division_ID" ) ) ) );
        }
        System.out.println( "\t\tCustomer List Updated to size: " + Customers.customerList.size() + "\n\tDone!" );
    }
    
    //Adds a new customer to the DB and refreshes the list
    public void addCustomer( Customers cus ) throws SQLException {
        //Setting up SQL query with variables added in
        System.out.print( "Making Sql Statement... " );
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        System.out.print( "Done!\nFilling in variables... " );
        ps.setString( 1, cus.getCustomer_Name() );
        ps.setString( 2, cus.getAddress() );
        ps.setString( 3, cus.getPostal_Code() );
        ps.setString( 4, cus.getPhone() );
        ps.setString( 5, verifiedUserLogin );
        ps.setString( 6, verifiedUserLogin );
        ps.setInt( 7, cus.getDivision().getDivisionID() );
        //Execute Query
        System.out.print( "Done!\nExecute Query... " );
        ps.executeUpdate();
        System.out.print( "Done!\nUpdate Cutomers...\n" );
        //Update Customer List
        updateCustomerList();
    }
    
    //Updates an exsisting customer in the DB and refreshes the list
    public void updateCustomer( Customers cus ) throws SQLException {
        //Setting up SQL query with variables added in        
        System.out.print( "Making Sql Statement... " );
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = NOW(), Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?;";
        PreparedStatement ps = conn.prepareStatement( sql );
        System.out.print( "Done!\nFilling in variables... " );
        ps.setString( 1, cus.getCustomer_Name() );
        ps.setString( 2, cus.getAddress() );
        ps.setString( 3, cus.getPostal_Code() );
        ps.setString( 4, cus.getPhone() );
        ps.setString( 5, verifiedUserLogin );
        ps.setInt( 6, cus.getDivision().getDivisionID() );
        ps.setInt( 7, cus.getCustomer_ID() );
        //Execute Query
        System.out.print( "Done!\nExecute Query... " );
        ps.executeUpdate();
        System.out.print( "Done!\nUpdate Cutomers...\n" );
        //Update Customer List
        updateCustomerList();
    }
    
    //Deletes an exsisting customer in the DB and refreshes the list
    public void deleteCustomer( Customers cus ) throws SQLException {
        //First delete any appointments the customer is in
        deleteAppointmentByCustomer( cus );
        //Setting up SQL query with variables added in        
        System.out.print( "Making Sql Statement... " );
        String sql = "DELETE FROM customers WHERE Customer_ID = ?;";
        PreparedStatement ps = conn.prepareStatement( sql );
        System.out.print( "Done!\nFilling in variables... " );
        ps.setInt( 1, cus.getCustomer_ID() );
        //Execute Query
        System.out.print( "Done!\nExecute Query... " );
        ps.executeUpdate();
        System.out.print( "Done!\nUpdate Cutomers...\n" );
        //Update Customer List
        updateCustomerList();
    }
    
    //Updates the appointments list
    public void updateAppointmentList() throws SQLException {
        //Setting up SQL query        
        System.out.print( "\tSending SQL Query..." );
        String sql = "SELECT * FROM appointments;";
        PreparedStatement ps = conn.prepareStatement( sql );
        //Execute Query and read result set
        ResultSet rs = ps.executeQuery();
        
        //Making sure Appointment list is clear
        System.out.print( "Done!\n\tClearing Appointments List..." );
        Appointments.appointmentList.clear();
        
        //Adding each line of the result set to appointment list
        System.out.print( "Done.\n\tAdding Results to Appointments List...\n" );
        while( rs.next() ) {
            Appointments.appointmentList.add( new Appointments( rs.getInt( "Appointment_ID" ), rs.getString( "Title" ), rs.getString( "Description" ), rs.getString( "Location" ), Contacts.findContactByID( rs.getInt( "Contact_ID" ) ), rs.getString( "Type" ), rs.getString( "Start" ), rs.getString( "End" ), Customers.getCustomerByID( rs.getInt( "Customer_ID" ) ) ) );
        }
        System.out.println( "\t\tAppointment List Updated to size: " + Customers.customerList.size() + "\n\tDone!" );
    }
    
    //update appointment list within a specific time frame
    public void updateAppointmentList( String timeStart, String timeEnd ) throws SQLException {
        //Setting up SQL query with variables added in        
        System.out.print( "\tSending SQL Query..." );
        String sql = "SELECT * FROM appointments WHERE (End BETWEEN ? AND ?);";
        PreparedStatement ps = conn.prepareStatement( sql );
        ps.setString( 1, timeStart );
        ps.setString( 2, timeEnd );
        //Execute Query and read result set
        ResultSet rs = ps.executeQuery();

        //Clearing out appointment list
        System.out.print( "Done!\n\tClearing Appointments List..." );        
        Appointments.appointmentList.clear();
        
        //Adding each line of result set into appointment list
        System.out.print( "Done.\n\tAdding Results to Appointments List...\n" );
        while( rs.next() ) {
            Appointments.appointmentList.add( new Appointments( rs.getInt( "Appointment_ID" ), rs.getString( "Title" ), rs.getString( "Description" ), rs.getString( "Location" ), Contacts.findContactByID( rs.getInt( "Contact_ID" ) ), rs.getString( "Type" ), rs.getString( "Start" ), rs.getString( "End" ), Customers.getCustomerByID( rs.getInt( "Customer_ID" ) ) ) );
        }
        System.out.println( "\t\tAppointment List Updated to size: " + Customers.customerList.size() + "\n\tDone!" );
    }
    
    //Adds an appointment to the DB and refreshes the list at the same timeframe
    public void addAppointment( Appointments app ) throws SQLException {        
        //Setting up SQL query with variables added in        
        System.out.print( "Making Sql Statement... " );
        String sql = "INSERT INTO appointments ( Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID ) VALUES ( ?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement( sql );
        System.out.print( "Done!\nFilling in variables... " );
        ps.setInt( 1, app.getApointmentID() );
        ps.setString( 2, app.getTitle() );
        ps.setString( 3, app.getDescription() );
        ps.setString( 4, app.getLocation() );
        ps.setString( 5, app.getType() );
        ps.setString( 6, app.getStartTimeUTC() );
        ps.setString( 7, app.getEndTimeUTC() );
        ps.setString( 8, verifiedUserLogin );
        ps.setString( 9, verifiedUserLogin );
        ps.setInt( 10, app.getCustomer().getCustomer_ID() );
        ps.setInt( 11, verifiedUserID );
        ps.setInt( 12, app.getContact().getID() );
        
        //Execute Query
        System.out.print( "Done!\nExecute Query... " );
        ps.executeUpdate();
        System.out.print( "Done!\nUpdate Appointments...\n" );
        
        //Get the begin and end times from Main Menu and refresh the appointment list
        String begin = Main_MenuController.startView.withZoneSameInstant( ZoneId.of( "UTC+0" ) ).format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) );
        String end = Main_MenuController.endView.withZoneSameInstant( ZoneId.of( "UTC+0" ) ).format( DateTimeFormatter.ISO_LOCAL_DATE );
        updateAppointmentList( begin, end );
    }
    
    //Deletes an Appointment in the DB and refreshes the list at the specific time frame
    public void deleteAppointment( Appointments app ) throws SQLException {
        //Setting up SQL query with variables added in        
        System.out.print( "Making Sql Statement... " );
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?;";
        PreparedStatement ps = conn.prepareStatement( sql );
        System.out.print( "Done!\nFilling in variables... " );
        ps.setInt( 1, app.getApointmentID() );
        
        //Execute Query
        System.out.print( "Done!\nExecute Query... " );
        ps.executeUpdate();
        System.out.print( "Done!\nUpdate Appointments...\n" );
        
        //Get the begin and end times from Main Menu and refresh the appointment list
        String begin = Main_MenuController.startView.withZoneSameInstant( ZoneId.of( "UTC+0" ) ).format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) );
        String end = Main_MenuController.endView.withZoneSameInstant( ZoneId.of( "UTC+0" ) ).format( DateTimeFormatter.ISO_LOCAL_DATE );
        updateAppointmentList( begin, end );
    }
    
    //Deletes all appointments from the DB by the customer ID Number
    public void deleteAppointmentByCustomer( Customers cus ) throws SQLException {
        //Setting up SQL query with variables added in        
        System.out.print( "Making Sql Statement... " );
        String sql = "DELETE FROM appointments WHERE Customer_ID = ?;";
        PreparedStatement ps = conn.prepareStatement( sql );
        System.out.print( "Done!\nFilling in variables... " );
        ps.setInt( 1, cus.getCustomer_ID() );

        //Execute Query
        System.out.print( "Done!\nExecute Query... " );
        ps.executeUpdate();
        System.out.print( "Done!\nUpdate Appointments...\n" );
        
        //Get the begin and end times from Main Menu and refresh the appointment list
        String begin = Main_MenuController.startView.withZoneSameInstant( ZoneId.of( "UTC+0" ) ).format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) );
        String end = Main_MenuController.endView.withZoneSameInstant( ZoneId.of( "UTC+0" ) ).format( DateTimeFormatter.ISO_LOCAL_DATE );
        updateAppointmentList( begin, end );
    }
    
    //Updates an appointment in the DB and refreshes the list
    public void updateAppointment( Appointments app ) throws SQLException {
        //Setting up SQL query with variables added in        
        System.out.print( "Making Sql Statement... " );
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = NOW(), Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?;";
        PreparedStatement ps = conn.prepareStatement( sql );
        System.out.print( "Done!\nFilling in variables... " );
        ps.setString( 1, app.getTitle() );
        ps.setString( 2, app.getDescription() );
        ps.setString( 3, app.getLocation() );
        ps.setString( 4, app.getType() );
        ps.setString( 5, app.getStartTimeUTC() );
        ps.setString( 6, app.getEndTimeUTC() );
        ps.setString( 7, verifiedUserLogin );
        ps.setInt( 8, app.getCustomer().getCustomer_ID() );
        ps.setInt( 9, verifiedUserID );
        ps.setInt( 10, app.getContact().getID() );
        ps.setInt( 11, app.getApointmentID() );

        //Execute Query
        System.out.print( "Done!\nExecute Query... " );
        ps.executeUpdate();
        System.out.print( "Done!\nUpdate Appointments...\n" );
        
        //Get the begin and end times from Main Menu and refresh the appointment list
        String begin = Main_MenuController.startView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = Main_MenuController.endView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ISO_LOCAL_DATE);
        updateAppointmentList(begin, end);
    }
}