package swain.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import swain.db.ConnectionPool;
import swain.utils.DownloadUtils;
import swain.utils.ExcelUtils;
import swain.utils.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class BackWord {
    Logger logger = LoggerFactory.getLogger(BackWord.class);

    @Autowired
    ConnectionPool  connectionPool;

    public int fetch(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        String sql = "SELECT USER_ID,WORDS FROM BACK_CHECK_WORDS";

        Map<String, String> titleMap = Maps.newLinkedHashMap();
        List<Map<String, Object>> dataMap = Lists.newArrayList();
        titleMap.put("userId", "用户id");
        titleMap.put("words", "关键词");

        try {
            connection = connectionPool.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            while(rs.next()){
                Map<String, Object> row = Maps.newHashMap();
                row.put("userId", rs.getString("USER_ID"));
                row.put("words", rs.getString("WORDS"));

                dataMap.add(row);
            }

            Workbook wb = ExcelUtils.createExcel(titleMap, dataMap);
            Sheet sheet = wb.getSheet("人员信息");

            sheet.setColumnWidth(1, 256 * 15);
            sheet.setColumnWidth(2, 256 * 40);

            DownloadUtils.downloadExcels(wb, "采集人员名单");

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally {
            release(connection,preparedStatement,rs);
        }

        return 1;
    }

    public void release(Connection connection, Statement statement, ResultSet resultSet){
        try {
            if(resultSet!=null){
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(statement!=null){
                statement.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        connectionPool.close(connection);
    }
}
