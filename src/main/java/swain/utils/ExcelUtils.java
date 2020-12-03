package swain.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaError;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

/**
 * ExcelUtils
 *
 * @author yupeng10@baidu.com
 * @since 1.0
 */
public class ExcelUtils {
    // -------------------------- STATIC METHODS --------------------------

    /**
     * 创建workbook对象
     *
     * @param file
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static Workbook create(File file) throws IOException, InvalidFormatException {
        return WorkbookFactory.create(file);
    }

    /**
     * 创建workbook对象，缓存整个文件，需要较大的内存空间
     *
     * @param inputStream
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static Workbook create(InputStream inputStream) throws IOException, InvalidFormatException {
        return WorkbookFactory.create(inputStream);
    }

    /**
     * 加载excel文件中第一个工作簿，如有异常则返回null
     *
     * @param inputStream excel文件
     * @return
     */
    public static Sheet loadFirstSheet(InputStream inputStream) {
        try {
            return WorkbookFactory.create(inputStream).getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    // =======================================================================//
    // ============================sheet util=================================//
    // =======================================================================//

    /**
     * 打印sheet信息
     *
     * @param sheet
     */
    public static void printSheetInfo(Sheet sheet) {
        for (Row row : sheet) {
            System.out.print(row.getRowNum() + "\t");
            for (Cell cell : row) {
                System.out.print(getStringCellValue(cell) + "\t");
            }
            System.out.print("\n");
        }
    }

    /**
     * 获取sheets的集合
     *
     * @param workbook
     * @return
     */
    public static List<Sheet> sheetList(Workbook workbook) {
        return Lists.newArrayList(sheetMap(workbook).values());
    }

    /**
     * 获取sheet的映射
     *
     * @param workbook
     * @return
     */
    public static Map<String, Sheet> sheetMap(Workbook workbook) {
        Map<String, Sheet> map = Maps.newLinkedHashMap();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            map.put(sheet.getSheetName(), sheet);
        }
        return map;
    }

    // =======================================================================//
    // ============================row util==================================//
    // =======================================================================//

    /**
     * 获取指定行
     *
     * @param workbook
     * @param sheetIndex
     * @param rowIndex
     * @return
     */
    public static Row row(Workbook workbook, int sheetIndex, int rowIndex) {
        return workbook.getSheetAt(sheetIndex).getRow(rowIndex);
    }

    // =======================================================================//
    // ============================cell util==================================//
    // =======================================================================//

    /**
     * 获取单元格的string数据
     *
     * @param cell
     * @return
     */
    public static String getStringCellValue(Cell cell) {
        cell.setCellType(Cell.CELL_TYPE_STRING);
        return getStringCellValue(cell, "");
    }

    /**
     * 获取单元格的string数据
     *
     * @param cell
     * @param defaultValue
     * @return
     */
    public static String getStringCellValue(Cell cell, String defaultValue) {
        return _getValue(cell, String.class, defaultValue);
    }

    /**
     * 获取单元格的BigDecimal数据
     *
     * @param cell
     * @param defaultValue
     * @return
     */
    public static BigDecimal getNumberCellValue(Cell cell, BigDecimal defaultValue) {
        return _getValue(cell, BigDecimal.class, defaultValue);
    }

    /**
     * 获取单元格的Integer数据
     *
     * @param cell
     * @return
     */
    public static Integer getIntegerCellValue(Cell cell) {
        return _getNumberCellValue(cell, Integer.class, 0);
    }

    /**
     * 获取单元格的Long数据
     *
     * @param cell
     * @return
     */
    public static Long getLongCellValue(Cell cell) {
        return _getNumberCellValue(cell, Long.class, 0L);
    }

    /**
     * 获取单元格的double数据
     *
     * @param cell
     * @param defaultValue
     * @return
     */
    public static Date getDateCellValue(Cell cell, Date defaultValue) {
        return _getValue(cell, Date.class, defaultValue);
    }

    /**
     * 获取单元格的boolean数据
     *
     * @param cell
     * @param defaultValue
     * @return
     */
    public static Boolean getBooleanCellValue(Cell cell, Boolean defaultValue) {
        return _getValue(cell, Boolean.class, defaultValue);
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @param c
     * @param defaultValue
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <T> T _getValue(Cell cell, Class<T> c, T defaultValue) {
        if (cell == null) {
            return defaultValue;
        }

        T value;
        if (Number.class.isAssignableFrom(c)) {
            // 数字 BigDecimal, BigInteger,
            // Byte, Double, Float, Integer,
            // Long, and Short.
            value = _getNumberCellValue(cell, c, defaultValue);
        } else if (Date.class.equals(c)) {
            Date date = cell.getDateCellValue();
//            value = date != null ? (T) date : defaultValue;
            value = null == date ? defaultValue : (T) date;
        } else if (Boolean.class.equals(c) || boolean.class.equals(c)) {
            try {
                boolean b = cell.getBooleanCellValue();
                value = (T) Boolean.valueOf(b);
            } catch (IllegalStateException e) {
                value = defaultValue;
            }
        } else if (String.class.equals(c)) {
            // 其他类型按照字符串处理
            value = (T) _getStringCellValue(cell);
        } else {
            throw new IllegalArgumentException("不支持的类型{}" + c.getName());
        }
        return value == null ? defaultValue : value;
    }

    /**
     * 数字类型BigDecimal, BigInteger, Byte, Double, Float, Integer, Long, and
     * Short. 如果是BigInteger只能直接返回默认值
     *
     * @param cell
     * @param c
     * @param defaultValue
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <T> T _getNumberCellValue(Cell cell, Class<T> c, T defaultValue) {
        if (!Number.class.isAssignableFrom(c) || BigInteger.class.equals(c)) {
            return defaultValue;
        }
        try {
            int cellType = cell.getCellType();
            switch (cellType) {
                case CELL_TYPE_FORMULA:
                case CELL_TYPE_NUMERIC: {
                    // 数字或者表达式单元格
                    double value = cell.getNumericCellValue();
                    if (double.class.equals(c) || Double.class.equals(c)) {
                        return (T) Double.valueOf(value);
                    }
                    return double2Number(value, c);
                }
                case CELL_TYPE_BLANK:
                    // 如果为空返回默认值
                    return defaultValue;
                case CELL_TYPE_STRING: {
                    String value = cell.getStringCellValue();
                    return string2Number(value, c);
                }
                default: {
                    cell.getNumericCellValue();
                    // 肯定报错了
                    return defaultValue;
                }
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 数字类型BigDecimal, BigInteger, Byte, Double, Float, Integer, Long, and
     * Short.
     *
     * @param value
     * @param c
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    private static <T> T double2Number(double value, Class<T> c) {

        BigDecimal number = BigDecimal.valueOf(value);
        return bigDecimal2Number(number, c);
    }

    private static <T> T bigDecimal2Number(BigDecimal number, Class<T> c) {
        T n;
        if (BigDecimal.class.equals(c)) {
            n = (T) number;
        } else if (Short.class.equals(c) || short.class.equals(c)) {
            n = (T) Short.valueOf(number.shortValue());
        } else if (Integer.class.equals(c) || int.class.equals(c)) {
            n = (T) Integer.valueOf(number.intValue());
        } else if (Long.class.equals(c) || long.class.equals(c)) {
            n = (T) Long.valueOf(number.longValue());
        } else if (Float.class.equals(c) || float.class.equals(c)) {
            n = (T) Float.valueOf(number.floatValue());
        } else if (Byte.class.equals(c) || byte.class.equals(c)) {
            n = (T) Byte.valueOf(number.byteValue());
        } else {
            throw new IllegalArgumentException("程序逻辑有问题");
        }
        return n;
    }

    /**
     * 数字类型BigDecimal, BigInteger, Byte, Double, Float, Integer, Long, and Short.
     *
     * @param value
     * @param c
     * @param <T>
     * @return
     */
    private static <T> T string2Number(String value, Class<T> c) {
        return bigDecimal2Number(new BigDecimal(value), c);
        // return double2Number(new BigDecimal(value).doubleValue(), c);
    }

    /**
     * 获取字符串值
     *
     * @param cell
     * @return
     */
    private static String _getStringCellValue(Cell cell) {
        String value = null;
        int cellType = cell.getCellType();
        switch (cellType) {
            case CELL_TYPE_FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case CELL_TYPE_NUMERIC:
                        value = String.valueOf(cell.getNumericCellValue());
                        break;
                    case CELL_TYPE_STRING:
                        value = cell.getStringCellValue();
                        break;
                    case CELL_TYPE_BOOLEAN:
                        value = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case CELL_TYPE_ERROR:
                        value = String.valueOf(cell.getErrorCellValue());
                        break;
                    default:
                        value = cell.getCellFormula();
                        break;
                }
                break;
            case CELL_TYPE_BLANK:
                // 如果为空返回默认值
            case CELL_TYPE_STRING: {
                value = cell.getStringCellValue();
                break;
            }
            case CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case CELL_TYPE_NUMERIC:
                value = String.valueOf(cell.getNumericCellValue());
                break;
            case CELL_TYPE_ERROR:
                value = FormulaError.forInt(cell.getErrorCellValue()).getString();
                break;
//            default: {
//                // is empty
//            }
            default:
        }
        return value;
    }

    /**
     * 获取指定单元格
     *
     * @param sheet
     * @param rowIndex
     * @param cellIndex
     * @return
     */
    public static Cell cell(Sheet sheet, int rowIndex, int cellIndex) {
        return sheet.getRow(rowIndex).getCell(cellIndex);
    }

    /**
     * 获取指定单元格
     *
     * @param workbook
     * @param sheetIndex
     * @param rowIndex
     * @param cellIndex
     * @return
     */
    public static Cell cell(Workbook workbook, int sheetIndex, int rowIndex, int cellIndex) {
        return row(workbook, sheetIndex, rowIndex).getCell(cellIndex);
    }

    /**
     * 反射的什么都不用了，还是直接导出吧，简单导出逻辑，如果想自定义，以後在扩展吧
     *
     * @param titlePattern 标题模式
     * @return
     */
    public static <T> Workbook createExcel(String titlePattern, List<T> data) {
        Map<String, String> titleMap = Splitter.on(',').withKeyValueSeparator("|").split(titlePattern);
        return createExcelFromBeanData(titleMap, data);
    }

    /**
     * 反射的什么都不用了，还是直接导出吧，简单导出逻辑，如果想自定义，以後在扩展吧
     *
     * @param titleMap
     * @param dataMap
     * @return
     */
    public static Workbook createExcel(Map<String, String> titleMap, List<Map<String, Object>> dataMap) {
        Workbook wb = new HSSFWorkbook();// 就用03吧
        Sheet sheet = wb.createSheet("人员信息");
        sheet.setDefaultRowHeightInPoints(20);

        // create title
        Row titleRow = sheet.createRow((short) 0);
        titleRow.setHeightInPoints(20);

        Font font = wb.createFont();
//        font.setColor(HSSFFont.COLOR_NORMAL);

        font.setFontName("华文仿宋");
        font.setFontHeightInPoints((short)12);
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFont(font);

        //边框
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        titleStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        //solid 填充  foreground  前景色
        titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        int titleColumn = 0;
        for (Map.Entry<String, String> entry : titleMap.entrySet()) {
            Cell titleCell = titleRow.createCell(titleColumn++);
            titleCell.setCellType(Cell.CELL_TYPE_STRING);
            titleCell.setCellStyle(titleStyle);
            titleCell.setCellValue(entry.getValue());
        }
        sheet.createFreezePane(0, 1, 0, 1);

        // create body
        CellStyle contentStyle = wb.createCellStyle();
        contentStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        contentStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        contentStyle.setWrapText(false);

        //字体
        Font font2 = wb.createFont();
        font2.setFontName("华文仿宋");
        font2.setFontHeightInPoints((short)12);
        contentStyle.setFont(font2);
        //边框
        contentStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        contentStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        contentStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        contentStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

        int dataRow = 1;
        for (Map<String, Object> dataItem : dataMap) {
            Row row = sheet.createRow(dataRow++);
            row.setHeightInPoints(20);
            int dataColumn = 0;
            for (Map.Entry<String, String> entry : titleMap.entrySet()) {

                String key = entry.getKey();
                String formattedValue;
                if (dataItem.get(key) instanceof Double) {
                    if (key.endsWith("%")) {
                        NumberFormat defaultFormat = NumberFormat.getPercentInstance();
                        defaultFormat.setMaximumFractionDigits(3);
                        formattedValue = defaultFormat.format((double) (Double) dataItem.get(key));
                    } else if (key.endsWith(".")) {
                        NumberFormat defaultFormat = NumberFormat.getNumberInstance();
                        defaultFormat.setMaximumFractionDigits(3);
                        formattedValue = defaultFormat.format((double) (Double) dataItem.get(key));
                    } else {
                        formattedValue = ObjectUtils.toString(dataItem.get(key));
                    }
                } else {
                    formattedValue = ObjectUtils.toString(dataItem.get(key));
                }

                Cell contentCell = row.createCell(dataColumn++);
                contentCell.setCellStyle(contentStyle);
                setCellValue(contentCell, formattedValue);
            }
        }
        return wb;
    }

    /**
     * 使用bean数据创建Excel
     *
     * @param titleMap 标题
     * @param data     数据
     * @param <T>      数据类型
     * @return
     */
    public static <T> Workbook createExcelFromBeanData(Map<String, String> titleMap, List<T> data) {
        List<Map<String, Object>> dataMap = Lists.newArrayList();
        for (T dataItem : data) {
            Map<String, Object> dataItemMap = Maps.newLinkedHashMap();
            for (String key : titleMap.keySet()) {
                try {
                    String convertedKey = key.replace(".", "").replace("%", "");
                    dataItemMap.put(key, PropertyUtils.getProperty(dataItem, convertedKey));
                } catch (IllegalAccessException e) {
                    // 忽略
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // 忽略
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    // 忽略
                    e.printStackTrace();
                }
            }
            dataMap.add(dataItemMap);
        }

        return createExcel(titleMap, dataMap);
    }


    /**
     * 仅支持部分类型读取
     *
     * @param cell  单元格
     * @param value 值
     */
    private static void setCellValue(Cell cell, Object value) {
        if (value instanceof Double) {
            Double d = (Double) value;
            cell.setCellValue(d);
        } else if (value instanceof Integer) {
            int i = (Integer) value;
            cell.setCellValue(i);
        } else if (value instanceof String) {
            String s = (String) value;
            cell.setCellValue(s);
        } else if (value instanceof Date) {
            Date d = (Date) value;
            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(d);
            cell.setCellValue(dateStr);
        } else if (value instanceof Boolean) {
            Boolean b = (Boolean) value;
            cell.setCellValue(b);
        } else if (value instanceof Double) {
            Double b = (Double) value;
            DecimalFormat fnum = new DecimalFormat("##0.00");
            String dd = fnum.format(b);
            cell.setCellValue(dd);
        } else {
            cell.setCellValue("");
        }
    }
}
