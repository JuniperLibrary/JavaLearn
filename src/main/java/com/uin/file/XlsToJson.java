package com.uin.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class XlsToJson {
  public static void main(String[] args) throws Exception {
    FileInputStream file = new FileInputStream(new File("data.xls"));
    Workbook workbook = WorkbookFactory.create(file);
    Sheet sheet = workbook.getSheetAt(0);
    List<Map<String, Object>> data = new ArrayList<>();

    Row headerRow = sheet.getRow(0);
    List<String> headers = new ArrayList<>();
    for (Cell cell : headerRow) {
      headers.add(cell.getStringCellValue());
    }

    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      Row row = sheet.getRow(i);
      Map<String, Object> rowData = new HashMap<>();
      for (int j = 0; j < headers.size(); j++) {
        Cell cell = row.getCell(j);
        rowData.put(headers.get(j), cell.toString());
      }
      data.add(rowData);
    }

    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(data);
    System.out.println(json);
    workbook.close();
  }
}

