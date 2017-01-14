import java.sql.*;

/**
 * Created by rugile on 12/01/17.
 */
public class Item {
    public String body; //size=10000
    public String title; //size=255
    public int id; //primary key

    public Item(int id, String b, String t){
        this.id = id;
        this.body = b;
        this.title = t;
    }

    public static void createTable() throws SQLException {
        Connection conn = getConnection();
        Statement stat = conn.createStatement();
        boolean rs = stat.execute("CREATE TABLE IF NOT EXISTS ITEMS(ID INT PRIMARY KEY, TITLE VARCHAR(255), BODY VARCHAR(10000));");
        closeConnection(conn);
    }
    private static Connection getConnection() throws SQLException {
        //TODO: need to set the passwords automatically using ansible
        return DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/./elpis","sa","s1ple");
    }
    private static void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }
    public static Item fetchUniqueItem(int id) throws SQLException {
        Connection conn = getConnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("SELECT * FROM ITEMS WHERE ID=" + id + ";");
        rs.next();
        return new Item(Integer.parseInt(rs.getString(1)), rs.getString("BODY"), rs.getString("TITLE") );
    }
    public static boolean createNewItem(int id, String title, String body) throws SQLException {
        Connection conn = getConnection();
        Statement stat = conn.createStatement();
        return stat.execute("INSERT INTO ITEMS VALUES (" + id + ", '" + title.replaceAll("'", "\"") + "', '" + body.replaceAll("'", "\"") + "');");
    }


}
