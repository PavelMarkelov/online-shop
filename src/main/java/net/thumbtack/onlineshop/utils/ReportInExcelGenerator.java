package net.thumbtack.onlineshop.utils;

import net.thumbtack.onlineshop.dto.Response.ProductInfoDtoResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class ReportInExcelGenerator {

    private final static String[] COLUMNs = {"Id", "Quantity", "Name", "Price", "Categories"};
    private final static String SHEET = "Product report";

    public static byte[] generateData(List<ProductInfoDtoResponse> products)
            throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            Sheet sheet = workbook.createSheet(SHEET);

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.DARK_GREEN.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
                cell.setCellStyle(headerCellStyle);
            }

            CellStyle bodyCellStyle = workbook.createCellStyle();
            bodyCellStyle.setAlignment(HorizontalAlignment.RIGHT);
            int rowIdx = 1;
            for (ProductInfoDtoResponse product : products) {
                Row bodyRow = sheet.createRow(rowIdx++);

                Cell cell = bodyRow.createCell(0);
                cell.setCellStyle(bodyCellStyle);
                cell.setCellValue(product.getId());

                cell = bodyRow.createCell(1);
                cell.setCellStyle(bodyCellStyle);
                cell.setCellValue(product.getCount());

                cell = bodyRow.createCell(2);
                cell.setCellStyle(bodyCellStyle);
                cell.setCellValue(product.getName());

                cell = bodyRow.createCell(3);
                cell.setCellStyle(bodyCellStyle);
                cell.setCellValue(product.getPrice());

                String categories = String.join(", ", product.getCategories());
                cell = bodyRow.createCell(4);
                cell.setCellStyle(bodyCellStyle);
                cell.setCellValue(categories);
            }
            for (int col = 0; col < COLUMNs.length; col++)
                sheet.autoSizeColumn(col);
            workbook.write(out);
            return out.toByteArray();
        }
    }
}
