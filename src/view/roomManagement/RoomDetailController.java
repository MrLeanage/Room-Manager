package view.roomManagement;

import bean.Category;
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
import service.CategoryService;
import service.RoomService;
import utility.dataHandler.DataValidation;
import utility.dataHandler.PrintReport;
import utility.dataHandler.UtilityMethod;
import utility.popUp.AlertPopUp;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

public class RoomDetailController implements Initializable {
    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TableView<Room> roomTable;

    @FXML
    private TableColumn<Room, String> rIdentificationColumn;

    @FXML
    private TableColumn<Room, String> rCategoryIDColumn;

    @FXML
    private TableColumn<Room, String> rNameColumn;

    @FXML
    private TableColumn<Room, String> rAvailabilityStatusColumn;

    @FXML
    private TableColumn<Room, String> rReservationStatusColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField rIdentificationTextField;

    @FXML
    private ComboBox<Category> rCategoryComboBox;

    @FXML
    private ChoiceBox<String> rAvailabilityChoiceBox;

    @FXML
    private TextField rReservationStatusTextField;

    @FXML
    private Label identificationNoValidationLabel;

    @FXML
    private JFXButton addButton;

    @FXML
    private JFXButton updateButton;

    @FXML
    private Label reservationStatusLabel;

    @FXML
    private ComboBox<Category> printCategoryComboBox;

    @FXML
    private ChoiceBox<String> printAvailabilityChoiceBox;

    @FXML
    private ChoiceBox<String> printReservationStatusChoiceBox;

    private Room selectedRoom = null;

    private static final String defaultRoomStatus = "NOT RESERVED";

    private static final String reservedRoomStatus = "RESERVED";

    private boolean inputRoomIdentificationExistence = false;

    private boolean onUpdate = false;

    private ArrayList<String> availabilityChoiceBoxList = new ArrayList<>();
    private ArrayList<Category> printCategoryList = new ArrayList<>();
    private ObservableList<String> reservationStatusObservableList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rReservationStatusTextField.setVisible(false);
        reservationStatusLabel.setVisible(false);

        Collections.addAll(availabilityChoiceBoxList, "Available", "Not Available");
        rAvailabilityChoiceBox.setValue("Available");
        rAvailabilityChoiceBox.setItems(FXCollections.observableArrayList(availabilityChoiceBoxList));

        Collections.addAll(availabilityChoiceBoxList, "ALL ROOMS");
        printAvailabilityChoiceBox.setItems(FXCollections.observableArrayList(availabilityChoiceBoxList));
        printAvailabilityChoiceBox.setValue("Available");

        reservationStatusObservableList.addAll(defaultRoomStatus, reservedRoomStatus, "ALL");
        printReservationStatusChoiceBox.setItems(reservationStatusObservableList);
        printReservationStatusChoiceBox.setValue(reservationStatusObservableList.get(0));


        Category category = new Category();
        category.setcName("ALL CATEGORIES");
        category.setcID("C000");


        CategoryService categoryService = new CategoryService();
        printCategoryList.addAll(categoryService.loadAllCategoryData());

        rCategoryComboBox.setItems(FXCollections.observableArrayList(printCategoryList));
        rCategoryComboBox.setValue(printCategoryList.get(0));


        Collections.addAll(printCategoryList, category);
        printCategoryComboBox.setValue(printCategoryList.get(0));
        printCategoryComboBox.setItems(FXCollections.observableArrayList(printCategoryList));

        loadData();
    }

    private void loadData() {
        RoomService roomService = new RoomService();
        ObservableList<Room> roomObservableList = roomService.loadAllRoomData();

        rIdentificationColumn.setCellValueFactory(new PropertyValueFactory<>("rIdentification"));
        rCategoryIDColumn.setCellValueFactory(new PropertyValueFactory<>("rCID"));
        rNameColumn.setCellValueFactory(new PropertyValueFactory<>("rCname"));
        rAvailabilityStatusColumn.setCellValueFactory(new PropertyValueFactory<>("rAvailabilityStatus"));
        rReservationStatusColumn.setCellValueFactory(new PropertyValueFactory<>("rReservationStatus"));

        roomTable.setItems(null);
        roomTable.setItems(roomObservableList);
        searchTable();
    }

    public void searchTable() {
        RoomService roomService = new RoomService();

        SortedList<Room> sortedData = roomService.searchTable(searchTextField);

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(roomTable.comparatorProperty());
        //adding sorted and filtered data to the table
        roomTable.setItems(sortedData);
    }

    @FXML
    void addRoom(ActionEvent event) {
        clearLabels();
        if (roomValidation() && !inputRoomIdentificationExistence) {
            Room room = new Room();
            RoomService roomService = new RoomService();


            room.setrCategory(rCategoryComboBox.getValue());
            room.setrIdentification(rIdentificationTextField.getText());
            room.setrAvailabilityStatus(rAvailabilityChoiceBox.getValue());
            room.setrReservationStatus(defaultRoomStatus);


            if (roomService.insertRoomData(room)) {
                AlertPopUp.insertSuccesfully("Room");
                loadData();
                searchTable();
                clearFields(event);
            } else
                AlertPopUp.insertionFailed("Room");

        } else
            roomValidationMessage();
    }

    @FXML
    void updateRoom(ActionEvent event) {
        clearLabels();
        //prompt pop select a record first if a record from table is not selected
        if(selectedRoom != null){
            //prompt a popup if the room is reserved, record cannot update
            if(!selectedRoom.getrReservationStatus().equals(reservedRoomStatus) ){
                //updating data if room validation is success
                if (roomValidation()) {
                    Room room = new Room();
                    RoomService roomService = new RoomService();

                    room.setrCategory(rCategoryComboBox.getValue());
                    room.setrIdentification(rIdentificationTextField.getText());
                    room.setrAvailabilityStatus(rAvailabilityChoiceBox.getValue());
                    room.setrReservationStatus(defaultRoomStatus);
                    room.setrID(selectedRoom.getrID());

                    if (roomService.updateRoomData(room)) {
                        AlertPopUp.updateSuccesfully("Room");
                        loadData();
                        searchTable();
                        clearFields(event);
                    } else
                        AlertPopUp.updateFailed("Room");

                } else
                    roomValidationMessage();
            }else {
                AlertPopUp.customErrorPopup("Cannot Update Record", "You cannot update information of a room which is reserved already");
            }

        }else{
            AlertPopUp.selectRow("Room to Update");
        }

    }

    @FXML
    private void deleteRoom(ActionEvent event) {
        if (selectedRoom != null) {
            //prompt a popup if the room is reserved, record cannot delete
            if(!selectedRoom.getrReservationStatus().equals(reservedRoomStatus) ){
                RoomService roomService = new RoomService();
                Optional<ButtonType> action = AlertPopUp.deleteConfirmation("Room");
                if (action.get() == ButtonType.OK) {
                    if (roomService.deleteRoomData((selectedRoom.getrID()))) {
                        AlertPopUp.deleteSuccesfull("Room");
                        loadData();
                        searchTable();
                        clearFields(event);
                    } else
                        AlertPopUp.deleteFailed("Room");
                } else
                    loadData();
            }else {
                AlertPopUp.customErrorPopup("Cannot Delete Record", "You cannot Delete information of a room which is reserved already");
            }

        } else {
            AlertPopUp.selectRowToDelete("Room");
        }
    }

    @FXML
    void setSelectedRoomDataToFields(MouseEvent event) {
        try {
            clearLabels();
            rReservationStatusTextField.setVisible(true);
            addButton.setVisible(false);
            updateButton.setVisible(true);
            reservationStatusLabel.setVisible(true);
            onUpdate = true;

            selectedRoom = roomTable.getSelectionModel().getSelectedItem();
            rCategoryComboBox.setValue(selectedRoom.getrCategory());
            rIdentificationTextField.setText(selectedRoom.getrIdentification());
            rAvailabilityChoiceBox.setValue(selectedRoom.getrAvailabilityStatus());
            rReservationStatusTextField.setText(selectedRoom.getrReservationStatus());
        } catch (NullPointerException exception) {

        }
    }

    @FXML
    void clearFields(ActionEvent event) {

        rReservationStatusTextField.setVisible(false);
        reservationStatusLabel.setVisible(false);
        addButton.setVisible(true);
        updateButton.setVisible(false);
        onUpdate = false;

        rCategoryComboBox.setValue(printCategoryList.get(0));
        rAvailabilityChoiceBox.setValue(availabilityChoiceBoxList.get(0));
        rIdentificationTextField.clear();
        rReservationStatusTextField.clear();

        clearLabels();
        selectedRoom = null;
    }

    private void clearLabels() {
        identificationNoValidationLabel.setText("");
    }

    private boolean roomValidation() {


        return DataValidation.TextFieldNotEmpty(rIdentificationTextField.getText())
                && DataValidation.isValidMaximumLength(rIdentificationTextField.getText(), 45);
    }

    private void roomValidationMessage() {
        DataValidation.TextFieldNotEmpty(rIdentificationTextField.getText(), identificationNoValidationLabel, "Room Identification Number is Required!");
        DataValidation.isValidMaximumLength(rIdentificationTextField.getText(), 45, identificationNoValidationLabel, "Field Limit 45 Exceeded!");
    }

    @FXML
    private void checkUserRoomNumberAvailability() {

        ObservableList<Room> modelList = roomTable.getItems();
        ArrayList<String> roomIdentificationList = new ArrayList<>();
        for (Room room : modelList) {
            roomIdentificationList.add(room.getrIdentification().toLowerCase());
        }
        inputRoomIdentificationExistence = true;
        if (rIdentificationTextField.getText().isEmpty()) {
            identificationNoValidationLabel.setStyle("-fx-text-fill: #ff0000 ");
            identificationNoValidationLabel.setText("Room Identification Cannot be empty");
        } else if (UtilityMethod.checkDataAvailability(roomIdentificationList, rIdentificationTextField.getText().toLowerCase())) {
            if(onUpdate){
                if(!selectedRoom.getrIdentification().toLowerCase().equals(rIdentificationTextField.getText().toLowerCase())){
                    identificationNoValidationLabel.setStyle("-fx-text-fill: #ff0000 ");
                    identificationNoValidationLabel.setText("Room Identification Already Exist!!");
                }
            }else{
                identificationNoValidationLabel.setStyle("-fx-text-fill: #ff0000 ");
                identificationNoValidationLabel.setText("Room Identification Already Exist!!");
            }

        } else {
            identificationNoValidationLabel.setStyle("-fx-text-fill: #00B605 ");
            identificationNoValidationLabel.setText("Room Identification Available to use");
            inputRoomIdentificationExistence = false;
        }
    }

    @FXML
    private void printRoomReport(ActionEvent actionEvent){
        RoomService roomService = new RoomService();
        System.out.println(printCategoryComboBox.getValue().getcID());
        ObservableList<Room> roomObservableList = roomService.loadAllCustomRoomData(UtilityMethod.seperateID(printCategoryComboBox.getValue().getcID()), printAvailabilityChoiceBox.getValue(), printReservationStatusChoiceBox.getValue());

        if(!roomObservableList.isEmpty()){
            PrintReport printReport = new PrintReport();
            printReport.printRoomList(roomObservableList, printCategoryComboBox.getValue().getcName(), printAvailabilityChoiceBox.getValue(), printReservationStatusChoiceBox.getValue());
        }else
            AlertPopUp.customErrorPopup("No Records Found", "No Records found for your given sorting Criteria, Please Change your sorting criteria and try again!!");



    }
}
