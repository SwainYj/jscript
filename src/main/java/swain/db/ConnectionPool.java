package swain.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

public class ConnectionPool{
    static Logger log = LoggerFactory.getLogger(ConnectionPool.class);
    //最大连接数
    private static final int maxCount = 1;
    //连接池
    private static final LinkedList<Connection> pools = new LinkedList<Connection>();

    public static void init(){
        long start = System.currentTimeMillis();
        log.info("init connection pool ===="+start);
        Connection conn = null;
        try{
            for(int i = 0; i < maxCount; i++) {
                conn = ConnectionBuilder.buildConnection();
                pools.add(conn);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        log.info("init connection pool end ==="+(System.currentTimeMillis()-start)/1000+"s");
    }

    public static synchronized Connection getConnection(){
        Connection conn = null;
        if(pools.size() == 0) {
            conn = ConnectionBuilder.buildConnection();
        } else {
            conn = pools.remove(0);
        }
        return conn;
    }


    public static synchronized void close(Connection conn) {
        if(pools.size() < maxCount) {
            pools.add(conn);
        }else{
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void destory(){
        pools.forEach(conn->{
            try {
                if(conn != null){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
