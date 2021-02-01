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
    
    public static ObservableList<Countries> countryList = FXCollections.observableArrayList();
    
    private final int countryID;
    private final String countryName;
    
    public Countries(int ID, String Name) {
        this.countryID = ID;
        this.countryName = Name;
    }
    
    public static Countries findCountry(int ID) {
        int i = 0;
        while ( i < countryList.size() ) {
            if ( ID == countryList.get(i).countryID )
                return countryList.get(i);
            i++;
        }
        return null;
    }
    
    public int getCountryID() {
        return this.countryID;
    }
    
    public String getCountryName() {
        return this.countryName;
    }
    
    @Override
    public String toString() {
        return this.countryName;
    }
    
}
