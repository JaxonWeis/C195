/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_ii_c195;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class Report_MenuController implements Initializable {

    @FXML
    private TextArea textArea;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    /**
     * Prefills the report box with the report Text
     * used by the mysql class to output the report to the text area
     * @param str the text of the report to fill in the text area
     */
    public void prefill(String str){
        textArea.setText(str);   
    }
}
