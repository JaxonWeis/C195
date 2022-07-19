/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_ii_c195;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Customer contains customers information like ID, name, address, postal code, phone, and division
 * @author Admin
 */
public class Customers {
    
    /**
     * CustomerList is the major variable that contains all of the customers
     */
    public static ObservableList<Customers> customerList = FXCollections.observableArrayList();
    
    private final SimpleIntegerProperty ID;
    private final SimpleStringProperty name;
    private final SimpleStringProperty address;
    private final SimpleStringProperty postalCode;
    private final SimpleStringProperty phone;
    private final Divisions division;
    
    /**
     * The constructor for the customer object
     * @param id the id of the customer from the database
     * @param name the name of the customer from the database
     * @param add the address of the customer from the database
     * @param post the postal code of the customer from the database
     * @param phone the phone number of the customer from the database
     * @param div the division object of the customer from the database processed by findDivisionById
     */
    public Customers(int id, String name, String add, String post, String phone, Divisions div) {
        this.ID = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(add);
        this.postalCode = new SimpleStringProperty(post);
        this.phone = new SimpleStringProperty(phone);
        this.division = div;
    }
    
    /**
     * get customer id
     * @return the customer ID
     */
    public int getID() {
        return this.ID.get();
    }
    
    /**
     * get customer Name
     * @return the customer name
     */
    public String getName() {
        return this.name.get();
    }
    
    /**
     * get customer address
     * @return the customer address
     */
    public String getAddress() {
        return this.address.get();
    }
    
    /**
     * get customer postal code
     * @return the customer postal code
     */
    public String getPostalCode() {
        return this.postalCode.get();
    }
    
    /**
     * get customer phone
     * @return the customer phone
     */
    public String getPhone() {
        return this.phone.get();
    }
    
    /**
     * get customer division
     * @return the customer division
     */
    public Divisions getDivision() {
        return this.division;
    }
    
    /**
     * convert customer object to string by returning customer name
     */
    @Override
    public String toString() {
        return this.getName();
    }
    
    /**
     * return customer object by entering its ID number used by the appointment contructor
     * to make an appointment object with a customer object
     * @param ID the id of the customer you want returned
     * @return the customer with the matching id number
     */
    public static Customers getCustomerByID(int ID) {
        int i = 0;
        while(i < customerList.size()) {
            if(ID == customerList.get(i).getID()) return customerList.get(i);
            i++;
        }
        return null;
    }
}
