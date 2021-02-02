/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_ii_c195;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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
    @FXML
    private Button SubmitButton;
    @FXML
    private Button CancelButton;
    
    private boolean update = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppContact.setItems(Contacts.contactList);
        AppCustomer.setItems(Customers.customerList);
        
        ObservableList<Integer> hr = FXCollections.observableArrayList();
        int i = 0;
        while( i < 24 ) {
            hr.add(i);
            i++;
        }
        AppStartHr.setItems(hr);
        AppEndHr.setItems(hr);
        
        ObservableList<Integer> min = FXCollections.observableArrayList(0, 15, 30, 45);
        
        AppStartMin.setItems(min);
        AppEndMin.setItems(min);
    }

    public void prefill (Appointments App) {                                         //PreFill all Customer info
        update = true;
        
        AppID.setText( String.valueOf( App.getApointmentID() ) );
        AppTitle.setText( App.getTitle() );
        AppDescription.setText( App.getDescription() );
        AppLocation.setText( App.getLocation() );
        AppContact.setValue( App.getContact() );
        AppType.setText( App.getType() );
        ZonedDateTime convertedStart = App.getStartTimeObj().withZoneSameInstant( ZoneId.systemDefault() );
        AppStartDate.setValue( convertedStart.toLocalDate() );
        AppStartHr.setValue( convertedStart.getHour() );
        AppStartMin.setValue( convertedStart.getMinute() );
        ZonedDateTime convertedEnd = App.getEndTimeObj().withZoneSameInstant( ZoneId.systemDefault() );
        AppEndDate.setValue( convertedEnd.toLocalDate() );
        AppEndHr.setValue( convertedEnd.getHour() );
        AppEndMin.setValue( convertedEnd.getMinute() );
        AppCustomer.setValue( App.getCustomer() );
    }    

    @FXML
    private void AppSubmit(ActionEvent event) {
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
        System.out.print(startHr + ":" + startMin);
        LocalTime beginTime = LocalTime.parse(startHr + ":" + startMin, DateTimeFormatter.ISO_TIME);
        ZonedDateTime begin = ZonedDateTime.of(beginDate, beginTime, ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC+0"));
        
        LocalDate endDate = AppEndDate.getValue();
        String endHr = AppEndHr.getValue().toString();
        String endMin = AppEndMin.getValue().toString();
        if( endHr.length() < 2 ) endHr = "0" + endHr;
        if( endMin.length() < 2 ) endMin = "0" + endMin;
        LocalTime endTime = LocalTime.parse(endHr + ":" + endMin, DateTimeFormatter.ISO_TIME);
        ZonedDateTime end = ZonedDateTime.of(endDate, endTime, ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC+0"));
        
        Customers customer = AppCustomer.getValue();
        
        try {
            Appointments app = new Appointments(ID, title, des, Loc, contact, type, begin.format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ), end.format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ), customer );
            if( update ) mysql.database.updateAppointment( app);
            else mysql.database.addAppointment( app );
        
        }
        catch ( Exception e ) {
            System.out.println("SQL Error!!! " + e);
        }
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void AppCancel(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
}
