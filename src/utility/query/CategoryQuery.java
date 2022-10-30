package utility.query;

public class CategoryQuery {
    public static final String LOAD_ALL_CATEGORY_DATA = "SELECT * FROM category";
    public static final String LOAD_ALL_CATEGORY_DATA_BY_AVAILABILITY = "SELECT * FROM category WHERE cAvailability = ?";
    public static final String LOAD_ALL_CATEGORY_DATA_BY_ROOM_TYPE = "SELECT * FROM category WHERE cRoomType = ?";
    public static final String LOAD_ALL_CATEGORY_DATA_BY_ROOM_TYPE_AND_AVAILABILITY = "SELECT * FROM category WHERE cRoomType = ? AND cAvailability = ?";
    public static final String LOAD_SPECIFIC_CATEGORY_DATA = "SELECT * FROM category WHERE cID = ?";
    public static final String INSERT_CATEGORY_DATA = "INSERT INTO category (cImage, cName, cBedArrangement, cRoomSIze, cRoomType,  cInfo, cPrice, cAvailability) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_CATEGORY_DATA = "UPDATE category SET cImage = ?, cName = ?, cBedArrangement = ?, cRoomSize = ?, cRoomType = ?,  cInfo = ?, cPrice = ?, cAvailability = ? WHERE cID = ?";
    public static final String DELETE_CATEGORY_DATA = "DELETE FROM category WHERE cID = ?";
}
