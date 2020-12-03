package swain.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBuilder {
    private static String DBDRIVER = ConfigUtil.getDBDriver();
    private static String DBURL = ConfigUtil.getDBUrl();
    private static String USERNAME = ConfigUtil.getUserName();
    private static String PASSWORD = ConfigUtil.getPassword();


    public static Connection buildConnection(){
        Connection conn = null;
        try {
            Class.forName(DBDRIVER).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn= DriverManager.getConnection(DBURL,USERNAME,PASSWORD);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

}
