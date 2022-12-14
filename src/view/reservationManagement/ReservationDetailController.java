package view.reservationManagement;

import bean.Reservation;
import bean.Room;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import service.ReservationService;
import utility.dataHandler.DataValidation;
import utility.dataHandler.PrintReport;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationDetailController implements Initializable {

    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TableView<Reservation> reservationTable;

    @FXML
    private TableColumn<Reservation, String> rIdentificationColumn;

    @FXML
    private TableColumn<Reservation, String> rNameColumn;

    @FXML
    private TableColumn<Reservation, String> rNICColumn;

    @FXML
    private TableColumn<Reservation, String> rPhoneColumn;

    @FXML
    private TableColumn<Reservation, String> rCheckInOutColumn;

    @FXML
    private TableColumn<Reservation, String> rReservationStatusColumn;

    @FXML
    private TableColumn<Reservation, String> rActionColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private JFXButton updateButton;

    @FXML
    private TextField reservationNoTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nicTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField reservedDateTextField;

    @FXML
    private DatePicker checkInDatePicker;

    @FXML
    private DatePicker checkOutDatePicker;

    @FXML
    private ChoiceBox<String> statusChoiceBox;

    @FXML
    private AnchorPane reportAnchorPane;

    @FXML
    private DatePicker reportCheckInDatePicker;

    @FXML
    private DatePicker reportCheckOutDatePicker;

    @FXML
    private ChoiceBox<String> reportStatusChoiceBox;


    private static Reservation selectedReservation = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
        clearFields();
    }

    private void loadData(){
        ReservationService reservationService = new ReservationService();
        ObservableList<Reservation> reservationObservableList = reservationService.getAllReservationData();
        ArrayList<String> reservationStatusArrayList = new ArrayList<>();
        Collections.addAll(reservationStatusArrayList, "RESERVED", "CHECKED-IN", "CHECKED-OUT", "DISCARDED");
        statusChoiceBox.setItems(FXCollections.observableArrayList(reservationStatusArrayList));
        statusChoiceBox.setValue(reservationStatusArrayList.get(0));

        Collections.addAll(reservationStatusArrayList, "ALL");
        reportStatusChoiceBox.setItems(FXCollections.observableArrayList(reservationStatusArrayList));
        reportStatusChoiceBox.setValue(reservationStatusArrayList.get(0));

        rIdentificationColumn.setCellValueFactory(new PropertyValueFactory<>("resID"));
        rNameColumn.setCellValueFactory(new PropertyValueFactory<>("resCustomerName"));
        rNICColumn.setCellValueFactory(new PropertyValueFactory<>("resCustomerNIC"));
        rPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("resCustomerPhone"));
        rCheckInOutColumn.setCellValueFactory(new PropertyValueFactory<>("resCheckInOutDate"));
        rReservationStatusColumn.setCellValueFactory(new PropertyValueFactory<>("resStatus"));
        rActionColumn.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<Reservation, String>, TableCell<Reservation, String>> parentCellFactory
                =
                new Callback<TableColumn<Reservation, String>, TableCell<Reservation, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Reservation, String> param) {
                        final TableCell<Reservation, String> cell = new TableCell<Reservation, String>() {

                            final Button btn = new Button("VIEW");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        selectedReservation = getTableView().getItems().get(getIndex());
                                        viewReservationRooms();
                                        //prompt user for confirmation
//                                        Optional<ButtonType> buttonTypeOptional = AlertPopUp.removeConfirmation("Room");
//                                        if (buttonTypeOptional.get().equals(ButtonType.OK)) {
//                                            int index = reservationTable.getItems().indexOf(getTableView().getItems().get(getIndex()));
//
//                                            //adding item back to available Rooms

//
//                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        rActionColumn.setCellFactory(parentCellFactory);

        reservationTable.setItems(null);
        reservationTable.setItems(reservationObservableList);
        searchRoomTable(reservationObservableList);
    }

    private void searchRoomTable(ObservableList<Reservation> roomObservableList){


        ReservationService reservationService = new ReservationService();

        SortedList<Reservation> sortedData = reservationService.searchReservationTable(searchTextField, roomObservableList);

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(reservationTable.comparatorProperty());
        //adding sorted and filtered data to the table
        reservationTable.setItems(sortedData);
    }

    @FXML
    private void clearFields(ActionEvent event) {
        clearFields();
    }

    private void clearFields(){
        updateButton.setVisible(false);
        checkInDatePicker.setEditable(true);
        checkOutDatePicker.setEditable(true);
        reportAnchorPane.setVisible(true);

        reservationNoTextField.clear();
        nameTextField.clear();
        emailTextField.clear();
        nicTextField.clear();
        phoneTextField.clear();
        reservedDateTextField.clear();
        checkInDatePicker.setValue(null);
        checkOutDatePicker.setValue(null);
        statusChoiceBox.setValue(statusChoiceBox.getItems().get(0));
    }

    @FXML
    private void setSelectedReservationDataToFields(MouseEvent event) {
        try{
            updateButton.setVisible(true);
            checkInDatePicker.setEditable(false);
            checkInDatePicker.setOnMouseClicked(e -> {
                if(!checkInDatePicker.isEditable())
                    checkInDatePicker.hide();
            });

            checkOutDatePicker.setEditable(false);
            checkOutDatePicker.setOnMouseClicked(e -> {
                if(!checkOutDatePicker.isEditable())
                    checkOutDatePicker.hide();
            });

            selectedReservation = reservationTable.getSelectionModel().getSelectedItem();
            reservationNoTextField.setText(selectedReservation.getResID());
            nameTextField.setText(selectedReservation.getResCustomerName());
            emailTextField.setText(selectedReservation.getResCustomerEmail());
            nicTextField.setText(selectedReservation.getResCustomerNIC());
            phoneTextField.setText(selectedReservation.getResCustomerPhone());
            reservedDateTextField.setText(selectedReservation.getResReservedDate());
            checkInDatePicker.setValue(LocalDate.parse(selectedReservation.getResCheckInDate()));
            checkOutDatePicker.setValue(LocalDate.parse(selectedReservation.getResCheckOutDate()));
            statusChoiceBox.setValue(selectedReservation.getResStatus());
        }catch(NullPointerException ignored){}

    }

    @FXML
    private void updateReservation(ActionEvent event) {
        if(selectedReservation != null){
            ReservationService reservationService = new ReservationService();
            if(reservationService.updateReservationStatus(selectedReservation.getResID(), statusChoiceBox.getValue())){
                AlertPopUp.updateSuccesfully("Reservation Information");
                clearFields();
                loadData();
            }else {
                AlertPopUp.updateFailed("Reservation Information");
            }
        }else{
            AlertPopUp.selectRow("Please Select a Reservation to Update!");
        }

    }

    @FXML
    private void addNewReservation(ActionEvent event){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("manageReservationPopUp.fxml"));
        try{
            loader.load();
        }catch (IOException ex){
            Logger.getLogger(ManageReservationPopUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Parent p = loader.getRoot();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Room Manager 1.0");
        stage.setScene(new Scene(p));
        stage.setResizable(false);
        stage.sizeToScene();

        stage.showAndWait();
        loadData();
    }


    private void viewReservationRooms(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("viewReservationPopUp.fxml"));
        try{
            loader.load();
        }catch (IOException ex){
            Logger.getLogger(ViewReservationPopUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Parent p = loader.getRoot();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Room Manager 1.0");
        stage.setScene(new Scene(p));
        stage.setResizable(false);
        stage.sizeToScene();

        stage.showAndWait();

        loadData();
    }

    public static Reservation getSelectedReservation(){
        return selectedReservation;
    }

    @FXML
    private void printReservationList(ActionEvent event){
        if(DataValidation.DatePickerNotEmpty(reportCheckInDatePicker) && DataValidation.DatePickerNotEmpty(reportCheckOutDatePicker)){
            if(!reportCheckInDatePicker.getValue().isAfter(reportCheckOutDatePicker.getValue())){
                ReservationService reservationService = new ReservationService();
                ObservableList<Reservation> reservationObservableList = reservationService.getCustomReservationData(reportCheckInDatePicker.getValue(), reportCheckOutDatePicker.getValue(), reportStatusChoiceBox.getValue());
                if(!reservationObservableList.isEmpty()){
                    PrintReport printReport = new PrintReport();
                    printReport.printReservationList(reservationObservableList, reportCheckInDatePicker.getValue().toString() + " To " + reportCheckOutDatePicker.getValue().toString(), reportStatusChoiceBox.getValue());
                }else
                    AlertPopUp.customErrorPopup("No Records Found!", "No Reservation Records found for your Given Criteria, Please change your sorting criteria and try again!");
            }else
                AlertPopUp.customErrorPopup("Validation Error", "Please Enter a Check Out which available date after the Check In Date");

        }else{
            AlertPopUp.customErrorPopup("Fields Cannot be empty", "Check In and Check out Dates cannot be empty, Please select Check In and Checkout Dates");
        }

    }


}
