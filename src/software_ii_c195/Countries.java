/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_ii_c195;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The Countries class contains country id and name
 * @author Admin
 */
public class Countries {
    
    /**
     * List object used to refrence all countrys
     */
    public static ObservableList<Countries> countryList = FXCollections.observableArrayList();
    
    private final int countryID;
    private final String countryName;
    
    /**
     * The countries constructor
     * @param id the id of the country from the database
     * @param name the name of the country from the database
     */
    public Countries(int id, String name) {
        this.countryID = id;
        this.countryName = name;
    }
    
    /**
     * Get a country object based on the Id passed
     * used by the mysql class to assign a country to a division
     * @param id the id of the country you want to return
     * @return the country of the id entered
     */
    public static Countries findCountryById(int id) {
        int i = 0;
        while (i < countryList.size()) {
            if (id == countryList.get(i).countryID)
                return countryList.get(i);
            i++;
        }
        return null;
    }
    
    /**
     * return country ID
     * @return the country id
     */
    public int getCountryID() {
        return this.countryID;
    }
    
    /**
     * return country name
     * @return the country name
     */
    public String getCountryName() {
        return this.countryName;
    }
    
    /**
     * Returns a nice looking string to display in the customer menu 
     * @return 
     */
    @Override
    public String toString() {
        return this.countryName;
    }
}
