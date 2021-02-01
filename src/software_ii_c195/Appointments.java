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
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Admin
 */
public class Appointments {
    
    public static ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();
    
    public SimpleIntegerProperty apointmentID;
    public SimpleStringProperty title;
    public SimpleStringProperty description;
    public SimpleStringProperty location;
    public Contacts contact;
    public SimpleStringProperty type;
    public ZonedDateTime startTime;
    public ZonedDateTime endTime;
    public Customers customer;
    
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
    
    public int getApointmentID() {
        return this.apointmentID.get();
    }
    
    public String getTitle() {
        return this.title.get();
    }
    
    public String getDescription() {
        return this.description.get();
    }
    
    public String getLocation() {
        return this.location.get();
    }
    
    public String getType() {
        return this.type.get();
    }
    
    public Contacts getContact() {
        return this.contact;
    }
    
    public Customers getCustomer() {
        return this.customer;
    }
    
    public String getStartTime() {
        ZonedDateTime localTime = this.startTime.withZoneSameInstant( ZoneId.systemDefault() );
        return localTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    public String getStartTimeUTC() {
        ZonedDateTime localTime = this.startTime;
        return localTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    public String getEndTime() {
        ZonedDateTime localTime = this.endTime.withZoneSameInstant( ZoneId.systemDefault() );
        return localTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    
    public String getEndTimeUTC() {
        ZonedDateTime localTime = this.endTime;
        return localTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
