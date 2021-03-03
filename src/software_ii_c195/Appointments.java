/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_ii_c195;

import java.time.LocalDateTime;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The Appointments class is the used for appointments
 * @see  appointmentList
 * @author Admin
 */
public class Appointments {
    
    /**
     * This is the major variable that contains all appointment objects
     */
    public static ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();
    
    private final SimpleIntegerProperty apointmentID;
    private final SimpleStringProperty title;
    private final SimpleStringProperty description;
    private final SimpleStringProperty location;
    private final Contacts contact;
    private final SimpleStringProperty type;
    private final ZonedDateTime startTime;
    private final ZonedDateTime endTime;
    private final Customers customer;
    
    //Contructor

    /**
     * Constructor of a new appointment
     * @param ID the set ID of an appointment
     * @param Title the title of an appointment
     * @param Description the description of an appointment
     * @param Location the location of the appointment
     * @param Contact the contact of the appointment in object form
     * @param Type the type of the appointment
     * @param Start the start time of the appointment
     * @param End the end time of the appointment
     * @param Customer the customer of the appointment in object form
     */
    public Appointments(int ID, String Title, String Description, String Location, Contacts Contact, String Type, String Start, String End, Customers Customer) {
        this.apointmentID = new SimpleIntegerProperty(ID);
        this.title = new SimpleStringProperty(Title);
        this.description = new SimpleStringProperty(Description);
        this.location = new SimpleStringProperty(Location);
        this.contact = Contact;
        this.type = new SimpleStringProperty(Type);
        this.startTime = ZonedDateTime.of(LocalDateTime.parse(Start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), ZoneId.of("UTC+0"));
        this.endTime = ZonedDateTime.of(LocalDateTime.parse(End, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), ZoneId.of("UTC+0"));
        this.customer = Customer;
    }
    
    /**
     * Gets appointment ID
     * @return int the ID number
     */
    public int getApointmentID() {
        return this.apointmentID.get();
    }
    
    /**
     * Gets appointment title
     * @return String the appointment title
     */
    public String getTitle() {
        return this.title.get();
    }
    
    /** 
     * Get Appointment Description
     * @return String the appointment description
     */
    public String getDescription() {
        return this.description.get();
    }
    
    /** 
     * get Appointment Location
     * @return String the appointment location
     */
    public String getLocation() {
        return this.location.get();
    }
    
    /** 
     * get appointment type
     * @return String Appointment Type
     */
    public String getType() {
        return this.type.get();
    }
    
    /**
     * get appointment contact
     * @return Contacts the appointment contact
     */
    public Contacts getContact() {
        return this.contact;
    }
    
    /**
     * get appointment customer
     * @return Customers the appointment customer
     */
    public Customers getCustomer() {
        return this.customer;
    }
    
    /**
     * get appointment start time in local time zone
     * @return the appointment start time
     */
    public String getStartTime() {
        ZonedDateTime localTime = this.startTime.withZoneSameInstant(ZoneId.systemDefault());
        return localTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    /**
     * get appointment start time as a ZonedDateTime object
     * @return the appointment start time local zone
     */
    public ZonedDateTime getStartTimeObj() {
        return this.startTime;
    }
    
    /**
     * get appointment start time in UTC time zone
     * @return the appointment start time in UTC
     */
    public String getStartTimeUTC() {
        ZonedDateTime localTime = this.startTime;
        return localTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    /**
     * get appointment end time in local time zone
     * @return the appointment end time 
     */
    public String getEndTime() {
        ZonedDateTime localTime = this.endTime.withZoneSameInstant(ZoneId.systemDefault());
        return localTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    /**
     * get appointment end time as a Zoned Date time Object
     * @return the appointment end time
     */
    public ZonedDateTime getEndTimeObj() {
        return this.endTime;
    }
    
    /**
     * get appointment end time in UTC time zone
     * @return the appointment end time in UTC
     */
    public String getEndTimeUTC() {
        ZonedDateTime localTime = this.endTime;
        return localTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
