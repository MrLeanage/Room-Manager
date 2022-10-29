package service;


import bean.Category;
import bean.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import utility.dataHandler.UtilityMethod;
import utility.dbConnection.DBConnection;
import utility.popUp.AlertPopUp;
import utility.query.RoomQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomService {
    private Connection connection;
    public RoomService(){
        connection = DBConnection.getDBConnection();
    }

    public ObservableList<Room> loadAllRoomData() {
        ObservableList<Room> roomObservableList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(RoomQuery.LOAD_ALL_ROOM_DATA);
            while (resultSet.next()) {
                CategoryService categoryService = new CategoryService();
                Category category = categoryService.loadSpecificCategory(UtilityMethod.addPrefix("C", resultSet.getString(2)));

                ReservationService reservationService = new ReservationService();
                String reservationStatus = "NOT RESERVED";
                if(reservationService.isSpecificRoomReserved(resultSet.getString(1)))
                    reservationStatus = "RESERVED";

                roomObservableList.add(new Room(resultSet.getString(1), category, resultSet.getString(3), resultSet.getString(4), reservationStatus));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return roomObservableList;
    }

    public ObservableList<Room> loadAllRoomDataByAvailableStatus(String availabilityStatus) {
        ObservableList<Room> roomObservableList = FXCollections.observableArrayList();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(RoomQuery.LOAD_ALL_ROOM_DATA_BY_AVAILABILITY);
            preparedStatement.setString(1, availabilityStatus);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CategoryService categoryService = new CategoryService();
                Category category = categoryService.loadSpecificCategory(UtilityMethod.addPrefix("C", resultSet.getString(2)));
                roomObservableList.add(new Room(resultSet.getString(1), category, resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return roomObservableList;
    }

    public Room loadSpecificRoom(String rID) {
        Room room = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(RoomQuery.LOAD_SPECIFIC_ROOM_DATA);
            preparedStatement.setInt(1, UtilityMethod.seperateID(rID));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CategoryService categoryService = new CategoryService();
                Category category = categoryService.loadSpecificCategory(UtilityMethod.addPrefix("C", resultSet.getString(2)));
                room = new Room(resultSet.getString(1), category, resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
            }
        } catch (Exception sqlException) {
            sqlException.printStackTrace();
            sqlException.printStackTrace();
            AlertPopUp.sqlQueryError(sqlException);

        }
        return room;
    }

    public boolean insertRoomData(Room room) {
        PreparedStatement psRoom = null;
        try {
            psRoom = connection.prepareStatement(RoomQuery.INSERT_ROOM_DATA);

            psRoom.setInt(1, UtilityMethod.seperateID(room.getrCID()));
            psRoom.setString(2, room.getrIdentification());
            psRoom.setString(3, room.getrAvailabilityStatus());
            psRoom.setString(4, room.getrReservationStatus());
            psRoom.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
        }
        return false;
    }

    public boolean updateRoomData(Room room) {
        PreparedStatement psRoom = null;
        try {

            psRoom = connection.prepareStatement(RoomQuery.UPDATE_ROOM_DATA);

            psRoom.setInt(1, UtilityMethod.seperateID(room.getrCID()));
            psRoom.setString(2, room.getrIdentification());
            psRoom.setString(3, room.getrAvailabilityStatus());
            psRoom.setString(4, room.getrReservationStatus());
            psRoom.setInt(5, UtilityMethod.seperateID(room.getrID()));
            psRoom.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
        }
        return false;
    }

    public boolean deleteRoomData(String rID) {
        PreparedStatement psRoom = null;
        try {

            psRoom = connection.prepareStatement(RoomQuery.DELETE_ROOM_DATA);

            psRoom.setInt(1, UtilityMethod.seperateID(rID));
            psRoom.execute();
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
        }
        return false;
    }

    public SortedList<Room> searchTable(TextField searchTextField) {
        //Retreiving all data from database
        ObservableList<Room> menuData = loadAllRoomData();
        //Wrap the ObservableList in a filtered List (initially display all data)
        FilteredList<Room> filteredData = new FilteredList<>(menuData, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(room -> {
                //if filter text is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //comparing search text with table columns one by one
                String lowerCaseFilter = newValue.toLowerCase();

                 if (room.getrCID().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                } else if (room.getrCategory().getcName().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                 } else if (room.getrIdentification().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     //return if filter matches data
                     return true;
                 } else if (room.getrAvailabilityStatus().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     //return if filter matches data
                     return true;
                }else if (room.getrReservationStatus().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                     //return if filter matches data
                     return true;
                 } else {
                    //have no matching
                    return false;
                }
            });
        });
        //wrapping the FilteredList in a SortedList
        SortedList<Room> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }

}
