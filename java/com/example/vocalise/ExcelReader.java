package com.example.vocalise;

import android.content.Context;
import android.content.res.AssetManager;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


import java.io.IOException;
import java.io.InputStream;


public class ExcelReader {

    public static String[][] readExcelFile(Context context, String fileName, String sheetName) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(fileName);

        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheet(sheetName);
        int rowsCount = sheet.getLastRowNum() + 1;
        int columnsCount = sheet.getRow(0).getLastCellNum();
        String[][] data = new String[rowsCount][columnsCount];

        for (int i = 0; i < rowsCount; i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j < columnsCount; j++) {
                Cell cell = row.getCell(j);
                data[i][j] = cell.toString();
            }
        }

        return data;
    }
}

