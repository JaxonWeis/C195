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
public class Contacts {
    
        //List object made to contain all contacts
        public static ObservableList<Contacts> contactList = FXCollections.observableArrayList();
        
        private final SimpleIntegerProperty contactID;
        private final SimpleStringProperty contactName;
        private final SimpleStringProperty contactEmail;
        
        public Contacts( int id, String name, String email ) {
            this.contactID = new SimpleIntegerProperty( id );
            this.contactName = new SimpleStringProperty( name );
            this.contactEmail = new SimpleStringProperty( email );
        }
        
        //get contact ID
        public int getID() {
            return this.contactID.get();
        }
        
        //get contact name
        public String getName() {
            return this.contactName.get();
        }
        
        //get contact email
        public String getEmail() {
            return this.contactEmail.get();
        }
        
        //find contact by Id 
        public static Contacts findContactByID( int id ) {
            int i = 0;
            while( i < contactList.size() ) {
                if( id == contactList.get(i).getID() ) return contactList.get( i );
                i++;
            }
            return null;
        }
        
        //convert contact object to string just the name for the table
        @Override
        public String toString() {
            return this.getName();
        }
}
