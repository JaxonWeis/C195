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
    
    private final SimpleIntegerProperty ID;
    private final SimpleStringProperty name;
    private final SimpleStringProperty address;
    private final SimpleStringProperty postalCode;
    private final SimpleStringProperty phone;
    private final Divisions division;
    
    public Customers( int id, String name, String add, String post, String phone, Divisions div ) {
        this.ID = new SimpleIntegerProperty( id );
        this.name = new SimpleStringProperty( name );
        this.address = new SimpleStringProperty( add );
        this.postalCode = new SimpleStringProperty( post );
        this.phone = new SimpleStringProperty( phone );
        this.division = div;
    }
    
    //get customer id
    public int getID() {
        return this.ID.get();
    }
    
    //get customer Name
    public String getName() {
        return this.name.get();
    }
    
    //get customer address
    public String getAddress() {
        return this.address.get();
    }
    
    //get customer postal code
    public String getPostalCode() {
        return this.postalCode.get();
    }
    
    //get customer phone
    public String getPhone() {
        return this.phone.get();
    }
    
    //get customer division
    public Divisions getDivision() {
        return this.division;
    }
    
    //convert cutomer object to string by returning customer name
    @Override
    public String toString() {
        return this.getName();
    }
    
    //return customer object by entering its ID number
    public static Customers getCustomerByID( int ID ) {
        int i = 0;
        while( i < customerList.size() ) {
            if( ID == customerList.get(i).getID() ) return customerList.get( i );
            i++;
        }
        return null;
    }
}
