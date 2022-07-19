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
import java.time.DayOfWeek;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 * the appointment add/update menu controller
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
    private Label ErrorMsg;
    @FXML
    private Button SubmitButton;
    @FXML
    private Button CancelButton;
    
    private boolean update = false;


    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //fill combo boxes with lists
        AppContact.setItems(Contacts.contactList);
        AppCustomer.setItems(Customers.customerList);
        
        //Make an observable list with 0-23 hour options
        ObservableList<Integer> hr = FXCollections.observableArrayList();
        int i = 0;
        while(i < 24) {
            hr.add(i);
            i++;
        }
        
        //fill hour combo boxes with hr list
        AppStartHr.setItems(hr);
        AppEndHr.setItems(hr);
        
        //make list with 0, 15, 30, 45
        ObservableList<Integer> min = FXCollections.observableArrayList(0, 15, 30, 45);
        
        //fill min combo boxes with min list
        AppStartMin.setItems(min);
        AppEndMin.setItems(min);
        
        //Clear the error at top of screen
        ClearError(new ActionEvent());
    }

    /**
     * This function fills in the form from the passed variable and sets update to true 
     * used by the Main Menu controller to fill in the menu
     * @param app the appointment to prefill into the form
     */
    public void prefill (Appointments app) {                                         //PreFill all Customer info
        update = true;
        
        AppID.setText(String.valueOf(app.getApointmentID()));
        AppTitle.setText(app.getTitle());
        AppDescription.setText(app.getDescription());
        AppLocation.setText(app.getLocation());
        AppContact.setValue(app.getContact());
        AppType.setText(app.getType());
        ZonedDateTime convertedStart = app.getStartTimeObj().withZoneSameInstant(ZoneId.systemDefault());
        AppStartDate.setValue(convertedStart.toLocalDate());
        AppStartHr.setValue(convertedStart.getHour());
        AppStartMin.setValue(convertedStart.getMinute());
        ZonedDateTime convertedEnd = app.getEndTimeObj().withZoneSameInstant(ZoneId.systemDefault());
        AppEndDate.setValue(convertedEnd.toLocalDate());
        AppEndHr.setValue(convertedEnd.getHour());
        AppEndMin.setValue(convertedEnd.getMinute());
        AppCustomer.setValue(app.getCustomer());
    }    

    //function runs when the submit button is pressed if update true send appointment to update if not make an appointment
    /**
     * This function runs when the submit button is pressed. <p>It collects all the information from the
     * form and compiles it into a appointment object. It uses the isBusinessHours function to check
     * if the new app is in business hours and uses the isOverlapping function to see if the new appointment
     * overlaps with any other appointment. Then it uses the mysql.AddAppointment function to add the appointment
     * to the database.
     */
    @FXML
    private void AppSubmit(ActionEvent event) {
        String tmp;
        tmp = AppID.getText();
        int ID = 0;
        if(update) ID = Integer.parseInt(tmp);
        
        String title = AppTitle.getText();
        String des = AppDescription.getText();
        String Loc = AppLocation.getText();
        Contacts contact = AppContact.getValue();
        String type = AppType.getText();
        
        LocalDate beginDate = AppStartDate.getValue();
        String startHr = AppStartHr.getValue().toString();
        String startMin = AppStartMin.getValue().toString();
        if(startHr.length() < 2) startHr = "0" + startHr;
        if(startMin.length() < 2) startMin = "0" + startMin;
        System.out.println(startHr + ":" + startMin);
        LocalTime beginTime = LocalTime.parse(startHr + ":" + startMin, DateTimeFormatter.ISO_TIME);
        ZonedDateTime begin = ZonedDateTime.of(beginDate, beginTime, ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC+0"));
        System.out.println("Time: " + beginTime);
        System.out.println("TimeDate: " + begin);
        
        LocalDate endDate = AppEndDate.getValue();
        String endHr = AppEndHr.getValue().toString();
        String endMin = AppEndMin.getValue().toString();
        if(endHr.length() < 2) endHr = "0" + endHr;
        if(endMin.length() < 2) endMin = "0" + endMin;
        LocalTime endTime = LocalTime.parse(endHr + ":" + endMin, DateTimeFormatter.ISO_TIME);
        ZonedDateTime end = ZonedDateTime.of(endDate, endTime, ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC+0"));
        
        Customers customer = AppCustomer.getValue();
        Appointments app = new Appointments(ID, title, des, Loc, contact, type, begin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), customer);
        
        if(!isBussinessHours(app)){
            ErrorMsg.setText("Selected Times are outside businness hours");
            return;
        }
        
        //We need to check if the new appointment will overlap any existing appointment
        if(isOverlapping(app)){
            ErrorMsg.setText("Selected Times are overlapping another appointment");
            return;
        }
        
        try {
            if(update) mysql.database.updateAppointment(app);
            else mysql.database.addAppointment(app);
        
        }
        catch (SQLException e) {
            System.out.println("SQL Error!!! " + e);
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    //Runs when the cancel button is pressed and closed the window
    @FXML
    private void AppCancel(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    
    /**
     * Used to check if a new appointment is in business hours in the appointment menu
     * before sending it to the database
     * @param app the appointment to be checked
     * @return true if it is in business hours
     */
    private boolean isBussinessHours(Appointments app){
        ZonedDateTime start = app.getStartTimeObj().withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime end = app.getEndTimeObj().withZoneSameInstant(ZoneId.of("America/New_York"));
        
        ZonedDateTime startLimit1 = ZonedDateTime.of(start.toLocalDate(), LocalTime.of(7, 59), ZoneId.of("America/New_York"));
        ZonedDateTime stopLimit1 = ZonedDateTime.of(start.toLocalDate(), LocalTime.of(22, 1), ZoneId.of("America/New_York"));
        ZonedDateTime startLimit2 = ZonedDateTime.of(end.toLocalDate(), LocalTime.of(7, 59), ZoneId.of("America/New_York"));
        ZonedDateTime stopLimit2 = ZonedDateTime.of(end.toLocalDate(), LocalTime.of(22, 1), ZoneId.of("America/New_York"));

        if(start.getDayOfWeek() == DayOfWeek.SATURDAY) return false;
        if(start.getDayOfWeek() == DayOfWeek.SUNDAY) return false;
        if(end.getDayOfWeek() == DayOfWeek.SATURDAY) return false;
        if(start.getDayOfWeek() == DayOfWeek.SUNDAY) return false;
        return startLimit1.isBefore(start) && stopLimit1.isAfter(start) && startLimit2.isBefore(end) && stopLimit2.isAfter(end);
    }
    
    boolean overlapCheck = false;

    /**
     * Check if the new appointment will overlap existing appointments after hitting
     * the submit button on the appointment menu but before sending it to the database
     * <p>This is used in Appointment_MenuController.AppSubmit. After the user hit submit on a new
     * or modifying appointment the generated appointment will be passed to isOverlapping to see if the
     * Appointment overlaps with any other appointment
     * @see AppSubmit
     * @param app the appointment object to be checked
     * @return true if overlapping false if appointment is clear
     */
    private boolean isOverlapping(Appointments app){
        ZonedDateTime start = app.getStartTimeObj();
        ZonedDateTime end = app.getEndTimeObj();
        System.out.println("Starting OverLap check");
        overlapCheck = false;
        String startDate = start.withZoneSameInstant(ZoneId.of("Z")).withHour(0).withMinute(0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss" ));
        String endDate = end.withZoneSameInstant(ZoneId.of("Z")).withHour(23).withMinute(59).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss" ));
        ObservableList<Appointments> List = FXCollections.observableArrayList();
        
        try{
            List = mysql.database.getAppointmentList(startDate, endDate);
        }
        catch(SQLException e){
            System.out.println("Database Error!!! " + e);
        }
        
        List.forEach(appList -> {
            if(appList.getApointmentID() != app.getApointmentID() ){
                if(appList.getStartTimeObj().isBefore(start)){
                    if(appList.getEndTimeObj().isAfter(start)){
                        overlapCheck = true;
                        System.out.println("OverLap Check Failed!!!");
                    }
                }
                else{
                    if(end.isAfter(appList.getStartTimeObj())){
                        overlapCheck = true;
                        System.out.println("OverLap Check Failed!!!");
                    }
                }
            }
        });
        
        System.out.println("Overlap check done " + overlapCheck);
        return overlapCheck;
    }

    @FXML
    private void ClearError(ActionEvent event) {
        ErrorMsg.setText("");
    }
}
