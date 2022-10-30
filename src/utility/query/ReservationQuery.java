package utility.query;

public class ReservationQuery {
    public static final String LOAD_ALL_RESERVATION_BOOKINGS = "SELECT * FROM reservations";
    public static final String LOAD_ALL_RESERVATION_BOOKINGS_BY_STATUS = "SELECT * FROM reservations WHERE resStatus = ?";
    public static final String LOAD_SPECIFIC_ALL_RESERVATION_BOOKINGS = "SELECT * FROM room_bookings WHERE rID = ?";
    public static final String LOAD_RESERVED_BOOKINGS = "SELECT * FROM room_bookings WHERE bookingStatus = 'RESERVED'";
    public static final String LOAD_SPECIFIC_RESERVATION_BOOKINGS = "SELECT * FROM room_bookings WHERE rID = ?";
    public static final String LOAD_SPECIFIC_ROOM_RESERVATION_BOOKINGS = "SELECT * FROM room_bookings WHERE roomNo = ?";
    public static final String INSERT_CUSTOMER_RESERVATION_DATA = "INSERT INTO reservations (resCusName, resCusEmail, resCusNIC, resCusAddress, resCusPhone, resReservedDate, resCheckInDate, resCheckOutDate, resStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_CUSTOMER_RESERVATION_DATA = "UPDATE reservations SET resStatus = ? WHERE resID = ?";
    public static final String INSERT_RESERVATION_ROOM_BOOKING = "INSERT INTO room_bookings(rID, roomNo, checkInDate, checkOutDate, bookingStatus) VALUES (?, ?, ?, ?, ?)";
    public static final String UPDATE_SPECIFIC_RESERVATION_BOOKING_STATUS = "UPDATE room_bookings SET bookingStatus = ? WHERE rID = ?";
}
