package view.categoryManagement;

import bean.Category;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import service.CategoryService;
import utility.dataHandler.DataValidation;
import utility.popUp.AlertPopUp;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.ResourceBundle;

public class CategoryDetailController implements Initializable {
    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TableView<Category> categoryTable;

    @FXML
    private TableColumn<Category, String> IDColumn;

    @FXML
    private TableColumn<Category, ImageView> imageColumn;

    @FXML
    private TableColumn<Category, String> nameColumn;

    @FXML
    private TableColumn<Category, String> bedArrangementColumn;

    @FXML
    private TableColumn<Category, String> typeColumn;

    @FXML
    private TableColumn<Category, Double> priceColumn;

    @FXML
    private TableColumn<Category, String> availabilityColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private Label nameLabel;

    @FXML
    private Label infoLabel;

    @FXML
    private Label categoryImageLabel;

    @FXML
    private TextField priceTextField;

    @FXML
    private Label priceLabel;

    @FXML
    private ImageView categoryImageView;

    @FXML
    private TextArea infoTextArea;

    @FXML
    private ChoiceBox<String> availabilityChoiceBox;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private TextField bedArrangementTextField;

    @FXML
    private Label bedArrangementLabel;

    @FXML
    private TextField roomSizeTextField;

    @FXML
    private Label roomSizeLabel;

    @FXML
    private ChoiceBox<String> roomTypeChoiceBox;
    private static File staticFile;

    private static Category selectedCategory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> availabilityChoiceBoxList = FXCollections.observableArrayList("Available", "Not Available");
        availabilityChoiceBox.setValue("Available");
        availabilityChoiceBox.setItems(availabilityChoiceBoxList);

        ObservableList<String> typeChoiceBoxList = FXCollections.observableArrayList("A/C SMOKE", "A/C NON SMOKE", "NON A/C SMOKE", "NON A/C NON SMOKE");
        roomTypeChoiceBox.setValue(typeChoiceBoxList.get(1));
        roomTypeChoiceBox.setItems(typeChoiceBoxList);

        loadData();

    }

    private void loadData() {
        CategoryService categoryService = new CategoryService();
        ObservableList<Category> menuObservableList = categoryService.loadAllCategoryData();

        IDColumn.setCellValueFactory(new PropertyValueFactory<>("cID"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("cImage"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("cName"));
        bedArrangementColumn.setCellValueFactory(new PropertyValueFactory<>("cBedArrangement"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("cRoomType"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("cPrice"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("cAvailability"));

        priceColumn.setCellFactory(tc -> new TableCell<Category, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty) ;
                if (empty) {
                    setText(null);
                } else {
                    setText("Rs "+ new DecimalFormat("#####.00").format(value));
                    setAlignment(Pos.valueOf("TOP_RIGHT"));
                }
            }
        });

        imageColumn.setCellFactory(param -> {
            //Set up the ImageView
            final ImageView imageview = new ImageView();
            imageview.setFitHeight(80);
            imageview.setFitWidth(80);

            //Set up the Table
            TableCell<Category, ImageView> cell = new TableCell<Category, ImageView>() {
                public void updateItem(ImageView item, boolean empty) {
                    if(item != null){
                        if(!empty){
                            imageview.setImage(item.getImage());
                        }
                    }
                }
            };
            // Attach the imageview to the cell
            cell.setGraphic(null);
            cell.setGraphic(imageview);
            return cell;
        });
        categoryTable.setItems(null);
        categoryTable.setItems(menuObservableList);
        searchTable();
    }

    public void searchTable() {
        CategoryService categoryService = new CategoryService();

        SortedList<Category> sortedData = categoryService.searchTable(searchTextField);

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(categoryTable.comparatorProperty());
        //adding sorted and filtered data to the table
        categoryTable.setItems(sortedData);
    }

    @FXML
    void addMenu(ActionEvent event) {

        clearLabels();
        if (categoryValidation()) {
            Category category = new Category();
            CategoryService categoryService = new CategoryService();


            category.setcName(nameTextField.getText());
            category.setcBedArrangement(bedArrangementTextField.getText());
            category.setcRoomSize(roomSizeTextField.getText());
            category.setcRoomType(roomTypeChoiceBox.getValue());
            category.setcInfo(infoTextArea.getText());
            category.setcPrice(Double.valueOf(priceTextField.getText()));
            category.setcAvailability(availabilityChoiceBox.getValue());
            category.setcImage(categoryImageView);


            if (categoryService.insertCategoryData(category)) {
                AlertPopUp.insertSuccesfully("Category");
                loadData();
                searchTable();
                clearFields(event);
            } else
                AlertPopUp.insertionFailed("Category");

        } else
            menuValidationMessage();
    }

    @FXML
    void updateMenu(ActionEvent event) {
        clearLabels();
        if (categoryValidation()) {
            Category category = new Category();
            CategoryService categoryService = new CategoryService();

            category.setcID(selectedCategory.getcID());
            category.setcName(nameTextField.getText());
            category.setcBedArrangement(bedArrangementTextField.getText());
            category.setcRoomSize(roomSizeTextField.getText());
            category.setcRoomType(roomTypeChoiceBox.getValue());
            category.setcInfo(infoTextArea.getText());
            category.setcPrice(Double.valueOf(priceTextField.getText()));
            category.setcAvailability(availabilityChoiceBox.getValue());
            category.setcImage(categoryImageView);

            if (categoryService.updateCategoryData(category)) {
                AlertPopUp.updateSuccesfully("Category");
                loadData();
                searchTable();
                clearFields(event);
            } else
                AlertPopUp.updateFailed("Category");
        } else
            menuValidationMessage();
    }

    @FXML
    private void deleteMenu(ActionEvent event) {
        if (selectedCategory != null) {
            CategoryService categoryService = new CategoryService();
            Optional<ButtonType> action = AlertPopUp.deleteConfirmation("Category");
            if (action.get() == ButtonType.OK) {
                if (categoryService.deleteCategoryData((selectedCategory.getcID()))) {
                    AlertPopUp.deleteSuccesfull("Category");
                    loadData();
                    searchTable();
                    clearFields(event);
                } else
                    AlertPopUp.deleteFailed("Category");
            } else
                loadData();
        } else {
            AlertPopUp.selectRowToDelete("Category");
        }
    }

    @FXML
    void setSelectedCategoryDataToFields(MouseEvent event) {
        try {
            clearLabels();
            addButton.setVisible(false);
            updateButton.setVisible(true);

            selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
            nameTextField.setText(selectedCategory.getcName());
            bedArrangementTextField.setText(selectedCategory.getcBedArrangement());
            roomSizeTextField.setText(selectedCategory.getcRoomSize());
            roomTypeChoiceBox.setValue(selectedCategory.getcRoomType());
            infoTextArea.setText(selectedCategory.getcInfo());
            priceTextField.setText(selectedCategory.getcPrice().toString());
            availabilityChoiceBox.setValue(selectedCategory.getcAvailability());
            categoryImageView.setImage(selectedCategory.getcImage().getImage());
        } catch (NullPointerException exception) {

        }
    }

    @FXML
    void clearFields(ActionEvent event) {
        nameTextField.setText("");
        bedArrangementTextField.setText("");
        roomSizeTextField.setText("");
        infoTextArea.setText("");
        priceTextField.setText("");
        categoryImageView.setImage(null);
        resetComponents();
        clearLabels();
        selectedCategory = null;
    }

    private void clearLabels() {
        nameLabel.setText("");
        bedArrangementLabel.setText("");
        roomSizeLabel.setText("");
        infoLabel.setText("");
        priceLabel.setText("");
        categoryImageLabel.setText("");
    }

    private boolean categoryValidation() {


        return DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(bedArrangementTextField.getText())
                && DataValidation.TextFieldNotEmpty(roomSizeTextField.getText())
                && DataValidation.TextFieldNotEmpty(infoTextArea.getText())
                && DataValidation.TextFieldNotEmpty(priceTextField.getText())
                && DataValidation.ImageFieldNotEmpty(categoryImageView)

                && DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(bedArrangementTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(roomSizeTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(infoTextArea.getText(), 400)

                && DataValidation.isValidNumberFormat(priceTextField.getText());


    }

    private void menuValidationMessage() {

        if (!(DataValidation.TextFieldNotEmpty(nameTextField.getText())
                && DataValidation.TextFieldNotEmpty(bedArrangementTextField.getText())
                && DataValidation.TextFieldNotEmpty(roomSizeTextField.getText())
                && DataValidation.TextFieldNotEmpty(infoTextArea.getText())
                && DataValidation.TextFieldNotEmpty(priceTextField.getText())
                && DataValidation.ImageFieldNotEmpty(categoryImageView))) {
            DataValidation.TextFieldNotEmpty(nameTextField.getText(), nameLabel, "Category Name Required!");
            DataValidation.TextFieldNotEmpty(bedArrangementTextField.getText(), bedArrangementLabel, "Category Bed Specification Required!");
            DataValidation.TextFieldNotEmpty(roomSizeTextField.getText(), roomSizeLabel, "Category Room Size Required!");
            DataValidation.TextFieldNotEmpty(infoTextArea.getText(), infoLabel, "Category Room Information Required!");
            DataValidation.TextFieldNotEmpty(priceTextField.getText(), priceLabel, "Category Price Required!");
            DataValidation.ImageFieldNotEmpty(categoryImageView, categoryImageLabel, "Select a Image!");

        }
        if (!(DataValidation.isValidMaximumLength(nameTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(bedArrangementTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(roomSizeTextField.getText(), 45)
                && DataValidation.isValidMaximumLength(infoTextArea.getText(), 400))) {

            DataValidation.isValidMaximumLength(nameTextField.getText(), 45, nameLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(bedArrangementTextField.getText(), 45, bedArrangementLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(roomSizeTextField.getText(), 45, roomSizeLabel, "Field Limit 45 Exceeded!");
            DataValidation.isValidMaximumLength(infoTextArea.getText(), 400, infoLabel, "Field Limit 400 Exceeded!");
        }
        DataValidation.isValidNumberFormat(priceTextField.getText(), priceLabel, "Invalid Price Amount!");
    }
    private void resetComponents() {
        addButton.setVisible(true);
        updateButton.setVisible(false);
    }

    @FXML
    private void chooseImage(ActionEvent event){
        categoryImageLabel.setText("");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.jpg"));
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            staticFile = file;
            Image image = new Image(file.toURI().toString());
            categoryImageView.setImage(image);
        }
    }
}
