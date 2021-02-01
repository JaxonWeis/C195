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
 *
 * @author Admin
 */
public class Customers {
    
    public static ObservableList<Customers> customerList = FXCollections.observableArrayList();
    
    private final SimpleIntegerProperty Customer_ID;
    private final SimpleStringProperty Customer_Name;
    private final SimpleStringProperty Address;
    private final SimpleStringProperty Postal_Code;
    private final SimpleStringProperty Phone;
    private Divisions division;
    
    public Customers(int id, String name, String add, String post, String phone, Divisions div) { //Contructor
        this.Customer_ID = new SimpleIntegerProperty(id);
        this.Customer_Name = new SimpleStringProperty(name);
        this.Address = new SimpleStringProperty(add);
        this.Postal_Code = new SimpleStringProperty(post);
        this.Phone = new SimpleStringProperty(phone);
        this.division = div;
    }
    
    public int getCustomer_ID() {
        return this.Customer_ID.get();
    }
    
    public String getCustomer_Name() {
        return this.Customer_Name.get();
    }
    
    public String getAddress() {
        return this.Address.get();
    }
    
    public String getPostal_Code() {
        return this.Postal_Code.get();
    }
    
    public String getPhone() {
        return this.Phone.get();
    }
    
    public Divisions getDivision() {
        return this.division;
    }
    
    @Override
    public String toString() {
        return this.getCustomer_Name();
    }
    
    public static Customers getCustomerByID(int ID) {
        int i = 0;
        while( i < customerList.size() ) {
            if( ID == customerList.get(i).getCustomer_ID() ) return customerList.get(i);
            i++;
        }
        return null;
    }
}
