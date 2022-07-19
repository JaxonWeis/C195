/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_ii_c195;

import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Reports class launch the report page
 * @author Admin
 */
public class Reports {
    
    /**
     *
     */
    public Reports(){
        
    }
    
    /**
     * Report launches the Report UI and prefills it with Database result
     * @param i use 1, 2, 3 for different reports
     */
    public void Report(int i) {
        try {
            FXMLLoader loader = new FXMLLoader( getClass().getResource( "Report_Menu.fxml" ) );
            Parent root = loader.load();
            Scene scene = new Scene( root );
            Stage stage = new Stage();
            stage.setTitle( "CalenDo - Reports" );
            stage.setScene( scene );
            stage.show();

            Report_MenuController controller = loader.getController();
            switch(i){
                case 1:
                    controller.prefill(mysql.database.report1());
                    break;
                case 2:
                    controller.prefill(mysql.database.report2());
                    break;
                case 3:
                    controller.prefill(mysql.database.report3());
                    break;
            }
        }
        catch (IOException | SQLException e) {
            System.out.println("Error!!!" + e);
        }
    }
}
