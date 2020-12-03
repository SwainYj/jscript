package swain;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import swain.controller.BackWord;
import swain.db.ConnectionPool;

public class Main {
    static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

        ConnectionPool.init();
        long startTime = System.currentTimeMillis();

        BackWord backWord = new BackWord();
        backWord.fetch();
        logger.info("move updateCert end :"+(System.currentTimeMillis()-startTime)/1000 +"s");

        ConnectionPool.destory();
        System.exit(1);
    }
}
