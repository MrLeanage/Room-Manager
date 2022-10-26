package bean;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utility.dataHandler.UtilityMethod;

public class Room {
    private String rID = null;
    private Category rCategory = null;
    private String rIdentification = null;
    private String rAvailabilityStatus = null;
    private String rReservationStatus = null;

    public Room(String rID, Category rCategory, String rIdentification, String rAvailabilityStatus, String rReservationStatus) {
        this.rID = UtilityMethod.addPrefix("R-", rID);
        this.rCategory = rCategory;
        this.rIdentification = rIdentification;
        this.rAvailabilityStatus = rAvailabilityStatus;
        this.rReservationStatus = rReservationStatus;
    }

    public Room() {

    }

    public String getrID() {
        return rID;
    }

    public StringProperty rIDProperty(){
        return new SimpleStringProperty(rID);
    }

    public void setrID(String rID) {
        this.rID = rID;
    }

    public Category getrCategory() {
        return rCategory;
    }

    public void setrCategory(Category rCategory) {
        this.rCategory = rCategory;
    }

    public String getrCID() {
        return rCategory.getcID();
    }

    public StringProperty rCIDProperty(){
        return rCategory.cIDProperty();
    }

    public StringProperty rCnameProperty(){
        return rCategory.cNameProperty();
    }

    public String getrIdentification() {
        return rIdentification;
    }

    public StringProperty rIdentificationProperty(){
        return new SimpleStringProperty(rIdentification);
    }

    public void setrIdentification(String rIdentification) {
        this.rIdentification = rIdentification;
    }

    public String getrAvailabilityStatus() {
        return rAvailabilityStatus;
    }

    public StringProperty rAvailabilityStatusProperty(){
        return new SimpleStringProperty(rAvailabilityStatus);
    }

    public void setrAvailabilityStatus(String rAvailabilityStatus) {
        this.rAvailabilityStatus = rAvailabilityStatus;
    }

    public String getrReservationStatus() {
        return rReservationStatus;
    }

    public StringProperty rReservationStatusProperty(){
        return new SimpleStringProperty(rReservationStatus);
    }

    public void setrReservationStatus(String rReservationStatus) {
        this.rReservationStatus = rReservationStatus;
    }
}
