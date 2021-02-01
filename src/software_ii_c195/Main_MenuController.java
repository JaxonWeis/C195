/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package software_ii_c195;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class Main_MenuController implements Initializable {

    @FXML
    private TableView<Customers> Customer_Table;
    @FXML
    private TableColumn<Customers, Integer> Cus_ID;
    @FXML
    private TableColumn<Customers, String> Cus_Name;
    @FXML
    private TableColumn<Customers, String> Cus_Address;
    @FXML
    private TableColumn<Customers, Divisions> Cus_Division;
    @FXML
    private TableColumn<Customers, String> Cus_Postal;
    @FXML
    private TableColumn<Customers, String> Cus_Phone;
    @FXML
    private Button customerAddButton;
    @FXML
    private Button customerUpdateButton;
    @FXML
    private Button customerDeleteButton;
    @FXML
    private TableView<Appointments> Appointment_Table;
    @FXML
    private TableColumn<Appointments, Integer> AppointmentCol;
    @FXML
    private TableColumn<Appointments, String> TitleCol;
    @FXML
    private TableColumn<Appointments, String> DescriptionCol;
    @FXML
    private TableColumn<Appointments, String> LocationCol;
    @FXML
    private TableColumn<Appointments, Contacts> ContactCol;
    @FXML
    private TableColumn<Appointments, String> TypeCol;
    @FXML
    private TableColumn<Appointments, String> StartCol;
    @FXML
    private TableColumn<Appointments, String> EndCol;
    @FXML
    private TableColumn<Appointments, Customers> CustomerCol;
    @FXML
    private Button AppointmentAddButton;
    @FXML
    private Button AppointmentUpdateButton;
    @FXML
    private Button AppointmentDeleteButton;
    @FXML
    private ToggleGroup ViewPeriod;
    @FXML
    private RadioButton weekRadio;
    @FXML
    private RadioButton monthRadio;
    @FXML
    private Button leftButton;
    @FXML
    private Label viewLabel;
    @FXML
    private Button rightButton;

    public static ZonedDateTime startView;
    public static ZonedDateTime endView;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            System.out.println("Updating Country List...");
            mysql.database.updateCountriesList();
            System.out.println("Done!\nUpdating Division List...");
            mysql.database.updateDivisionsList();
            System.out.println("Done!\nUpdating Contact List...");
            mysql.database.updateContactsList();
            System.out.println("Done!\nUpdating Customer List...");
            mysql.database.updateCustomerList();
            System.out.println("Done!\nUpdating Appointment List...");
            mysql.database.updateAppointmentList();
            System.out.println("Done!");
        }
        catch(SQLException e) {
            System.out.println("SQL ERROR!!! " + e);
        }
        
        Cus_ID.setCellValueFactory( new PropertyValueFactory<>("Customer_ID") );
        Cus_Name.setCellValueFactory( new PropertyValueFactory<>("Customer_Name") );
        Cus_Address.setCellValueFactory( new PropertyValueFactory<>("Address") );
        Cus_Division.setCellValueFactory( new PropertyValueFactory<>("division") );
        Cus_Postal.setCellValueFactory( new PropertyValueFactory<>("Postal_Code") );
        Cus_Phone.setCellValueFactory( new PropertyValueFactory<>("Phone") );
        Customer_Table.setItems(Customers.customerList);
        
        AppointmentCol.setCellValueFactory( new PropertyValueFactory<>("apointmentID") );
        TitleCol.setCellValueFactory( new PropertyValueFactory<>("title") );
        DescriptionCol.setCellValueFactory( new PropertyValueFactory("description") );
        LocationCol.setCellValueFactory( new PropertyValueFactory<>("location") );
        ContactCol.setCellValueFactory( new PropertyValueFactory("contact") );
        TypeCol.setCellValueFactory( new PropertyValueFactory<>("type") );
        CustomerCol.setCellValueFactory( new PropertyValueFactory<>("customer") );
        StartCol.setCellValueFactory( new PropertyValueFactory<>("startTime") );
        EndCol.setCellValueFactory( new PropertyValueFactory<>("endTime") );
        Appointment_Table.setItems(Appointments.appointmentList);
        
        weekRadio.fire();
    }    

    @FXML
    private void customerAdd(ActionEvent event) {
        try {
            //Parent root = FXMLLoader.load(getClass().getResource("Customer_Menu.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Customer_Menu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("CalenDo - Add Customer");
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e) {
            System.out.println("SQL ERROR!!! " + e);
        }
    }

    @FXML
    private void customerUpdate(ActionEvent event) {
        Customers selected = Customer_Table.getSelectionModel().getSelectedItem();
        try {
            //Parent root = FXMLLoader.load(getClass().getResource("Customer_Menu.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Customer_Menu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("CalenDo - Update Customer");
            stage.setScene(scene);
            stage.show();
            
            Customer_MenuController controller = loader.getController();
            controller.prefill(selected);
        }
        catch(IOException e) {
            System.out.println("SQL ERROR!!! " + e);
        }
    }

    @FXML
    private void customerDelete(ActionEvent event) {
        Customers selected = Customer_Table.getSelectionModel().getSelectedItem();
        try {
            mysql.database.deleteCustomer(selected);
        }
        catch (SQLException e) {
            System.out.println("SQL ERROR!!!" + e);
        }
    }

    @FXML
    private void appointmentAdd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Appointment_Menu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("CalenDo - Add Appointment");
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e) {
            System.out.println("SQL ERROR!!! " + e);
        }
    }

    @FXML
    private void appointmentUpdate(ActionEvent event) {
    }

    @FXML
    private void appointmentDelete(ActionEvent event) {
        Appointments selected = Appointment_Table.getSelectionModel().getSelectedItem();
        
        try {
            mysql.database.deleteAppointment(selected);
        }
        catch( SQLException e ) {
            System.out.println("SQL Error!!!" + e);
        }
    }

    @FXML
    private void viewRadioChange(ActionEvent event) {
        if( ViewPeriod.getSelectedToggle().equals(weekRadio) ){
            System.out.println("Week is selected");
            startView = ZonedDateTime.now();
            endView = startView.plusWeeks(1);
        }
        
        if( ViewPeriod.getSelectedToggle().equals(monthRadio)){
            System.out.println("Month is selected");
            startView = ZonedDateTime.now();
            endView = startView.plusMonths(1);
        }
        updateView();
    }
    
    private void updateView() {
        String begin = startView.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String end = endView.format(DateTimeFormatter.ISO_LOCAL_DATE);        
        viewLabel.setText(begin + " :: " + end);
        
        begin = startView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        end = endView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ISO_LOCAL_DATE);
        
        try {
            mysql.database.updateAppointmentList(begin, end);
        }
        catch(SQLException e) {
            System.out.println("SQL Error!!! " + e);
        }
        
    }

    @FXML
    private void ViewMinus(ActionEvent event) {
        if( ViewPeriod.getSelectedToggle().equals(weekRadio) ){
            startView = startView.minusWeeks(1);
            endView = startView.plusWeeks(1);
        }
        
        if( ViewPeriod.getSelectedToggle().equals(monthRadio)){
            startView = startView.minusMonths(1);
            endView = startView.plusMonths(1);
        }
        updateView();
    }

    @FXML
    private void ViewPlus(ActionEvent event) {
        if( ViewPeriod.getSelectedToggle().equals(weekRadio) ){
            startView = startView.plusWeeks(1);
            endView = startView.plusWeeks(1);
        }
        
        if( ViewPeriod.getSelectedToggle().equals(monthRadio)){
            startView = startView.plusMonths(1);
            endView = startView.plusMonths(1);
        }
        updateView();
    }
}
