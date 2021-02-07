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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class Appointment_MenuController implements Initializable {

    @FXML
    private TextField AppID;
    @FXML
    private TextField AppTitle;
    @FXML
    private TextField AppDescription;
    @FXML
    private TextField AppLocation;
    @FXML
    private ComboBox<Contacts> AppContact;
    @FXML
    private TextField AppType;
    @FXML
    private DatePicker AppStartDate;
    @FXML
    private ComboBox<Integer> AppStartHr;
    @FXML
    private ComboBox<Integer> AppStartMin;
    @FXML
    private DatePicker AppEndDate;
    @FXML
    private ComboBox<Integer> AppEndHr;
    @FXML
    private ComboBox<Integer> AppEndMin;
    @FXML
    private ComboBox<Customers> AppCustomer;
    
    private boolean update = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize( URL url, ResourceBundle rb ) {
        //fill combo boxes with lists
        AppContact.setItems( Contacts.contactList );
        AppCustomer.setItems( Customers.customerList );
        
        //Make an observable list with 0-23 hour options
        ObservableList<Integer> hr = FXCollections.observableArrayList();
        int i = 0;
        while( i < 24 ) {
            hr.add( i );
            i++;
        }
        
        //fill hour combo boxes with hr list
        AppStartHr.setItems( hr );
        AppEndHr.setItems( hr );
        
        //make list with 0, 15, 30, 45
        ObservableList<Integer> min = FXCollections.observableArrayList( 0, 15, 30, 45 );
        
        //fill min combo boxes with min list
        AppStartMin.setItems( min );
        AppEndMin.setItems( min );
    }

    //This function fills in the form from the passed variable and sets update to true
    public void prefill ( Appointments app ) {                                         //PreFill all Customer info
        update = true;
        
        AppID.setText( String.valueOf( app.getApointmentID() ) );
        AppTitle.setText( app.getTitle() );
        AppDescription.setText( app.getDescription() );
        AppLocation.setText( app.getLocation() );
        AppContact.setValue( app.getContact() );
        AppType.setText( app.getType() );
        ZonedDateTime convertedStart = app.getStartTimeObj().withZoneSameInstant( ZoneId.systemDefault() );
        AppStartDate.setValue( convertedStart.toLocalDate() );
        AppStartHr.setValue( convertedStart.getHour() );
        AppStartMin.setValue( convertedStart.getMinute() );
        ZonedDateTime convertedEnd = app.getEndTimeObj().withZoneSameInstant( ZoneId.systemDefault() );
        AppEndDate.setValue( convertedEnd.toLocalDate() );
        AppEndHr.setValue( convertedEnd.getHour() );
        AppEndMin.setValue( convertedEnd.getMinute() );
        AppCustomer.setValue( app.getCustomer() );
    }    

    //function runs when the submit button is pressed if update true send appointment to update if not make an appointment
    @FXML
    private void AppSubmit( ActionEvent event ) {
        String tmp;
        tmp = AppID.getText();
        int ID = 0;
        if( update ) ID = Integer.parseInt( tmp );
        
        String title = AppTitle.getText();
        String des = AppDescription.getText();
        String Loc = AppLocation.getText();
        Contacts contact = AppContact.getValue();
        String type = AppType.getText();
        
        LocalDate beginDate = AppStartDate.getValue();
        String startHr = AppStartHr.getValue().toString();
        String startMin = AppStartMin.getValue().toString();
        if( startHr.length() < 2 ) startHr = "0" + startHr;
        if( startMin.length() < 2 ) startMin = "0" + startMin;
        System.out.print( startHr + ":" + startMin );
        LocalTime beginTime = LocalTime.parse( startHr + ":" + startMin, DateTimeFormatter.ISO_TIME );
        ZonedDateTime begin = ZonedDateTime.of( beginDate, beginTime, ZoneId.systemDefault()).withZoneSameInstant( ZoneId.of( "UTC+0" ) );
        
        LocalDate endDate = AppEndDate.getValue();
        String endHr = AppEndHr.getValue().toString();
        String endMin = AppEndMin.getValue().toString();
        if( endHr.length() < 2 ) endHr = "0" + endHr;
        if( endMin.length() < 2 ) endMin = "0" + endMin;
        LocalTime endTime = LocalTime.parse( endHr + ":" + endMin, DateTimeFormatter.ISO_TIME );
        ZonedDateTime end = ZonedDateTime.of( endDate, endTime, ZoneId.systemDefault() ).withZoneSameInstant( ZoneId.of( "UTC+0" ) );
        
        Customers customer = AppCustomer.getValue();
        
        try {
            Appointments app = new Appointments( ID, title, des, Loc, contact, type, begin.format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ), end.format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ), customer );
            if( update ) mysql.database.updateAppointment( app );
            else mysql.database.addAppointment( app );
        
        }
        catch ( SQLException e ) {
            System.out.println( "SQL Error!!! " + e );
        }
        ( ( Node ) ( event.getSource() ) ).getScene().getWindow().hide();
    }

    //Runs when the cancel button is pressed and closed the window
    @FXML
    private void AppCancel(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
}
