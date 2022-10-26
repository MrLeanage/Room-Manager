package view.reservationManagement;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ManageReservationPopUpController {

    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private DatePicker checkInDatePicker;

    @FXML
    private DatePicker checkOutDatePicker;

    @FXML
    private TableView<?> roomCategoryTable;

    @FXML
    private TableColumn<?, ?> rCNameColumn;

    @FXML
    private TableColumn<?, ?> rcAvailableQtyColumn;

    @FXML
    private TextField selectedCategoryTextField;

    @FXML
    private Spinner<?> requiredRoomsSpinner;

    @FXML
    private TextField availableRoomsTextField;

    @FXML
    private TableView<?> reservationCartTable;

    @FXML
    private TableColumn<?, ?> cartCategoryNameColumn;

    @FXML
    private TableColumn<?, ?> cartNoOfRoomsColumn;

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
    private JFXButton updateButton;

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
    private void addCategory(ActionEvent event) {

    }

    @FXML
    private void addReservation(ActionEvent event) {

    }

    @FXML
    private void clearCartFields(ActionEvent event) {

    }

    @FXML
    private void clearCategoryFields(ActionEvent event) {

    }

    @FXML
    private void updateReservation(ActionEvent event) {

    }

}
