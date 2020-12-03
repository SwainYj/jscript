package swain.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import swain.threadLocal.ThreadLocalHolder;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * DownloadUtils
 *
 * @author yupeng10@baidu.com
 * @since 1.0
 */
@Slf4j
public class DownloadUtils {

    public static void initDownloadHeader(String fileName, HttpServletResponse response) {
        String name = fileName;
        try {
            if (name == null) {
                name = "";
            }
            name = new String(name.getBytes("UTF-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException， error ", e);
        }
        response.setHeader("Content-type", "application/octet-stream");
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Cache-Control", "");
        response.setHeader("Content-Disposition", "attachment; filename=" + name);
    }


    public static void downloadExcels(Workbook wb, String filename) {
        OutputStream outputStream = null;
        HttpServletResponse response = ThreadLocalHolder.getResponse();
        initDownloadHeader(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date()) + filename + ".xls", response);
        try {
            outputStream = response.getOutputStream();
            wb.write(outputStream);
        } catch (IOException e) {
            log.error("export statistics excel error：", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            try {
//                wb.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        log.info("export statistics excel success");
    }


}
