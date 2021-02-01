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
public class Divisions {
    
    public static ObservableList<Divisions> divisionList = FXCollections.observableArrayList();
    
    private final int divisionID;
    private final String divisionName;
    private final Countries country;
    
    public Divisions (int ID, String name, Countries country) {
        this.divisionID = ID;
        this.divisionName = name;
        this.country = country;
    }
    
    public static Divisions findDivision(int ID) {
                int i = 0;
        while ( i < divisionList.size() ) {
            if ( ID == divisionList.get(i).divisionID )
                return divisionList.get(i);
            i++;
        }
        return null;
    }
    
    public int getDivisionID() {
        return this.divisionID;
    }
    
    public String getDivisionName() {
        return this.divisionName;
    }
    
    public Countries getCounry() {
        return this.country;
    }
    
    @Override
    public String toString() {
        return this.divisionName;
    }
    
    public static ObservableList<Divisions> getDivByCountry(Countries country) {
        ObservableList<Divisions> temp = FXCollections.observableArrayList();
        
        int i = 0;
        while (i < divisionList.size()) {
            if (country == divisionList.get(i).country)
                temp.add(divisionList.get(i));
            i++;
        }
        return temp;
    }
}
