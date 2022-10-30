package service;


import bean.Category;
import bean.Reservation;
import bean.Room;
import bean.RoomBooking;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TextField;
import utility.dataHandler.UtilityMethod;
import utility.dbConnection.DBConnection;
import utility.popUp.AlertPopUp;
import utility.query.CategoryQuery;
import utility.query.ReservationQuery;
import utility.query.RoomQuery;

import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Observable;
import java.util.function.Predicate;

public class ReservationService {
    private Connection connection;
    public ReservationService(){
        connection = DBConnection.getDBConnection();
    }

    /**
     * Get All reservation Data
     */
    public ObservableList<Reservation> getAllReservationData(){
        ObservableList<Reservation> reservationObservableList = FXCollections.observableArrayList();
        try {
            ResultSet reservationResultSet = connection.createStatement().executeQuery(ReservationQuery.LOAD_ALL_RESERVATION_BOOKINGS);

            while (reservationResultSet.next()) {
                reservationObservableList.add(new Reservation(reservationResultSet.getString(1), reservationResultSet.getString(2),
                        reservationResultSet.getString(3),reservationResultSet.getString(4),
                        reservationResultSet.getString(5),reservationResultSet.getString(6),
                        reservationResultSet.getString(7),reservationResultSet.getString(8),
                        reservationResultSet.getString(9),reservationResultSet.getString(10), getSpecificReservedRoomList(reservationResultSet.getString(1))));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
            System.out.println(sqlException);
        }
        return reservationObservableList;
    }
    /**
     * Get All reservation Data
     */
    public ObservableList<Reservation> getCustomReservationData(LocalDate checkInStartDate, LocalDate checkOutEndDate, String reservationStatus){
        ObservableList<Reservation> reservationObservableList = FXCollections.observableArrayList();
        try {
            ResultSet reservationResultSet = null;
            if(reservationStatus.toLowerCase().equals("all")){
                reservationResultSet = connection.createStatement().executeQuery(ReservationQuery.LOAD_ALL_RESERVATION_BOOKINGS);
            }else{
                PreparedStatement reservationPreparedStatement = connection.prepareStatement(ReservationQuery.LOAD_ALL_RESERVATION_BOOKINGS_BY_STATUS);
                reservationPreparedStatement.setString(1, reservationStatus);
                reservationResultSet = reservationPreparedStatement.executeQuery();
            }

            while (reservationResultSet.next()) {
                LocalDate reservationCheckInDate = LocalDate.parse(reservationResultSet.getString(7));
                LocalDate reservationCheckOutDate = LocalDate.parse(reservationResultSet.getString(8));


                if(!checkInStartDate.isAfter(reservationCheckInDate) && !checkOutEndDate.isBefore(reservationCheckOutDate)){
                    reservationObservableList.add(new Reservation(reservationResultSet.getString(1), reservationResultSet.getString(2),
                            reservationResultSet.getString(3),reservationResultSet.getString(4),
                            reservationResultSet.getString(5),reservationResultSet.getString(6),
                            reservationResultSet.getString(7),reservationResultSet.getString(8),
                            reservationResultSet.getString(9),reservationResultSet.getString(10), getSpecificReservedRoomList(reservationResultSet.getString(1))));
                }
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
            System.out.println(sqlException);
        }
        return reservationObservableList;
    }
    /**
     * Get Available Rooms for given Start and end Date
     * @param startDate startDate
     * @param endDate endDate
     * @return availableRoomList
     */
    public ObservableList<Room> getAvailableRoomsForCategories(String startDate, String endDate) {
        /**
         * ############ PLAN
         * 1. Get All Room Data where room is available to use
         * 2. Create a new Room list for the all room data by adding getting all dates between check in and out Dates, Mark them as RESERVED
         * 3. Get all currently reserved Bookings
         * 4. Creating reserved room list for all reserved Dates of Reserved Bookings
         * 5. Creating Available room list if a particular room not reserved
         * 6. Sorting available room list by removing room duplicates
         * 7. Return Room List
         */

        /**
         * Creating a room list for given dates
         */

        RoomService roomService = new RoomService();
        //getting all rooms
        ObservableList<Room> roomObservableList = roomService.loadAllRoomDataByAvailableStatus("Available");
        //creating a room list for given dates
        ArrayList<Room> allRoomList = new ArrayList<>();
        for(Room room : roomObservableList){
            for(String date : UtilityMethod.getDaysBetweenGivenTwoDays(startDate, endDate)){
                Room newRoom = room;
                newRoom.setrReservedDate(date);
                newRoom.setrReservationStatus("RESERVED");
                allRoomList.add(newRoom);
            }
        }


        /**
         * Getting currently reserved Booking for rooms
         */
        ArrayList<RoomBooking> roomBookingList = getAllReservedRoomBookings();


        /**
         * Creating reserved room list for all reserved Dates
         */
        ArrayList<Room> reservedRoomList = new ArrayList<>();
//        for(RoomBooking roomBooking : roomBookingList){
//            for(Room room : allRoomList){
//                if(String.valueOf(room.getIntRID()).equals(roomBooking.getRoomNo())){
//                    for(String date : UtilityMethod.getDaysBetweenGivenTwoDays(roomBooking.getCheckInDate(), roomBooking.getCheckOutDate())){
//                        Room newRoom = room;
//                        newRoom.setrReservedDate(date);
//                        newRoom.setrReservationStatus("RESERVED");
//                        reservedRoomList.add(newRoom);
//                    }
//                }
//            }
//
//        }

        for(Room room : roomObservableList){
            for(RoomBooking roomBooking : roomBookingList){
                if(roomBooking.getRoomNo().equals(String.valueOf(room.getIntRID()))){
                    for(String date : UtilityMethod.getDaysBetweenGivenTwoDays(roomBooking.getCheckInDate(), roomBooking.getCheckOutDate())){
                        Room newRoom = room;
                        newRoom.setrReservedDate(date);
                        newRoom.setrReservationStatus("RESERVED");
                        reservedRoomList.add(newRoom);
                        System.out.println(date);
                    }
                }
//                    reservedRoomList.add(room);

            }
        }

        /**
         * Creating Available room list if room not reserved
         */
        ArrayList<Room> sortedList = new ArrayList<>();

        for(Room room : allRoomList){
            for(Room reservedRoom : reservedRoomList){
                if(room.getIntRID() == reservedRoom.getIntRID() && room.getrReservedDate().equals(reservedRoom.getrReservedDate())){
                    System.out.println("Room No : " + reservedRoom.getIntRID() + " | Reserved Date : " + reservedRoom.getrReservedDate());
                }else{
                    sortedList.add(room);
                }
            }
        }

//        ArrayList<Room> availableRoomList = new ArrayList<>();
//        for(Room room : allRoomList){
//            for(Room reservedRoom : reservedRoomList){
//                if(!(room.getIntRID() == reservedRoom.getIntRID() && room.getrReservedDate().equals(reservedRoom.getrReservedDate()))){
//                    if(!availableRoomList.contains(room)){
//                        availableRoomList.add(room);
//                    }
//                }
//            }
//        }
//
        /**
         * Sorting available room list by removing room duplicates
         */
        ArrayList<Room> filteredList = new ArrayList<>();
        ArrayList<String> roomIDs = new ArrayList<>();

        for(Room room : sortedList){
            if(!roomIDs.contains(room.getrID())){
                room.setrReservationStatus("AVAILABLE");
                roomIDs.add(room.getrID());
                filteredList.add(room);
            }
        }

        return FXCollections.observableArrayList(filteredList);
    }

    /**
     * Adding a Reservation
     */
    public boolean addReservation(Reservation reservation){
        /**
         * Adding Customer Reservation Info
         */

        PreparedStatement psReservation = null;
        try {
            //adding Customer Info
            psReservation = connection.prepareStatement(ReservationQuery.INSERT_CUSTOMER_RESERVATION_DATA, Statement.RETURN_GENERATED_KEYS);

            psReservation.setString(1, reservation.getResCustomerName());
            psReservation.setString(2, reservation.getResCustomerEmail());
            psReservation.setString(3, reservation.getResCustomerNIC());
            psReservation.setString(4, reservation.getResCustomerAddress());
            psReservation.setString(5, reservation.getResCustomerPhone());
            psReservation.setString(6, reservation.getResReservedDate());
            psReservation.setString(7, reservation.getResCheckInDate());
            psReservation.setString(8, reservation.getResCheckOutDate());
            psReservation.setString(9, reservation.getResStatus());
            psReservation.execute();

            //Getting Reservation ID
            ResultSet generatedKeys = psReservation.getGeneratedKeys();
            //Adding Reserved Rooms with generated Reservation ID
            while (generatedKeys.next()){
                int generatedReservationId = generatedKeys.getInt(1);
                PreparedStatement psRoom = null;
                for(Room room : reservation.getReservedRoomList()){
                    psRoom = connection.prepareStatement(ReservationQuery.INSERT_RESERVATION_ROOM_BOOKING);
                    psRoom.setInt(1, generatedReservationId);
                    psRoom.setInt(2, UtilityMethod.seperateID(room.getrID()));
                    psRoom.setString(3, reservation.getResCheckInDate());
                    psRoom.setString(4, reservation.getResCheckOutDate());
                    psRoom.setString(5, reservation.getResStatus());
                    psRoom.execute();
                }
            }
            return true;

        } catch (SQLException ex) {
            AlertPopUp.sqlQueryError(ex);
        }
        return false;
    }
    /**
     * Update Reservation Status
     */
    public boolean updateReservationStatus(String reservationID, String reservationStatus){
        PreparedStatement psReservation = null;
        try{
            //Updating reservation Status
            psReservation = connection.prepareStatement(ReservationQuery.UPDATE_CUSTOMER_RESERVATION_DATA);

            psReservation.setString(1, reservationStatus);
            psReservation.setString(2, reservationID);

            psReservation.execute();

            //Updating Booking Status of reservations
            updateReservationBookingStatus(reservationID, reservationStatus);
        return true;
        }catch(SQLException exception){

        }
        return false;
    }
    /**
     * search All Reservations in Table
     * @param searchTextField searchTextField
     * @param reservationObservableList reservationObservableList
     * @return sortedData
     */
    public SortedList<Reservation> searchReservationTable(TextField searchTextField, ObservableList<Reservation> reservationObservableList) {

        //Wrap the ObservableList in a filtered List (initially display all data)
        FilteredList<Reservation> filteredData = new FilteredList<>(reservationObservableList, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(room -> {
                //if filter text is empty display all data
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //comparing search text with table columns one by one
                String lowerCaseFilter = newValue.toLowerCase();

                if (room.getResID().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                } else if (room.getResCustomerName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                } else if (room.getResCustomerPhone().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else if (room.getResCustomerNIC().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else if (room.getResStatus().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else {
                    //have no matching
                    return false;
                }
            });
        });
        //wrapping the FilteredList in a SortedList
        SortedList<Reservation> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }

    /**
     * search available Rooms in Table
     * @param searchTextField searchTextField
     * @param roomObservableList roomObservableList
     * @return sortedData
     */
    public SortedList<Room> searchAvailableRoomTable(TextField searchTextField, ObservableList<Room> roomObservableList) {

        //Wrap the ObservableList in a filtered List (initially display all data)
        FilteredList<Room> filteredData = new FilteredList<>(roomObservableList, b -> true);

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
                }else {
                    //have no matching
                    return false;
                }
            });
        });
        //wrapping the FilteredList in a SortedList
        SortedList<Room> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }

    /**
     * search ReRooms in View Table
     * @param searchTextField searchTextField
     * @param roomObservableList roomObservableList
     * @return sortedData
     */
    public SortedList<Room> searchReservedRoomTable(TextField searchTextField, ObservableList<Room> roomObservableList) {

        //Wrap the ObservableList in a filtered List (initially display all data)
        FilteredList<Room> filteredData = new FilteredList<>(roomObservableList, b -> true);

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
                } else if (room.getrReservationStatus().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    //return if filter matches data
                    return true;
                }else {
                    //have no matching
                    return false;
                }
            });
        });
        //wrapping the FilteredList in a SortedList
        SortedList<Room> sortedData = new SortedList<>(filteredData);
        return sortedData;
    }

    public ArrayList<Room> getSpecificReservedRoomList(String reservationID){
        ArrayList<Room> roomObservableList = new ArrayList<>();
        RoomService roomService = new RoomService();
        PreparedStatement psRoomList = null;
        try {
            psRoomList = connection.prepareStatement(ReservationQuery.LOAD_SPECIFIC_ALL_RESERVATION_BOOKINGS);
            psRoomList.setString(1, reservationID);

            ResultSet resultSet = psRoomList.executeQuery();

            while (resultSet.next()) {
                Room room = roomService.loadSpecificRoom(UtilityMethod.addPrefix("r", resultSet.getString(2)));
                room.setrReservationStatus(resultSet.getString(5));
                roomObservableList.add(room);
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return roomObservableList;
    }

    /**
     * Get all reserved Bookings
     * @return
     */
    public ArrayList<RoomBooking> getAllReservedRoomBookings(){
        ArrayList<RoomBooking> roomBookingObservableList = new ArrayList<>();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(ReservationQuery.LOAD_RESERVED_BOOKINGS);
            while (resultSet.next()) {
                roomBookingObservableList.add(new RoomBooking(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return roomBookingObservableList;
    }

    /**
     * Get all reserved Bookings
     * @return
     */
    public ObservableList<RoomBooking> getSpecificReservedRoomBookingsByResID(String reservationID){
        ObservableList<RoomBooking> roomBookingObservableList = FXCollections.observableArrayList();
        PreparedStatement psReservation = null;
        try {
            psReservation = connection.prepareStatement(ReservationQuery.LOAD_SPECIFIC_RESERVATION_BOOKINGS);
            psReservation.setString(1, reservationID);
            ResultSet resultSet = psReservation.executeQuery();
            while (resultSet.next()) {
                roomBookingObservableList.add(new RoomBooking(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5)));
            }

        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
        }
        return roomBookingObservableList;
    }

    /**
     * Update Reservation Booking Status
     */
    public boolean updateReservationBookingStatus(String reservationID, String reservationStatus){
        PreparedStatement psReservation = null;
        try{
            //Updating reservation Status
            psReservation = connection.prepareStatement(ReservationQuery.UPDATE_SPECIFIC_RESERVATION_BOOKING_STATUS);

            psReservation.setString(1, reservationStatus);
            psReservation.setString(2, reservationID);

            psReservation.execute();
            return true;
        }catch(SQLException exception){

        }
        return false;
    }

    /**
     * check whether a specific room is reserved by a customer or not
     * @param roomID
     * @return
     */
    public boolean isSpecificRoomReserved(String roomID) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(ReservationQuery.LOAD_SPECIFIC_ROOM_RESERVATION_BOOKINGS);
            preparedStatement.setString(1, roomID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if(resultSet.getString(5).equals("RESERVED"))
                    return true;
            }
        } catch (SQLException sqlException) {
            AlertPopUp.sqlQueryError(sqlException);
            System.out.println(sqlException);
        }
        return false;
    }

}
