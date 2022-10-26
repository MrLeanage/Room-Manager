package bean;

import javafx.beans.property.*;
import javafx.scene.image.ImageView;
import utility.dataHandler.UtilityMethod;

public class Category {
    private String cID = null;
    private ImageView cImage = null;
    private String cName = null;
    private String cBedArrangement = null;
    private String cRoomSize = null;
    private String cRoomType = null;
    private String cInfo = null;
    private Double cPrice = null;
    private String cAvailability = null;

    public Category() {
    }

    public Category(String cID, ImageView cImage, String cName, String cBedArrangement, String cRoomSize, String cRoomType, String cInfo, Double cPrice, String cAvailability) {
        this.cID = UtilityMethod.addPrefix("C", cID);
        this.cImage = cImage;
        this.cName = cName;
        this.cBedArrangement = cBedArrangement;
        this.cRoomSize = cRoomSize;
        this.cRoomType = cRoomType;
        this.cInfo = cInfo;
        this.cPrice = cPrice;
        this.cAvailability = cAvailability;
    }

    @Override
    public String toString() {
        return this.getcName();
    }

    public String getcID() {
        return cID;
    }

    public StringProperty cIDProperty(){
        return new SimpleStringProperty(cID);
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    public ImageView getcImage() {
        return cImage;
    }

    public ObjectProperty<ImageView> cImageProperty() {
        return new SimpleObjectProperty<>(cImage);
    }

    public void setcImage(ImageView cImage) {
        this.cImage = cImage;
    }

    public String getcName() {
        return cName;
    }

    public StringProperty cNameProperty(){
        return new SimpleStringProperty(cName);
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcBedArrangement() {
        return cBedArrangement;
    }

    public StringProperty cBedArrangementProperty(){
        return new SimpleStringProperty(cBedArrangement);
    }

    public void setcBedArrangement(String cBedArrangement) {
        this.cBedArrangement = cBedArrangement;
    }

    public String getcRoomSize() {
        return cRoomSize;
    }

    public StringProperty cRoomSizeProperty(){
        return new SimpleStringProperty(cRoomSize);
    }

    public void setcRoomSize(String cRoomSize) {
        this.cRoomSize = cRoomSize;
    }

    public String getcRoomType() {
        return cRoomType;
    }

    public StringProperty cRoomTypeProperty(){
        return new SimpleStringProperty(cRoomType);
    }

    public void setcRoomType(String cRoomType) {
        this.cRoomType = cRoomType;
    }

    public String getcInfo() {
        return cInfo;
    }

    public StringProperty cInfoProperty(){
        return new SimpleStringProperty(cInfo);
    }

    public void setcInfo(String cInfo) {
        this.cInfo = cInfo;
    }

    public Double getcPrice() {
        return cPrice;
    }

    public DoubleProperty cPriceProperty(){
        return new SimpleDoubleProperty(cPrice);
    }

    public void setcPrice(Double cPrice) {
        this.cPrice = cPrice;
    }

    public String getcAvailability() {
        return cAvailability;
    }

    public StringProperty cAvailabilityProperty(){
        return new SimpleStringProperty(cAvailability);
    }

    public void setcAvailability(String cAvailability) {
        this.cAvailability = cAvailability;
    }
}
