import ro.pippo.core.Pippo;
import org.h2.tools.Server;

import java.sql.SQLException;


public class Elpis {
    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        try{
            stopDB(); //stop server if database running
        } catch (org.h2.jdbc.JdbcSQLException e){}
        startDB();
        Item.createTable(); //create items table if does not already exist
        Pippo pippo = new Pippo(new ElpisApplication());
        pippo.start();
    }
    private static void startDB() throws SQLException {
        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    private static void stopDB() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
    }
}
