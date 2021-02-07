/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_ii_c195;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Admin
 */
public class Countries {
    
    //List object used to refrence all countrys
    public static ObservableList<Countries> countryList = FXCollections.observableArrayList();
    
    private final int countryID;
    private final String countryName;
    
    public Countries( int id, String name ) {
        this.countryID = id;
        this.countryName = name;
    }
    
    //return a country object based on the Id passed
    public static Countries findCountryById( int id ) {
        int i = 0;
        while ( i < countryList.size() ) {
            if ( id == countryList.get( i ).countryID )
                return countryList.get( i );
            i++;
        }
        return null;
    }
    
    //return country ID
    public int getCountryID() {
        return this.countryID;
    }
    
    //return country name
    public String getCountryName() {
        return this.countryName;
    }
    
    //convert object to string just name
    @Override
    public String toString() {
        return this.countryName;
    }
}
