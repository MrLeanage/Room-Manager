package utility.query;

public class RoomQuery {
    public static final String LOAD_ALL_ROOM_DATA = "SELECT * FROM rooms";
    public static final String LOAD_SPECIFIC_ROOM_DATA = "SELECT * FROM rooms WHERE rID = ?";
    public static final String INSERT_ROOM_DATA = "INSERT INTO rooms (rCID, rIdentification, rAvailabilityStatus, rReservationStatus) VALUES (?, ?, ?, ?)";
    public static final String UPDATE_ROOM_DATA = "UPDATE rooms SET rCID = ?, rIdentification = ?, rAvailabilityStatus = ?, rReservationStatus = ?WHERE rID = ?";
    public static final String DELETE_ROOM_DATA = "DELETE FROM rooms WHERE rID = ?";
}
