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
    
        public static ObservableList<Contacts> contactList = FXCollections.observableArrayList();
        
        private SimpleIntegerProperty contactID;
        private SimpleStringProperty contactName;
        private SimpleStringProperty contactEmail;
        
        public Contacts(int ID, String Name, String Email) {
            this.contactID = new SimpleIntegerProperty(ID);
            this.contactName = new SimpleStringProperty(Name);
            this.contactEmail = new SimpleStringProperty(Email);
        }
        
        public int getID() {
            return this.contactID.get();
        }
        
        public String getName() {
            return this.contactName.get();
        }
        
        public String getEmail() {
            return this.contactEmail.get();
        }
        
        public static Contacts findContactByID(int ID) {
            int i = 0;
            while( i < contactList.size() ) {
                if( ID == contactList.get(i).getID() ) return contactList.get(i);
                i++;
            }
            return null;
        }
        
        @Override
        public String toString() {
            return this.getName();
        }
}
