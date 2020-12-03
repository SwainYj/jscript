package swain.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
    private static String DBDRIVER = "com.mysql.jdbc.Driver";
    private static String DBURL = "jdbc:mysql://10.50.0.77:3306/reborn_admin?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=true";
    private static String USERNAME = "Youze";
    private static String PASSWORD = "YouzeACm9H5";
    private static String postUrl = "http://127.0.0.1:9999/certification/dolicense";
    static {
        InputStream in = null;
        try {
            String path = ConnectionBuilder.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String aa= File.separator;
            int lastIndex = path.lastIndexOf("/") + 1;
            path = path.substring(0, lastIndex)+"source.properties";

            File file = new File(path);

            in = new FileInputStream(file);

            Properties properties = new Properties();
            properties.load(in);

            DBDRIVER =  properties.getProperty("driver");
            DBURL = properties.getProperty("url");
            USERNAME = properties.getProperty("username");
            PASSWORD = properties.getProperty("password");
            postUrl = properties.getProperty("postUrl");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String getDBDriver() {
        return DBDRIVER;
    }

    public static String getDBUrl() {
        return DBURL;
    }

    public static String getUserName() {
        return USERNAME;
    }

    public static String getPassword() {
        return PASSWORD;
    }

    public static String getPostUrl() {
        return postUrl;
    }

}
