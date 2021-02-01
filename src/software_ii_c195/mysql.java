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
 * @author Admin Git Test
 */

public class mysql {
    
    public static mysql database;                                               //database object all pages use to interact with the database
    
    private final Connection conn;                                                    //Make the cinnection private so other classes cant access
    private int verifiedUserID;                                                 //keep verified user on hand ID and Login
    private String verifiedUserLogin;
    
    public mysql() throws SQLException{
        System.out.print("Setting up Connection... ");
        
        String url = "jdbc:mysql://wgudb.ucertify.com:3306/WJ05XJp?autoReconnect=true";
        String user = "U05XJp";
        String password = "53688633129";
        
        conn = DriverManager.getConnection(url, user, password);
        System.out.print("Done!\n");
    }
    
    public boolean verifyUser(String User,String Pass) throws SQLException {
        System.out.print("\tChecking Credintials... ");
        
        String sql = "SELECT * FROM users WHERE User_Name=?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, User);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String DB_Pass = rs.getString("Password");
        
        if(Pass.matches(DB_Pass)) {
            System.out.print("Done!\n");
            database.verifiedUserLogin = rs.getString("User_Name");
            database.verifiedUserID = rs.getInt("User_ID");
            System.out.println("\tUserID: " + database.verifiedUserID + " User Login: " + database.verifiedUserLogin + "... Logged in!");
        }
        return Pass.matches(DB_Pass);   
    }
    
    public void updateCountriesList() throws SQLException {
        System.out.print("\tSending SQL Query...");
        String sql = "SELECT * FROM countries;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        System.out.print("Done!\n\tClearing Country List...");
        
        Countries.countryList.clear();
        
        System.out.print("Done.\n\tAdding Results to Country List...\n");
        while(rs.next()) {
            //System.out.println("\t\t" + rs.getInt("Country_ID") + " " + rs.getString("Country"));
            Countries.countryList.add(new Countries(rs.getInt("Country_ID"), rs.getString("Country")));
        }
        System.out.println("\t\tCountry List Updated to size: " + Countries.countryList.size() + "\n\tDone!");
    }
    
    public void updateDivisionsList() throws SQLException {
        System.out.print("\tSending SQL Query...");
        String sql = "SELECT * FROM first_level_divisions;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        System.out.print("Done!\n\tClearing Division List...");
        
        Divisions.divisionList.clear();
        
        System.out.print("Done.\n\tAdding Results to Division List...\n");
        while(rs.next()) {
            //System.out.println("\t\t" + rs.getInt("Division_ID") + " " + rs.getString("Division"));
            Divisions.divisionList.add(new Divisions(rs.getInt("Division_ID"), rs.getString("Division"), Countries.findCountry(rs.getInt("Country_ID"))));
        }
        System.out.println("\t\tDivisions List Updated to size: " + Divisions.divisionList.size() + "\n\tDone!");
    }
    
    public void updateContactsList() throws SQLException {
        System.out.print("\tSending SQL Query...");
        String sql = "SELECT * FROM contacts;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        System.out.print("Done!\n\tClearing Contacts List...");
        
        Contacts.contactList.clear();
        
        System.out.print("Done.\n\tAdding Results to Contacts List...\n");
        while(rs.next()) {
            Contacts.contactList.add(new Contacts(rs.getInt("Contact_ID"), rs.getString("Contact_Name"), rs.getString("Email")));
        }
        System.out.println("\t\tContacts List Updated to size: " + Contacts.contactList.size() + "\n\tDone!");
    }
    
    public void updateCustomerList() throws SQLException {
        System.out.print("\tSending SQL Query...");
        String sql = "SELECT * FROM customers;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        System.out.print("Done!\n\tClearing Customer List...");
        
        Customers.customerList.clear();
        
        System.out.print("Done.\n\tAdding Results to Customer List...\n");
        while(rs.next()) {
            //System.out.println("\t\t" + rs.getInt("Customer_ID") + " " + rs.getString("Customer_Name"));
            Customers.customerList.add(new Customers(rs.getInt("Customer_ID"), rs.getString("Customer_Name"), rs.getString("Address"), rs.getString("Postal_Code"), rs.getString("Phone"), Divisions.findDivision(rs.getInt("Division_ID"))));
        }
        System.out.println("\t\tCustomer List Updated to size: " + Customers.customerList.size() + "\n\tDone!");
    }
        
    public void addCustomer(Customers C) throws SQLException {
        System.out.print("Making Sql Statement... ");
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        System.out.print("Done!\nFilling in variables... ");
        ps.setString(1, C.getCustomer_Name());
        ps.setString(2, C.getAddress());
        ps.setString(3, C.getPostal_Code());
        ps.setString(4, C.getPhone());
        ps.setString(5, verifiedUserLogin);
        ps.setString(6, verifiedUserLogin);
        ps.setInt(7, C.getDivision().getDivisionID());
        System.out.print("Done!\nExecute Query... ");
        ps.executeUpdate();
        System.out.print("Done!\nUpdate Cutomers...\n");
        updateCustomerList();
    }
    
    public void updateCustomer(Customers C) throws SQLException {
        System.out.print("Making Sql Statement... ");
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = NOW(), Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        System.out.print("Done!\nFilling in variables... ");
        ps.setString(1, C.getCustomer_Name());
        ps.setString(2, C.getAddress());
        ps.setString(3, C.getPostal_Code());
        ps.setString(4, C.getPhone());
        ps.setString(5, verifiedUserLogin);
        ps.setInt(6, C.getDivision().getDivisionID());
        ps.setInt(7, C.getCustomer_ID());
        System.out.print("Done!\nExecute Query... ");
        ps.executeUpdate();
        System.out.print("Done!\nUpdate Cutomers...\n");
        updateCustomerList();
    }
    
    public void deleteCustomer(Customers C) throws SQLException {
        deleteAppointmentByCustomer(C);
        System.out.print("Making Sql Statement... ");
        String sql = "DELETE FROM customers WHERE Customer_ID = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        System.out.print("Done!\nFilling in variables... ");
        ps.setInt(1, C.getCustomer_ID());
        System.out.print("Done!\nExecute Query... ");
        ps.executeUpdate();
        System.out.print("Done!\nUpdate Cutomers...\n");
        updateCustomerList();
    }
    
    public void updateAppointmentList() throws SQLException {
        System.out.print("\tSending SQL Query...");
        String sql = "SELECT * FROM appointments;";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        System.out.print("Done!\n\tClearing Appointments List...");
        
        Appointments.appointmentList.clear();
        
        System.out.print("Done.\n\tAdding Results to Appointments List...\n");
        while(rs.next()) {
            Appointments.appointmentList.add(new Appointments(rs.getInt("Appointment_ID"), rs.getString("Title"), rs.getString("Description"), rs.getString("Location"), Contacts.findContactByID(rs.getInt("Contact_ID")), rs.getString("Type"), rs.getString("Start"), rs.getString("End"), Customers.getCustomerByID(rs.getInt("Customer_ID"))));
        }
        System.out.println("\t\tAppointment List Updated to size: " + Customers.customerList.size() + "\n\tDone!");
    }
    
    public void updateAppointmentList(String TimeStart, String TimeEnd) throws SQLException {
        System.out.print("\tSending SQL Query...");
        String sql = "SELECT * FROM appointments WHERE (End BETWEEN ? AND ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, TimeStart);
        ps.setString(2, TimeEnd);
        ResultSet rs = ps.executeQuery();
        System.out.print("Done!\n\tClearing Appointments List...");
        
        Appointments.appointmentList.clear();
        
        System.out.print("Done.\n\tAdding Results to Appointments List...\n");
        while(rs.next()) {
            Appointments.appointmentList.add(new Appointments(rs.getInt("Appointment_ID"), rs.getString("Title"), rs.getString("Description"), rs.getString("Location"), Contacts.findContactByID(rs.getInt("Contact_ID")), rs.getString("Type"), rs.getString("Start"), rs.getString("End"), Customers.getCustomerByID(rs.getInt("Customer_ID"))));
        }
        System.out.println("\t\tAppointment List Updated to size: " + Customers.customerList.size() + "\n\tDone!");
    }
    
    public void addAppointment(Appointments app) throws SQLException {
        System.out.print("Making Sql Statement... ");
        String sql = "INSERT INTO appointments ( Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID ) VALUES ( ?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?, ?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(sql);
        System.out.print("Done!\nFilling in variables... ");
        ps.setInt(1, app.getApointmentID());
        ps.setString(2, app.getTitle());
        ps.setString(3, app.getDescription());
        ps.setString(4, app.getLocation());
        ps.setString(5, app.getType());
        ps.setString(6, app.getStartTimeUTC());
        ps.setString(7, app.getEndTimeUTC());
        ps.setString(8, verifiedUserLogin);
        ps.setString(9, verifiedUserLogin);
        ps.setInt(10, app.getCustomer().getCustomer_ID());
        ps.setInt(11, verifiedUserID);
        ps.setInt(12, app.getContact().getID());
        System.out.print("Done!\nExecute Query... ");
        ps.executeUpdate();
        System.out.print("Done!\nUpdate Appointments...\n");
        
        String begin = Main_MenuController.startView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = Main_MenuController.endView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ISO_LOCAL_DATE);
        updateAppointmentList(begin, end);
    }
    
    public void deleteAppointment(Appointments App) throws SQLException {
        System.out.print("Making Sql Statement... ");
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        System.out.print("Done!\nFilling in variables... ");
        ps.setInt(1, App.getApointmentID());
        System.out.print("Done!\nExecute Query... ");
        ps.executeUpdate();
        System.out.print("Done!\nUpdate Appointments...\n");
        
        String begin = Main_MenuController.startView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = Main_MenuController.endView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ISO_LOCAL_DATE);
        updateAppointmentList(begin, end);
    }
    
    public void deleteAppointmentByCustomer(Customers C) throws SQLException {
        System.out.print("Making Sql Statement... ");
        String sql = "DELETE FROM appointments WHERE Customer_ID = ?;";
        PreparedStatement ps = conn.prepareStatement(sql);
        System.out.print("Done!\nFilling in variables... ");
        ps.setInt(1, C.getCustomer_ID());
        System.out.print("Done!\nExecute Query... ");
        ps.executeUpdate();
        System.out.print("Done!\nUpdate Appointments...\n");
        
        String begin = Main_MenuController.startView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = Main_MenuController.endView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ISO_LOCAL_DATE);
        updateAppointmentList(begin, end);
    }
}
