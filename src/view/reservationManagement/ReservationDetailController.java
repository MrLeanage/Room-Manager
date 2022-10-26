package view.reservationManagement;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationDetailController implements Initializable {

    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TableView<?> reservationTable;

    @FXML
    private TableColumn<?, ?> rIdentificationColumn;

    @FXML
    private TableColumn<?, ?> rCategoryIDColumn;

    @FXML
    private TableColumn<?, ?> rNameColumn;

    @FXML
    private TableColumn<?, ?> rAvailabilityStatusColumn;

    @FXML
    private TableColumn<?, ?> rAvailabilityStatusColumn1;

    @FXML
    private TableColumn<?, ?> rReservationStatusColumn;

    @FXML
    private TableColumn<?, ?> rReservationStatusColumn1;

    @FXML
    private TextField searchTextField;

    @FXML
    private JFXButton updateButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void clearFields(ActionEvent event) {

    }

    @FXML
    private void setSelectedReservationDataToFields(MouseEvent event) {

    }

    @FXML
    private void updateReservation(ActionEvent event) {

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
    }


}
