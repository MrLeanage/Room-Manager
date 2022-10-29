package view.reservationManagement;

import bean.Reservation;
import bean.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.ReservationService;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewReservationPopUpController implements Initializable {

    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TableView<Room> reservationRoomTable;

    @FXML
    private TableColumn<Room, String> cartCategoryNameColumn;

    @FXML
    private TableColumn<Room, String> roomNoColumn;

    @FXML
    private TableColumn<Room, String> statusColumn;

    @FXML
    private Label customerInfoLabel;

    @FXML
    private TextField searchTableTextField;

    private Reservation selectedReservation = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTableData();
        customerInfoLabel.setText("Reservation ID : " + selectedReservation.getResID() + " : " + selectedReservation.getResCustomerName());
    }

    @FXML
    private void closeStage(ActionEvent event){
        ((Stage) detailAnchorPane.getScene().getWindow()).close();
    }


    private void loadTableData(){

        selectedReservation = ReservationDetailController.getSelectedReservation();

        ReservationService reservationService = new ReservationService();
        ObservableList<Room> cartObservableList = FXCollections.observableArrayList(reservationService.getSpecificReservedRoomList(selectedReservation.getResID()));

        cartCategoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("rCnameWithRoomType"));
        roomNoColumn.setCellValueFactory(new PropertyValueFactory<>("rIdentification"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("rReservationStatus"));


        reservationRoomTable.setItems(null);
        reservationRoomTable.setItems(cartObservableList);
        searchRoomTable(cartObservableList);
    }

    private void searchRoomTable(ObservableList<Room> roomObservableList){


        ReservationService reservationService = new ReservationService();

        SortedList<Room> sortedData = reservationService.searchReservedRoomTable(searchTableTextField, roomObservableList);

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(reservationRoomTable.comparatorProperty());
        //adding sorted and filtered data to the table
        reservationRoomTable.setItems(sortedData);
    }



}
