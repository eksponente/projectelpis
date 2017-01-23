import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Item extends DatabaseTable{
    public String description; //size=10000
    public String title; //size=255
    public String language;
    public String category;
    public boolean offline;
    public List<Link> links;

    public Item(int id, String d, String t, String l, String c, boolean offline){
        super(id);
        this.description = d;
        this.title = t;
        this.language = l;
        this.offline = offline;
        this.category = c;
    }

    public static void createTable() throws SQLException {
        Connection conn = getConnection();
        Statement stat = conn.createStatement();
        stat.execute("CREATE TABLE IF NOT EXISTS ITEMS(CATEGORY VARCHAR(255), OFFLINE VARCHAR(5), ID INT PRIMARY KEY AUTO_INCREMENT);");
        stat.execute("CREATE TABLE IF NOT EXISTS ITEMS_T(ITEM_ID INT, LANG VARCHAR(5), TITLE VARCHAR(255), DESCRIPTION VARCHAR(10000), PRIMARY KEY(ITEM_ID, LANG), FOREIGN KEY(ITEM_ID) REFERENCES ITEMS(ID));");
        closeConnection(conn);
    }

    public static Item fetchUniqueItem(int id, String language) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stat = conn.prepareStatement("SELECT * FROM ITEMS " +
                "LEFT JOIN ITEMS_T ON ITEMS.ID=ITEMS_T.ITEM_ID " +
                "WHERE ITEMS.ID=? AND ITEMS_T.LANG=?;");
        stat.setInt(1, id);
        stat.setString(2, language);
        ResultSet rs = stat.executeQuery();
        rs.next();
        return new Item(rs.getInt("ID"), rs.getString("DESCRIPTION"), rs.getString("TITLE"), rs.getString("LANG"), rs.getString("CATEGORY"), rs.getBoolean("OFFLINE"));
    }

    public static List<Item> fetchList(int howMany, int page, String language) throws SQLException {
        List<Item> retval = new ArrayList<Item>();
        Connection conn = getConnection();
        PreparedStatement stat = conn.prepareStatement("SELECT * FROM ITEMS LEFT JOIN ITEMS_T ON ITEMS.ID=ITEMS_T.ITEM_ID WHERE ITEMS_T.LANG=? LIMIT ? OFFSET ?;");
        stat.setString(1, language);
        stat.setInt(2, howMany);
        stat.setInt(3, (page-1)*howMany);
        ResultSet rs = stat.executeQuery();
        rs.next();
        while(!rs.isAfterLast()){
            Item newItem = new Item(rs.getInt("ID"), rs.getString("DESCRIPTION"), rs.getString("TITLE"), rs.getString("LANG"), rs.getString("CATEGORY"), rs.getBoolean("OFFLINE"));
            newItem.links = Link.selectLinksForItem(newItem, conn);
            retval.add(newItem);

            rs.next();
        }
        closeConnection(conn);
        return retval;
    }

    public static int createNewItem(List<String> title, List<String> description, List<String> language, String category, boolean offline) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stat = conn.prepareStatement("INSERT INTO ITEMS (CATEGORY, OFFLINE) VALUES (?,?);", Statement.RETURN_GENERATED_KEYS);
        stat.setString(1, category);
        stat.setBoolean(2, offline);
        int rows = stat.executeUpdate();
        if(rows == 0){
            throw new SQLException("Creating new item failed, no rows affected.");
        }
        ResultSet generatedKeys = stat.getGeneratedKeys();
        generatedKeys.next();
        int id = generatedKeys.getInt(1);
        for(int i = 0; i < language.size(); i++){
            addNewLanguage(id, language.get(i), title.get(i), description.get(i), conn);
        }
        conn.close();
        return id;
    }
    private static boolean addNewLanguage(int item_id, String language, String title, String description, Connection conn) throws SQLException {
        PreparedStatement stat = conn.prepareStatement("INSERT INTO ITEMS_T VALUES(?, ?, ?, ?);");
        stat.setInt(1,  item_id);
        stat.setString(2, language);
        stat.setString(3, title);
        stat.setString(4, description);
        boolean retval = stat.execute();
        return retval;
    }
}
