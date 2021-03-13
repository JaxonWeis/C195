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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Button;
import javax.swing.JOptionPane;

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
    private ToggleGroup ViewPeriod;
    @FXML
    private RadioButton weekRadio;
    @FXML
    private RadioButton monthRadio;
    @FXML
    private Label viewLabel;
    @FXML
    private Label NextAppointment;
    @FXML
    private Button AppointmentAddButton;
    @FXML
    private Button AppointmentUpdateButton;
    @FXML
    private Button AppointmentDeleteButton;
    @FXML
    private Button leftButton;
    @FXML
    private Button rightButton;
    @FXML
    private Button customerAddButton;
    @FXML
    private Button customerUpdateButton;
    @FXML
    private Button customerDeleteButton;
    

    public static ZonedDateTime startView;
    public static ZonedDateTime endView;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            //Update all lists
            System.out.println("Updating Country List...");
            mysql.database.updateCountriesList();
            System.out.println("Done!\nUpdating Division List...");
            mysql.database.updateDivisionsList();//Requires Countries
            System.out.println("Done!\nUpdating Contact List...");
            mysql.database.updateContactsList();
            System.out.println("Done!\nUpdating Customer List...");
            mysql.database.updateCustomerList();//Requires Divisions 
            System.out.println("Done!\nUpdating Appointment List...");
            mysql.database.updateAppointmentList();// Requires Contacts and Customers
            System.out.println("Done!");
            
            System.out.println("Checking for appointments within 15 minutes... ");
            startView = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
            //startView.minusDays(0);
            endView = startView.plusMinutes(15);
            String begin = startView.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String end = endView.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            mysql.database.updateAppointmentListByStart(begin, end);
            System.out.println("Done! Found " + Appointments.appointmentList.size() + " Appointments");
        }
        catch(SQLException e) {
            System.out.println("SQL ERROR!!! " + e);
        }
        
        if(Appointments.appointmentList.size() > 0) NextAppointment.setText("Id: "+ Appointments.appointmentList.get(0).getApointmentID() + " Appointment: " + Appointments.appointmentList.get(0).getTitle() + " Starts at: " + Appointments.appointmentList.get(0).getStartTime());
        else NextAppointment.setText("There are no appointments soon.");
        
        //Setup table and columns for customer list
        Cus_ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        Cus_Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        Cus_Address.setCellValueFactory(new PropertyValueFactory<>("address"));
        Cus_Division.setCellValueFactory(new PropertyValueFactory<>("division"));
        Cus_Postal.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        Cus_Phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        Customer_Table.setItems(Customers.customerList);
        
        //Setup table and column for appointment list
        AppointmentCol.setCellValueFactory(new PropertyValueFactory<>("apointmentID"));
        TitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        DescriptionCol.setCellValueFactory(new PropertyValueFactory("description"));
        LocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        ContactCol.setCellValueFactory(new PropertyValueFactory("contact"));
        TypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        CustomerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        StartCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        EndCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        Appointment_Table.setItems(Appointments.appointmentList);
        
        //Fire teh week filter to prefil the appointments list and preselect filter
        weekRadio.fire();
    }    

    //When add customer button is presssed cutomer menu opens to add new customer to database
    @FXML
    private void customerAdd(ActionEvent event) {
        try {
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

    //When the update button is pressed get the selected cutomer and pass it to the menu using prefill function
    @FXML
    private void customerUpdate(ActionEvent event) {
        Customers selected = Customer_Table.getSelectionModel().getSelectedItem();
        if(selected == null) return;
        
        try {
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

    /**
     * This function runs when the Customer Delete button is pressed.
     * <p> This function grabs the selected customer from the customer table, if there is
     * one selected. Then it will start a new thread that will open a pop-up box to ask
     * if you are sure you want to delete the customer. If the user hits ok then the selected 
     * customer gets passed to mysql.deleteCustomer() to be removed from the database
     */
    @FXML
    private void customerDelete(ActionEvent event) {
        Customers selected = Customer_Table.getSelectionModel().getSelectedItem();
        
        if(selected == null) return;
        
        Runnable CDRun =
        () -> { if (!ConfirmCustomerDelete(selected)) return;
            
                try {
                    mysql.database.deleteCustomer(selected);
                }
                catch (SQLException e) {
                    System.out.println("SQL ERROR!!!" + e);
                } };
        Thread thread = new Thread(CDRun);
        thread.start();
    }
    
    //Open the appointment menu
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

    //Get the selected appointment and send it to appointment menu with prefill funtion
    @FXML
    private void appointmentUpdate(ActionEvent event) {
        Appointments selected = Appointment_Table.getSelectionModel().getSelectedItem();
        if(selected == null) return;//don't finish function if nothing is selected
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Appointment_Menu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("CalenDo - Update Appointment");
            stage.setScene(scene);
            stage.show();
            
            Appointment_MenuController controller = loader.getController();
            controller.prefill(selected);
        }
        catch(IOException e) {
            System.out.println("SQL ERROR!!! " + e);
        }
    }

    /**
     * When the appointment delete button is pressed this function will run
     * <p>This function will pull the appointment that is selected and make sure an appointment
     * was selected. It will start a new thread to confirm if the user wants to delete the appointment with
     * a popup. Once the popup was confirmed then it will pass the selected appointment to 
     * mysql.deleteAppointment() to remove it from the database.
     */
    @FXML
    private void appointmentDelete(ActionEvent event) {
        Appointments selected = Appointment_Table.getSelectionModel().getSelectedItem();
        
        if(selected == null) return;
        
        //AppDeleteRun ADRun = new AppDeleteRun(selected);
        //new Thread(ADRun).start();
        
        Runnable ADRun =
            () -> { if (!ConfirmAppointmentDelete(selected)) return;
            
            try {
                mysql.database.deleteAppointment(selected);
            }
            catch (SQLException e) {
                System.out.println("SQL ERROR!!!" + e);
            } };
        Thread thread = new Thread(ADRun);
        thread.start();
    }

    //On radio button change update the timeline 
    @FXML
    private void viewRadioChange(ActionEvent event) {
        //If week radio button
        if(ViewPeriod.getSelectedToggle().equals(weekRadio)){
            System.out.println("Week is selected");
            startView = ZonedDateTime.now().withHour(0).withMinute(0);
            endView = startView.plusWeeks(1).withHour(23).withMinute(59);
        }
        //if month radio button
        if(ViewPeriod.getSelectedToggle().equals(monthRadio)){
            System.out.println("Month is selected");
            startView = ZonedDateTime.now().withHour(0).withMinute(0);
            endView = startView.plusMonths(1).withHour(23).withMinute(59);
        }
        updateView();
    }
    
    //get a new list to diplay base on the begin and end dates
    private void updateView() {
        String begin = startView.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String end = endView.format(DateTimeFormatter.ISO_LOCAL_DATE);        
        viewLabel.setText(begin + " :: " + end);
        
        begin = startView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ISO_LOCAL_DATE);
        end = endView.withZoneSameInstant(ZoneId.of("UTC+0")).format(DateTimeFormatter.ISO_LOCAL_DATE);
        
        try {
            mysql.database.updateAppointmentList(begin, end);
        }
        catch(SQLException e) {
            System.out.println("SQL Error!!! " + e);
        }
    }

    //when the view minus button is pressed subtract a week or month based on radio buttons
    @FXML
    private void ViewMinus(ActionEvent event) {
        if(ViewPeriod.getSelectedToggle().equals(weekRadio)){
            startView = startView.minusWeeks(1);
            endView = startView.plusWeeks(1);
        }
        
        if(ViewPeriod.getSelectedToggle().equals(monthRadio)){
            startView = startView.minusMonths(1);
            endView = startView.plusMonths(1);
        }
        updateView();
    }

    //when the view plus button is pressed add a week or month based on radio button
    @FXML
    private void ViewPlus(ActionEvent event) {
        if(ViewPeriod.getSelectedToggle().equals(weekRadio)){
            startView = startView.plusWeeks(1);
            endView = startView.plusWeeks(1);
        }
        
        if(ViewPeriod.getSelectedToggle().equals(monthRadio)){
            startView = startView.plusMonths(1);
            endView = startView.plusMonths(1);
        }
        updateView();
    }

    /**
     * used by the cusDeleteRun class to confirm deletion with a popup
     * @param selected the customer object to be deleted
     * @return true if popup was confirmed
     */
    private boolean ConfirmCustomerDelete(Customers selected) {
        int i = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + selected.getName() + "?", "Delete Customer?", JOptionPane.YES_NO_OPTION);
        
        return i == 0;
    }
    
    /**
     * used by the appDeleteRun class to confirm deletion with a popup
     * @param selected the customer object to be deleted
     * @return true if popup was confirmed
     */
    private boolean ConfirmAppointmentDelete(Appointments selected){
        int i = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + selected.getApointmentID() + ": " + selected.getType() + "?", "Delete Appointment?", JOptionPane.YES_NO_OPTION);
        
        return i == 0;
    }

    // run the first report action button
    @FXML
    private void RunReport1(ActionEvent event) {
        new Reports().Report(1);
    }

    // run the second report action button
    @FXML
    private void RunReport2(ActionEvent event) {
        new Reports().Report(2);
    }

    //runt he third report action button
    @FXML
    private void RunReport3(ActionEvent event) {
        new Reports().Report(3);
    }
}
