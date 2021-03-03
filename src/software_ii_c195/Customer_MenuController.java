/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_ii_c195;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class Customer_MenuController implements Initializable {

    @FXML
    private TextField Customer_ID;
    @FXML
    private TextField Customer_Name;
    @FXML
    private TextField Customer_Add;
    @FXML
    private TextField Customer_Post;
    @FXML
    private TextField Customer_Phone;
    @FXML
    private ComboBox<Divisions> Customer_Div;
    @FXML
    private ComboBox<Countries> Customer_Country;
    
    private boolean update = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Customer_Div.setDisable(true);
        Customer_Country.setItems(Countries.countryList);
    }

    //This function is used to prefill the form and set it to update

    /**
     * Prefills the customer menu to allow an user to edit a customer
     * @param cus the customer object to fill the table
     */
    public void prefill (Customers cus) {
        update = true;
        
        Customer_ID.setText(String.valueOf(cus.getID()));
        Customer_Name.setText(cus.getName());
        Customer_Add.setText(cus.getAddress());
        Customer_Post.setText(cus.getPostalCode());
        Customer_Phone.setText(cus.getPhone());
        Customer_Country.setValue(cus.getDivision().getCountry());
        Customer_Div.setValue(cus.getDivision());
    }

    /**
     * Compiles all the menu information when the submit button is hit
     */
    @FXML
    private void customerSubmit(ActionEvent event) {
        System.out.print("Submit button hit!\ncreating temp customer...");
        int tempID = 0;
        if (update) tempID = Integer.parseInt(Customer_ID.getText());
        String tempName = Customer_Name.getText();
        String tempAdd = Customer_Add.getText();
        String tempPost = Customer_Post.getText();
        String tempPhone = Customer_Phone.getText();
        Divisions tempDiv = Customer_Div.getValue();
        Customers cus = new Customers(tempID, tempName, tempAdd, tempPost, tempPhone, tempDiv);
        System.out.print(cus.getName());
        System.out.print(" Done!\n\tPass temp Customer to mysql...\n");
        try {
            if (update) mysql.database.updateCustomer(cus);
            else mysql.database.addCustomer(cus);
        }
        catch (SQLException e) {
            System.out.println("SQL ERROR!!! " + e);
        }
        System.out.print("MySQL Done!\n");
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    //function that runs when cancel button is hit
    @FXML
    private void customerCancel(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    //when the country is seleced update the division list
    @FXML
    private void CountryChange(ActionEvent event) {
        Countries selected = Customer_Country.getValue();
        Customer_Div.setDisable(false);
        Customer_Div.setItems(Divisions.getDivisionByCountry(selected));
    }
}
