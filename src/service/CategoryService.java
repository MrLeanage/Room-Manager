package service;


import bean.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import utility.dataHandler.UtilityMethod;
import utility.dbConnection.DBConnection;
import utility.popUp.AlertPopUp;
import utility.query.CategoryQuery;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryService {
    private Connection connection;
    public CategoryService(){
        connection = DBConnection.getDBConnection();
    }

    public ObservableList<Category> loadAllCategoryData() {
        ObservableList<Category> categoryObservableList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(CategoryQuery.LOAD_ALL_CATEGORY_DATA);
            while (resultSet.next()) {
                InputStream inputStream = resultSet.getBinaryStream(2);
                categoryObservableList.add(new Category(resultSet.getString(1), UtilityMethod.convertInputStreamToImage(inputStream), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),  resultSet.getString(7), resultSet.getDouble(8), resultSet.getString(9)));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return categoryObservableList;
    }

    public ObservableList<Category> loadCustomCategoryData(String categoryType, String categoryAvailability) {
        ObservableList<Category> categoryObservableList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = null;
            if(categoryType.toLowerCase().contains("ALL".toLowerCase()) && categoryAvailability.toLowerCase().contains("ALL".toLowerCase())){
                resultSet = connection.createStatement().executeQuery(CategoryQuery.LOAD_ALL_CATEGORY_DATA);
            }else if(!categoryType.toLowerCase().contains("ALL".toLowerCase()) && categoryAvailability.toLowerCase().contains("ALL".toLowerCase())){
                PreparedStatement preparedStatement = connection.prepareStatement(CategoryQuery.LOAD_ALL_CATEGORY_DATA_BY_ROOM_TYPE);
                preparedStatement.setString(1, categoryType);
                resultSet = preparedStatement.executeQuery();
            }else if(categoryType.toLowerCase().contains("ALL".toLowerCase()) && !categoryAvailability.toLowerCase().contains("ALL".toLowerCase())){
                PreparedStatement preparedStatement = connection.prepareStatement(CategoryQuery.LOAD_ALL_CATEGORY_DATA_BY_AVAILABILITY);
                preparedStatement.setString(1, categoryAvailability);
                resultSet = preparedStatement.executeQuery();
            }else {
                PreparedStatement preparedStatement = connection.prepareStatement(CategoryQuery.LOAD_ALL_CATEGORY_DATA_BY_ROOM_TYPE_AND_AVAILABILITY);
                preparedStatement.setString(1, categoryType);
                preparedStatement.setString(2, categoryAvailability);
                resultSet = preparedStatement.executeQuery();
            }

            while (resultSet.next()) {
                InputStream inputStream = resultSet.getBinaryStream(2);
                categoryObservableList.add(new Category(resultSet.getString(1), UtilityMethod.convertInputStreamToImage(inputStream), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),  resultSet.getString(7), resultSet.getDouble(8), resultSet.getString(9)));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return categoryObservableList;
    }

    public Category loadSpecificCategory(String cID) {
        Category category = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(CategoryQuery.LOAD_SPECIFIC_CATEGORY_DATA);
            preparedStatement.setInt(1, UtilityMethod.seperateID(cID));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                InputStream inputStream = resultSet.getBinaryStream(2);
                category = new Category(resultSet.getString(1), UtilityMethod.convertInputStreamToImage(inputStream), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),  resultSet.getString(7), resultSet.getDouble(8), resultSet.getString(9));
            }
        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
            System.out.println(sqlException);
        }
        return category;
    }

    public boolean insertCategoryData(Category category) {
        PreparedStatement psCategory = null;
        try {
            psCategory = connection.prepareStatement(CategoryQuery.INSERT_CATEGORY_DATA);

            psCategory.setBinaryStream(1, UtilityMethod.convertImageToInputStream(category.getcImageView()));
            psCategory.setString(2, category.getcName());
            psCategory.setString(3, category.getcBedArrangement());
            psCategory.setString(4, category.getcRoomSize());
            psCategory.setString(5, category.getcRoomType());
            psCategory.setString(6, category.getcInfo());
            psCategory.setDouble(7, category.getcPrice());
            psCategory.setString(8, category.getcAvailability());
            psCategory.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
        }
        return false;
    }

    public boolean updateCategoryData(Category category) {
        PreparedStatement psCategory = null;
        try {

            psCategory = connection.prepareStatement(CategoryQuery.UPDATE_CATEGORY_DATA);

            psCategory.setBinaryStream(1, UtilityMethod.convertImageToInputStream(category.getcImageView()));
            psCategory.setString(2, category.getcName());
            psCategory.setString(3, category.getcBedArrangement());
            psCategory.setString(4, category.getcRoomSize());
            psCategory.setString(5, category.getcRoomType());
            psCategory.setString(6, category.getcInfo());
            psCategory.setDouble(7, category.getcPrice());
            psCategory.setString(8, category.getcAvailability());
            psCategory.setInt(9, UtilityMethod.seperateID(category.getcID()));
            psCategory.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
        }
        return false;
    }

    public boolean deleteCategoryData(String cID) {
        PreparedStatement psCategory = null;
        try {

            psCategory = connection.prepareStatement(CategoryQuery.DELETE_CATEGORY_DATA);

            psCategory.setInt(1, UtilityMethod.seperateID(cID));
            psCategory.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
        }
        return false;
    }

    public SortedList<Category> searchTable(TextField searchTextField) {
        //Retreiving all data from database
        ObservableList<Category> menuData = loadAllCategoryData();
        //Wrap the ObservableList in a filtered List (initially display all data)
        FilteredList<Category> filteredData = new FilteredList<>(menuData, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(category -> {
                //if filter text is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //comparing search text with table columns one by one
                String lowerCaseFilter = newValue.toLowerCase();

                 if (category.getcName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                } else if (category.getcPrice().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                 } else if (category.getcBedArrangement().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     //return if filter matches data
                     return true;
                 } else if (category.getcRoomType().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     //return if filter matches data
                     return true;
                }else if (category.getcAvailability().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     //return if filter matches data
                     return true;
                 } else {
                    //have no matchings
                    return false;
                }
            });
        });
        //wrapping the FilteredList in a SortedList
        SortedList<Category> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }

}
