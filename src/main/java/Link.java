import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Link extends DatabaseTable {
    public String url;
    public boolean offline;
    public String description;


    public Link(int id, String url, boolean offline, String description) {
        super(id);
        this.url = url;
        this.offline = offline;
        this.description = description;
    }

    public static void createTable() throws SQLException {
        Connection conn = getConnection();
        Statement stat = conn.createStatement();
        stat.execute("CREATE TABLE IF NOT EXISTS LINKS(ITEM_ID INT, OFFLINE VARCHAR(5), URL VARCHAR(300), DESCRIPTION VARCHAR(200), PRIMARY KEY(ITEM_ID, URL), FOREIGN KEY(ITEM_ID) REFERENCES ITEMS(ID));");
        closeConnection(conn);
    }

    public static int createNewLink(int item_id, String url, boolean offline, String description) throws SQLException {
        Connection conn = getConnection();
        int retval = createNewLink(item_id, url, offline, description, conn);
        closeConnection(conn);
        return retval;
    }

    public static int createNewLink(int item_id, String url, boolean offline, String description, Connection conn) throws SQLException {
        PreparedStatement stat = conn.prepareStatement("INSERT INTO LINKS(ITEM_ID, OFFLINE, URL, DESCRIPTION) VALUES (?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
        stat.setInt(1, item_id);
        stat.setBoolean(2, offline);
        stat.setString(3, url);
        stat.setString(4, description);
        int rows = stat.executeUpdate();
        if(rows == 0){
            throw new SQLException("Creating new item failed, no rows affected.");
        }
        return rows;
    }

    public static List<Link> selectLinksForItem(Item it) throws SQLException {
        Connection conn = getConnection();
        List<Link> retval = selectLinksForItem(it, conn);
        closeConnection(conn);
        return retval;
    }

    public static List<Link> selectLinksForItem(Item it, Connection conn) throws SQLException {
        List<Link> retval = new ArrayList<Link>();
        PreparedStatement stat = conn.prepareStatement("SELECT * FROM LINKS WHERE ITEM_ID=?;");
        stat.setInt(1, it.id);
        ResultSet rs = stat.executeQuery();
        while(rs.next()){
            retval.add(new Link(rs.getInt("ITEM_ID"), rs.getString("URL"), rs.getBoolean("OFFLINE"), rs.getString("DESCRIPTION")));
        }
        return retval;
    }
}
