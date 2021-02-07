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
    ResourceBundle rbLang = ResourceBundle.getBundle("Language.lang", locale );
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize( URL url, ResourceBundle rb ) {
        Platform.runLater( () -> root.requestFocus() );
        
        //set menu language items
        Login_Email.setPromptText( rbLang.getString( "Email" ) );
        Login_Password.setPromptText( rbLang.getString( "Password" ) );
        loginBtn.setText( rbLang.getString( "Login" ) );
        
        
        
        //Clear error message
        Login_ErrorMsg.setText( "" );
        
        //Show country name and zone Id
        String Country_Name = locale.getDisplayCountry();
        String Zone_ID = ZoneId.systemDefault().getDisplayName( TextStyle.FULL, locale );
        Login_ZoneID.setText( Country_Name + " - " + Zone_ID );
        
        try {
            //Try to setup the database
            mysql.database = new mysql();
        }
        catch( SQLException e ) {
            System.out.println( "Database Setup Failed!!! " + e );
        }
    }    

    //Run this function when login button hit
    @FXML
    private void login_Action( ActionEvent event ) {
        System.out.println( "Login Button hit..." );
        
        //Clear error message
        Login_ErrorMsg.setText("");
        
        //fetch user and pass
        String User = Login_Email.getText();
        String Pass = Login_Password.getText();
        boolean verifyUser = false;
        
        //Check database for matching username and password
        try {
            verifyUser = mysql.database.verifyUser( User, Pass );
        }
        catch( SQLException e )
        {
            System.out.println( "Database Failed!!! " + e );
        }
        
        if( verifyUser ) {
            System.out.println( "Login Sucessful!" );
            try {
                Parent root = FXMLLoader.load( getClass().getResource( "Main_Menu.fxml" ) );
                Scene scene = new Scene( root );
                Stage stage = new Stage();
                stage.setTitle( "CalenDo - Main" );
                stage.setScene( scene );
                stage.show();
                
                ( ( Node )( event.getSource() ) ).getScene().getWindow().hide();
            }
            catch( IOException e ) {
                System.out.println( "Error!!! " + e );
            }
        }
        else
        {
            System.out.println( "Login Error!!!" );
            Login_ErrorMsg.setText( rbLang.getString("FailedLogin") );
        }
    }   
}

