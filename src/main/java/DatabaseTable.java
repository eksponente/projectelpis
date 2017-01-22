import java.sql.*;

/**
 * Created by rugile on 12/01/17.
 */
public abstract class DatabaseTable {
    public int id;

    public DatabaseTable(int id){
        this.id = id;
    }


    protected static Connection getConnection() throws SQLException {
        //TODO: need to set the passwords automatically using ansible
        return DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/./elpis","sa","s1ple");
    }
    protected static void closeConnection(Connection conn) throws SQLException {
        conn.close();
    }

}
