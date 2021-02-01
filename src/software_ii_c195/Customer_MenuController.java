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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class Customer_MenuController implements Initializable {

    @FXML
    private Button Customer_Submit;
    @FXML
    private Button Customer_Cancel;
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
    
    public boolean update = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {                        //Initialize with Division disabled until country selected 
        Customer_Div.setDisable(true);
        Customer_Country.setItems(Countries.countryList);
    }

    public void prefill (Customers C) {                                         //PreFill all Customer info
        update = true;
        
        Customer_ID.setText(String.valueOf(C.getCustomer_ID()));
        Customer_Name.setText(C.getCustomer_Name());
        Customer_Add.setText(C.getAddress());
        Customer_Post.setText(C.getPostal_Code());
        Customer_Phone.setText(C.getPhone());
        Customer_Country.setValue(C.getDivision().getCounry());
        Customer_Div.setValue(C.getDivision());
    }

    @FXML
    private void customerSubmit(ActionEvent event) {                            //Submit button is hit
        System.out.print("Submit button hit!\ncreating temp customer...");
        int tempID = 0;
        if (update) tempID = Integer.parseInt(Customer_ID.getText());           // fill in tempID if updating customer
        String tempName = Customer_Name.getText();
        String tempAdd = Customer_Add.getText();
        String tempPost = Customer_Post.getText();
        String tempPhone = Customer_Phone.getText();
        Divisions tempDiv = Customer_Div.getValue();
        Customers C = new Customers(tempID, tempName, tempAdd, tempPost, tempPhone, tempDiv);
        System.out.print(C.getCustomer_Name());
        System.out.print(" Done!\n\tPass temp Customer to mysql...\n");
        try {
            if (!update) mysql.database.addCustomer(C);
            if (update) mysql.database.updateCustomer(C);
        }
        catch (SQLException e) {
            System.out.println("SQL ERROR!!! " + e);
        }
        System.out.print("MySQL Done!\n");
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void customerCancel(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void CountryChange(ActionEvent event) {
        Countries selected = Customer_Country.getValue();
        Customer_Div.setDisable(false);
        Customer_Div.setItems(Divisions.getDivByCountry(selected));
    }
    
}
