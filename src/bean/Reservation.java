package bean;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class Reservation {
    private String resID = null;
    private String resCustomerName = null;
    private String resCustomerEmail = null;
    private String resCustomerNIC = null;
    private String resCustomerPhone = null;
    private String resCustomerAddress = null;
    private String resReservedDate = null;
    private String resCheckInDate = null;
    private String resCheckOutDate = null;
    private String resStatus = null;
    private ArrayList<Room> reservedRoomList = null;

    public Reservation() {
    }

    public Reservation(String resID, String resCustomerName, String resCustomerEmail, String resCustomerNIC, String resCustomerAddress,  String resCustomerPhone, String resReservedDate, String resCheckInDate, String resCheckOutDate, String resStatus, ArrayList<Room> reservedRoomList) {
        this.resID = resID;
        this.resCustomerName = resCustomerName;
        this.resCustomerEmail = resCustomerEmail;
        this.resCustomerNIC = resCustomerNIC;
        this.resCustomerPhone = resCustomerPhone;
        this.resCustomerAddress = resCustomerAddress;
        this.resReservedDate = resReservedDate;
        this.resCheckInDate = resCheckInDate;
        this.resCheckOutDate = resCheckOutDate;
        this.resStatus = resStatus;
        this.reservedRoomList = reservedRoomList;
    }

    //Property Methods
    public StringProperty resIDProperty(){
        return new SimpleStringProperty(resID);
    }
    public StringProperty resCustomerNameProperty(){
        return new SimpleStringProperty(resCustomerName);
    }
    public StringProperty resCustomerNICProperty(){
        return new SimpleStringProperty(resCustomerNIC);
    }
    public StringProperty resCustomerPhoneProperty(){
        return new SimpleStringProperty(resCustomerPhone);
    }
    public StringProperty resCheckInOutDateProperty(){
        return new SimpleStringProperty(resCheckInDate + " - " + resCheckOutDate);
    }
    public StringProperty resStatusProperty(){
        return new SimpleStringProperty(resStatus);
    }


    public String getResID() {
        return resID;
    }

    public void setResID(String resID) {
        this.resID = resID;
    }

    public String getResCustomerName() {
        return resCustomerName;
    }

    public void setResCustomerName(String resCustomerName) {
        this.resCustomerName = resCustomerName;
    }

    public String getResCustomerEmail() {
        return resCustomerEmail;
    }

    public void setResCustomerEmail(String resCustomerEmail) {
        this.resCustomerEmail = resCustomerEmail;
    }

    public String getResCustomerNIC() {
        return resCustomerNIC;
    }

    public void setResCustomerNIC(String resCustomerNIC) {
        this.resCustomerNIC = resCustomerNIC;
    }

    public String getResCustomerPhone() {
        return resCustomerPhone;
    }

    public void setResCustomerPhone(String resCustomerPhone) {
        this.resCustomerPhone = resCustomerPhone;
    }

    public String getResCustomerAddress() {
        return resCustomerAddress;
    }

    public void setResCustomerAddress(String resCustomerAddress) {
        this.resCustomerAddress = resCustomerAddress;
    }

    public String getResCheckInDate() {
        return resCheckInDate;
    }

    public void setResCheckInDate(String resCheckInDate) {
        this.resCheckInDate = resCheckInDate;
    }

    public String getResCheckOutDate() {
        return resCheckOutDate;
    }

    public void setResCheckOutDate(String resCheckOutDate) {
        this.resCheckOutDate = resCheckOutDate;
    }

    public ArrayList<Room> getReservedRoomList() {
        return reservedRoomList;
    }

    public void setReservedRoomList(ArrayList<Room> reservedRoomList) {
        this.reservedRoomList = reservedRoomList;
    }

    public String getResReservedDate() {
        return resReservedDate;
    }

    public void setResReservedDate(String resReservedDate) {
        this.resReservedDate = resReservedDate;
    }

    public String getResStatus() {
        return resStatus;
    }

    public void setResStatus(String resStatus) {
        this.resStatus = resStatus;
    }


}
