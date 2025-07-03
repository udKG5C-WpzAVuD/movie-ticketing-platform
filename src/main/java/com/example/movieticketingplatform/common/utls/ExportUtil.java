package com.example.movieticketingplatform.common.utls;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExportUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 将 List<T> 导出为 Excel 字节数组
     * @param dataList 要导出的数据列表
     * @param headers 表头，Map<字段名, 显示名>
     * @param <T> 数据类型
     * @return Excel 文件的字节数组
     * @throws
     */
    public static <T> byte[] exportExcel(List<T> dataList, Map<String, String> headers) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // 创建标题行
        Row headerRow = sheet.createRow(0);
        int col = 0;
        for (String headerName : headers.values()) {
            Cell cell = headerRow.createCell(col++);
            cell.setCellValue(headerName);
        }

        // 填充数据
        int rowNum = 1;
        for (T data : dataList) {
            Row row = sheet.createRow(rowNum++);
            col = 0;
            for (String fieldName : headers.keySet()) {
                try {
                    Field field = data.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true); // 允许访问私有字段
                    Object value = field.get(data);
                    Cell cell = row.createCell(col++);
                    setCellValue(cell, value);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    // 字段不存在或访问异常，跳过或记录日志
                    Cell cell = row.createCell(col++);
                    cell.setCellValue(""); // 填充空字符串
                    System.err.println("Warning: Field '" + fieldName + "' not found or accessible in " + data.getClass().getSimpleName());
                }
            }
        }

        // 自动调整列宽
        for (int i = 0; i < headers.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return bos.toByteArray();
    }

    // 根据数据类型设置单元格值
    private static void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof LocalDate) {
            cell.setCellValue(((LocalDate) value).format(DATE_FORMATTER));
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue(((LocalDateTime) value).format(DATETIME_FORMATTER));
        } else {
            cell.setCellValue(value.toString());
        }
    }


    /**
     * 将 List<T> 导出为 CSV 字节数组
     * @param dataList 要导出的数据列表
     * @param headers 表头，Map<字段名, 显示名>
     * @param <T> 数据类型
     * @return CSV 文件的字节数组
     */
    public static <T> byte[] exportCsv(List<T> dataList, Map<String, String> headers) {
        StringBuilder csvContent = new StringBuilder();

        // 添加表头
        String headerLine = headers.values().stream().collect(Collectors.joining(","));
        csvContent.append(headerLine).append("\n");

        // 添加数据
        for (T data : dataList) {
            StringBuilder rowBuilder = new StringBuilder();
            boolean firstField = true;
            for (String fieldName : headers.keySet()) {
                if (!firstField) {
                    rowBuilder.append(",");
                }
                try {
                    Field field = data.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object value = field.get(data);
                    rowBuilder.append(formatCsvValue(value));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    rowBuilder.append(""); // 填充空字符串
                    System.err.println("Warning: Field '" + fieldName + "' not found or accessible in " + data.getClass().getSimpleName());
                }
                firstField = false;
            }
            csvContent.append(rowBuilder.toString()).append("\n");
        }

        return csvContent.toString().getBytes(StandardCharsets.UTF_8);
    }

    // CSV 值格式化，处理逗号和双引号
    private static String formatCsvValue(Object value) {
        if (value == null) {
            return "";
        }
        String stringValue;
        if (value instanceof LocalDate) {
            stringValue = ((LocalDate) value).format(DATE_FORMATTER);
        } else if (value instanceof LocalDateTime) {
            stringValue = ((LocalDateTime) value).format(DATETIME_FORMATTER);
        } else {
            stringValue = value.toString();
        }

        // 如果值中包含逗号、双引号或换行符，则用双引号包围，并对内部的双引号进行转义（变成两个双引号）
        if (stringValue.contains(",") || stringValue.contains("\"") || stringValue.contains("\n")) {
            return "\"" + stringValue.replace("\"", "\"\"") + "\"";
        }
        return stringValue;
    }
}
