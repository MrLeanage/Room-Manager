package utility.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import utility.dataHandler.UserAuthentication;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.util.Optional;

public class Navigation {

    public void logoutAccount(AnchorPane baseAnchorPane) {
        try {
            Optional<ButtonType> action = AlertPopUp.logoutConfirmation();
            if (action.get() == ButtonType.OK) {
                UserAuthentication.setAuthenticatedUser(null);
                AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/appHome/appLogin.fxml"));
                baseAnchorPane.getChildren().setAll(pane);
            }

        } catch (IOException ex) {
            AlertPopUp.generalError(ex);
        }
    }

    public void loadAppStage(AnchorPane baseAnchorPane) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/appHome/appStage.fxml"));
            baseAnchorPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            AlertPopUp.generalError(ex);
            ex.printStackTrace();
        }
    }

    public void loadHomeDetail(AnchorPane detailAnchorPane) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/appHome/homeDetail.fxml"));
            detailAnchorPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            AlertPopUp.generalError(ex);
            ex.printStackTrace();
        }
    }

    public void loadReservationDetail(AnchorPane baseAnchorPane) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/reservationManagement/reservationDetail.fxml"));
            baseAnchorPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            AlertPopUp.generalError(ex);
        }
    }

    public void loadRoomBookDetail(AnchorPane baseAnchorPane) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/roomBookManagement/roomBookDetail.fxml"));
            baseAnchorPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            AlertPopUp.generalError(ex);
        }
    }

    public void loadCategoryDetail(AnchorPane baseAnchorPane) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/categoryManagement/categoryDetail.fxml"));
            baseAnchorPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            ex.printStackTrace();
            AlertPopUp.generalError(ex);

        }
    }

    public void loadRoomDetail(AnchorPane baseAnchorPane) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/roomManagement/roomDetail.fxml"));
            baseAnchorPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            AlertPopUp.generalError(ex);
        }
    }

    public void loadUserDetail(AnchorPane baseAnchorPane) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/view/userManagement/userDetail.fxml"));
            baseAnchorPane.getChildren().setAll(pane);
        } catch (IOException ex) {
            AlertPopUp.generalError(ex);
        }
    }
}
