import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Item extends DatabaseTable{
    public String description; //size=10000
    public String title; //size=255
    public int id; //primary key
    public String language;
    public String category;
    public boolean offline;


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
        return new Item(Integer.parseInt(rs.getString(1)), rs.getString("DESCRIPTION"), rs.getString("TITLE"), rs.getString("LANG"), rs.getString("CATEGORY"), Boolean.parseBoolean(rs.getString("OFFLINE")));
    }

    public static List<Item> fetchList(int howMany, int page, String language) throws SQLException {
        List<Item> retval = new ArrayList<Item>();
        Connection conn = getConnection();
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery("SELECT * FROM ITEMS LEFT JOIN ITEMS_T ON ITEMS.ID=ITEMS_T.ITEM_ID WHERE ITEMS_T.LANG=\"" + language + "\" LIMIT " + howMany + " OFFSET " + howMany*page + ";");
        rs.next();
        while(!rs.isAfterLast()){
            retval.add(new Item(rs.getInt(1), rs.getString("DESCRIPTION"), rs.getString("TITLE"), rs.getString("LANG"), rs.getString("CATEGORY"), rs.getBoolean("OFFLINE")));
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
            addNewLanguage(id, language.get(i), title.get(i), description.get(i));
        }
        conn.close();
        return id;
    }
    public static boolean addNewLanguage(int item_id, String language, String title, String description) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stat = conn.prepareStatement("INSERT INTO ITEMS_T VALUES(?, ?, ?, ?);");
        stat.setInt(1,  item_id);
        stat.setString(2, language);
        stat.setString(3, title);
        stat.setString(4, description);
        boolean retval = stat.execute();
        closeConnection(conn);
        return retval;
    }

    }
