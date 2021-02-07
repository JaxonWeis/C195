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
public class Divisions{
    
    //Object where all divisions are held
    public static ObservableList<Divisions> divisionList = FXCollections.observableArrayList();
    
    //Variables that are part of each object
    private final int ID;
    private final String name;
    private final Countries country;
    
    //Contructor
    public Divisions ( int ID, String name, Countries country ) {
        this.ID = ID;
        this.name = name;
        this.country = country;
    }
    
    //Return division object when passing corrisponding id
    public static Divisions findDivisionByID( int ID ) {
        int i = 0;
        while ( i < divisionList.size() ) {
            if ( ID == divisionList.get( i ).getID() )
                return divisionList.get( i );
            i++;
        }
        return null;
    }
    
    //get ID of division object
    public int getID() {
        return this.ID;
    }
    
    //get Name of division object
    public String getName() {
        return this.name;
    }
    
    //return country object of division object
    public Countries getCountry() {
        return this.country;
    }
    
    //override string of object by returning name
    @Override
    public String toString() {
        return this.name;
    }
    
    //return list of divisions  of certain country
    public static ObservableList<Divisions> getDivisionByCountry( Countries country ) {
        ObservableList<Divisions> temp = FXCollections.observableArrayList();
        
        int i = 0;
        while ( i < divisionList.size() ) {
            if ( country == divisionList.get( i ).getCountry() )
                temp.add( divisionList.get( i ) );
            i++;
        }
        return temp;
    }
}
