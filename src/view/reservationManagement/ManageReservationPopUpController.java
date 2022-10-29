package view.reservationManagement;

import bean.Reservation;
import bean.Room;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import service.ReservationService;
import utility.dataHandler.DataValidation;
import utility.popUp.AlertPopUp;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class ManageReservationPopUpController implements Initializable {

    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private DatePicker checkInDatePicker;

    @FXML
    private DatePicker checkOutDatePicker;

    @FXML
    private TableView<Room> roomCategoryTable;

    @FXML
    private TableColumn<Room, String> rCNameColumn;

    @FXML
    private TableColumn<Room, String> rcRoomNoColumn;

    @FXML
    private TextField selectedCategoryTextField;

    @FXML
    private TextField roomIdentificationTextField;

    @FXML
    private TableView<Room> reservationCartTable;

    @FXML
    private TableColumn<Room, String> cartCategoryNameColumn;

    @FXML
    private TableColumn<Room, String> roomNoColumn;

    @FXML
    private TableColumn<Room, String> actionColumn;

    @FXML
    private TextField customerNameTextField;

    @FXML
    private TextField customerEmailTextField;

    @FXML
    private TextField customerNICTextField;

    @FXML
    private TextField customerPhoneTextField;

    @FXML
    private TextArea customerAddressTextField;

    @FXML
    private JFXButton addReservationButton;

    @FXML
    private Label addressValidationLabel;

    @FXML
    private Label nameValidationLabel;

    @FXML
    private Label emailValidationLabel;

    @FXML
    private Label nicValidationLabel;

    @FXML
    private Label phoneValidationLabel;

    @FXML
    private Label pageTitleLabel;

    @FXML
    private TextField searchAvailableRoomTextField;

    @FXML
    private Label datePickerValidationLabel;

    @FXML
    private TextField roomTypeTextField;

    private Room selectedRoom = null;

    private LinkedList<Room> selectedRoomsCartLinkedList = new LinkedList<>();

    private LinkedList<Room> availableRoomCartLinkedList = new LinkedList<>();

    private int selectedIndex = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearCart();
        clearCategory();
        clearSearch();
    }

    @FXML
    private void searchAvailableRooms(ActionEvent event){

        clearSearchValidation();
        if(DataValidation.DatePickerNotEmpty(checkInDatePicker) && DataValidation.DatePickerNotEmpty(checkOutDatePicker)){

            ReservationService reservationService = new ReservationService();
            availableRoomCartLinkedList.clear();
            availableRoomCartLinkedList.addAll(reservationService.getAvailableRoomsForCategories(checkInDatePicker.getValue().toString(), checkOutDatePicker.getValue().toString()));
            ObservableList<Room> availableRoomList = FXCollections.observableArrayList(availableRoomCartLinkedList);

            rCNameColumn.setCellValueFactory(new PropertyValueFactory<>("rCnameWithRoomType"));
            rcRoomNoColumn.setCellValueFactory(new PropertyValueFactory<>("rIdentification"));

            roomCategoryTable.setItems(null);
            roomCategoryTable.setItems(availableRoomList);

            searchAvailableRoomTable(availableRoomList);

        }else{
            datePickerValidationLabel.setText("Check In and Checkout Dates Required!");
        }
    }

    private void searchAvailableRoomTable(ObservableList<Room> roomObservableList){


        ReservationService reservationService = new ReservationService();

        SortedList<Room> sortedData = reservationService.searchAvailableRoomTable(searchAvailableRoomTextField, roomObservableList);

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(roomCategoryTable.comparatorProperty());
        //adding sorted and filtered data to the table
        roomCategoryTable.setItems(sortedData);
    }

    @FXML
    private void setSelectedRoomDateToFields(MouseEvent event){
        try{
            selectedIndex = roomCategoryTable.getSelectionModel().getSelectedIndex();
            selectedRoom = roomCategoryTable.getSelectionModel().getSelectedItem();
            selectedCategoryTextField.setText(selectedRoom.getrCategory().getcName());
            roomTypeTextField.setText(selectedRoom.getrCategory().getcRoomType());
            roomIdentificationTextField.setText(selectedRoom.getrIdentification());
        }catch(NullPointerException ignored){

        }
    }

    @FXML
    private void addCategory(ActionEvent event) {
        if(selectedRoom != null){
            selectedRoomsCartLinkedList.add(selectedRoom);
            availableRoomCartLinkedList.remove(selectedIndex);
            clearCategoryFields();
            refreshCategoryTable();
            refreshCartTable();
        }
    }

    @FXML
    private void addReservation(ActionEvent event) {
        clearCartValidation();
        if(reservationValidation()){
            Reservation reservation = new Reservation();
            reservation.setResCustomerName(customerNameTextField.getText());
            reservation.setResCustomerEmail(customerEmailTextField.getText());
            reservation.setResCustomerNIC(customerNICTextField.getText());
            reservation.setResCustomerPhone(customerPhoneTextField.getText());
            reservation.setResCustomerAddress(customerAddressTextField.getText());

            reservation.setResReservedDate(String.valueOf(LocalDate.now()));
            reservation.setResCheckInDate(checkInDatePicker.getValue().toString());
            reservation.setResCheckOutDate(checkOutDatePicker.getValue().toString());
            reservation.setResStatus("RESERVED");

            reservation.setReservedRoomList(new ArrayList<>(selectedRoomsCartLinkedList));

            ReservationService reservationService = new ReservationService();
            if (reservationService.addReservation(reservation)) {
                AlertPopUp.insertSuccesfully("Reservation");
                clearCart();
                clearCategory();
                clearSearch();
            } else
                AlertPopUp.insertionFailed("Reservation");

        }else
            reservationValidationMessage();
    }

    @FXML
    private void clearCartFields(ActionEvent event) {
        clearCart();
    }

    @FXML
    private void clearCategoryFields(ActionEvent event) {
        clearCategory();
    }

    @FXML
    private void closeStage(ActionEvent event){
        ((Stage) detailAnchorPane.getScene().getWindow()).close();
    }

    @FXML
    private void clearSearch(ActionEvent event){
        clearSearch();
    }

    private void clearSearch(){
        checkInDatePicker.setDayCellFactory(DataValidation.hideOldDates());
        checkOutDatePicker.setDayCellFactory(DataValidation.hideOldDates());
        checkInDatePicker.getEditor().clear();
        checkOutDatePicker.getEditor().clear();
        checkInDatePicker.setValue(null);
        checkOutDatePicker.setValue(null);
        clearSearchValidation();
        clearCategory();
        clearCart();
    }

    private void clearCategory(){

        roomCategoryTable.setItems(null);
        availableRoomCartLinkedList.clear();
        roomCategoryTable.refresh();
        clearCategoryFields();
    }

    private void clearCategoryFields() {
        selectedCategoryTextField.clear();
        roomTypeTextField.clear();
        roomIdentificationTextField.clear();
    }


    private void clearCart(){
        reservationCartTable.setItems(null);
        selectedRoomsCartLinkedList.clear();
        reservationCartTable.refresh();
        customerNameTextField.clear();
        customerEmailTextField.clear();
        customerNICTextField.clear();
        customerPhoneTextField.clear();
        customerAddressTextField.clear();
        clearCartValidation();
    }

    private void clearSearchValidation(){
        datePickerValidationLabel.setText("");
    }
    private void clearCartValidation(){
        nameValidationLabel.setText("");
        emailValidationLabel.setText("");
        nicValidationLabel.setText("");
        phoneValidationLabel.setText("");
        addressValidationLabel.setText("");
    }

    private void refreshCategoryTable(){
        ObservableList<Room> availableRoomList = FXCollections.observableArrayList(availableRoomCartLinkedList);

        rCNameColumn.setCellValueFactory(new PropertyValueFactory<>("rCnameWithRoomType"));
        rcRoomNoColumn.setCellValueFactory(new PropertyValueFactory<>("rIdentification"));

        roomCategoryTable.setItems(null);
        roomCategoryTable.setItems(availableRoomList);

        searchAvailableRoomTable(availableRoomList);
    }

    private void refreshCartTable(){
        ObservableList<Room> cartObservableList = FXCollections.observableArrayList(selectedRoomsCartLinkedList);

        cartCategoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("rCnameWithRoomType"));
        roomNoColumn.setCellValueFactory(new PropertyValueFactory<>("rIdentification"));
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

        Callback<TableColumn<Room, String>, TableCell<Room, String>> parentCellFactory
                =
                new Callback<TableColumn<Room, String>, TableCell<Room, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Room, String> param) {
                        final TableCell<Room, String> cell = new TableCell<Room, String>() {

                            final Button btn = new Button("Remove");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        //prompt user for confirmation
                                        Optional<ButtonType> buttonTypeOptional = AlertPopUp.removeConfirmation("Room");
                                        if (buttonTypeOptional.get().equals(ButtonType.OK)) {
                                            int index = reservationCartTable.getItems().indexOf(getTableView().getItems().get(getIndex()));
                                            //Removing Specific room from Cart List
                                            selectedRoomsCartLinkedList.remove(index);
                                            //adding item back to available Rooms
                                            Room selectedRoom = getTableView().getItems().get(getIndex());
                                            availableRoomCartLinkedList.add(selectedRoom);

                                            //Refreshing Both tables
                                            refreshCartTable();
                                            refreshCategoryTable();
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        actionColumn.setCellFactory(parentCellFactory);


        reservationCartTable.setItems(null);
        reservationCartTable.setItems(cartObservableList);

    }

    private boolean reservationValidation() {


        return DataValidation.TextFieldNotEmpty(customerNameTextField.getText())
                && DataValidation.TextFieldNotEmpty(customerEmailTextField.getText())
                && DataValidation.TextFieldNotEmpty(customerNICTextField.getText())
                && DataValidation.TextFieldNotEmpty(customerPhoneTextField.getText())
                && DataValidation.TextFieldNotEmpty(customerAddressTextField.getText())

                && DataValidation.isValidMaximumLength(customerNameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(customerEmailTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(customerNICTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(customerAddressTextField.getText(), 400)

                && DataValidation.isValidEmail(customerEmailTextField.getText())
                && DataValidation.isValidNIC(customerNICTextField)
                && DataValidation.isValidPhoneNo(customerPhoneTextField.getText());

    }

    private void reservationValidationMessage() {
        DataValidation.TextFieldNotEmpty(customerNameTextField.getText(), nameValidationLabel, "Customer Name is Required!..");
        DataValidation.TextFieldNotEmpty(customerEmailTextField.getText(), emailValidationLabel, "Customer Email is Required!..");
        DataValidation.TextFieldNotEmpty(customerNICTextField.getText(), nicValidationLabel, "Customer NIC is Required!..");
        DataValidation.TextFieldNotEmpty(customerPhoneTextField.getText(), phoneValidationLabel, "Customer Contact No is Required!..");
        DataValidation.TextFieldNotEmpty(customerAddressTextField.getText(), addressValidationLabel, "Customer Address is Required!..");

        DataValidation.isValidMaximumLength(customerNameTextField.getText(), 45, nameValidationLabel, "Field Limit 45 Exceeded!");
        DataValidation.isValidMaximumLength(customerEmailTextField.getText(), 45, emailValidationLabel, "Field Limit 45 Exceeded!");
        DataValidation.isValidMaximumLength(customerNICTextField.getText(), 45, nicValidationLabel, "Field Limit 45 Exceeded!");
        DataValidation.isValidMaximumLength(customerAddressTextField.getText(), 400, addressValidationLabel, "Field Limit 400 Exceeded!");

        DataValidation.isValidEmail(customerEmailTextField.getText(), emailValidationLabel, "Enter a valid Email address!..");
        DataValidation.isValidNIC(customerNICTextField, nicValidationLabel, "Enter a valid NIC!..");
        DataValidation.isValidPhoneNo(customerPhoneTextField.getText(), phoneValidationLabel, "Enter Valid Phone Number!..");
    }



}
