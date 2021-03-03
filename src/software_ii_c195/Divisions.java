/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_ii_c195;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Division class contains the ID, name, and country of each division
 * @author Admin
 */
public class Divisions{
    
    /**
     * The major variable where all divisions are stored
     */
    public static ObservableList<Divisions> divisionList = FXCollections.observableArrayList();
    
    //Variables that are part of each object
    private final int ID;
    private final String name;
    private final Countries country;
    
    /**
     * the contructor of the Division object
     * @param ID the id of the division from the database
     * @param name the name of the division from the database
     * @param country the country of the division from the database and ran through findCountryById
     */
    public Divisions (int ID, String name, Countries country) {
        this.ID = ID;
        this.name = name;
        this.country = country;
    }
    
    /**
     * Return division object when passing corrisponding id
     * @param ID the id of the division you want returned
     * @return the division with matching ID
     */
    public static Divisions findDivisionByID(int ID) {
        int i = 0;
        while (i < divisionList.size()) {
            if (ID == divisionList.get(i).getID())
                return divisionList.get(i);
            i++;
        }
        return null;
    }
    
    /**
     * get ID of division object
     * @return the ID of the division
     */
    public int getID() {
        return this.ID;
    }
    
    /**
     * get Name of division object
     * @return the name of the division
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * return country object of division object
     * @return the country of the division
     */
    public Countries getCountry() {
        return this.country;
    }
    
    /**
     * override string of object by returning name
     */
    @Override
    public String toString() {
        return this.name;
    }
    
    /**
     * return list of divisions of certain country used by the customer menu 
     * to only show divisions that belong to a country already selected
     * @param country the country to filter divisions with 
     * @return the filtered list of divisions
     */
    public static ObservableList<Divisions> getDivisionByCountry(Countries country) {
        ObservableList<Divisions> temp = FXCollections.observableArrayList();
        
        int i = 0;
        while (i < divisionList.size()) {
            if (country == divisionList.get(i).getCountry())
                temp.add(divisionList.get(i));
            i++;
        }
        return temp;
    }
}
