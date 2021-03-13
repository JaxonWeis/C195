/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_ii_c195;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;
import javafx.event.ActionEvent;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class Login_MenuController implements Initializable {

    @FXML
    private Label Login_ErrorMsg;
    @FXML
    private Label Login_ZoneID;
    @FXML
    private TextField Login_Email;
    @FXML
    private PasswordField Login_Password;
    @FXML
    private BorderPane root;
    @FXML
    private Button loginBtn;
    
    Locale locale = Locale.getDefault();
    ResourceBundle rbLang = ResourceBundle.getBundle("Language.lang", locale);
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> root.requestFocus());
        
        //set menu language items
        Login_Email.setPromptText(rbLang.getString("Email"));
        Login_Password.setPromptText(rbLang.getString("Password"));
        loginBtn.setText(rbLang.getString("Login"));
        
        //Clear error message
        Login_ErrorMsg.setText("");
        
        //Show country name and zone Id
        String Country_Name = locale.getDisplayCountry();
        String Zone_ID = ZoneId.systemDefault().getDisplayName(TextStyle.FULL, locale);
        Login_ZoneID.setText(Country_Name + " - " + Zone_ID);
        
        try {
            //Try to setup the database
            mysql.database = new mysql();
        }
        catch(SQLException e) {
            System.out.println("Database Setup Failed!!! " + e);
        }
        
        //Create log file if one hasn't been made
        Log.createLog();
        
    }    

    //Run this function when login button hit
    /**
     * This function verifys collects the user password input from the form. 
     * <p>This function grabs the login user and password information. Passes them to the
     * mysql.verifyUser function to determine if the user password combination exist in the database. 
     * <p>If true the login_Menu closes and opens the Main_Menu
     * <p>If false it writes login failed to the log file and to the error message part of the login page
     */
    @FXML
    private void login_Action(ActionEvent event) {
        System.out.println("Login Button hit...");
        
        //Clear error message
        Login_ErrorMsg.setText("");
        
        //fetch user and pass
        String User = Login_Email.getText();
        String Pass = Login_Password.getText();
        boolean verifyUser = false;
        
        //We need to check the user and password against the database to securly log in
        try {
            verifyUser = mysql.database.verifyUser(User, Pass);
        }
        catch(SQLException e)
        {
            System.out.println("Database Failed!!! " + e);
        }
        
        if(verifyUser) {
            System.out.println("Login Successful!");
            try {
                Log.write(User + " Login Successful.");
                
                Parent root = FXMLLoader.load(getClass().getResource("Main_Menu.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("CalenDo - Main");
                stage.setScene(scene);
                stage.show();
                
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            catch(IOException e) {
                System.out.println("Error!!! " + e);
            }
        }
        else
        {
            Log.write(User + " Login Failed.");
            System.out.println("Login Error!!!");
            Login_ErrorMsg.setText(rbLang.getString("FailedLogin"));
        }
    }   
}

