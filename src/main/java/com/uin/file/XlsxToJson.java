package com.uin.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

@Slf4j
public class XlsxToJson {

  public static void main(String[] args) {
    try {
      FileInputStream file = new FileInputStream(new File("src/main/java/com/uin/file/jm456265.xlsx"));
      Workbook workbook = WorkbookFactory.create(file);
      Sheet sheet = workbook.getSheetAt(0);
      List<Map<String, Object>> data = new ArrayList<>();

      // 手动选择你需要作为表头的行，比如从第二行开始
      Row headerRow = sheet.getRow(1);  // 假设从第二行开始读取表头
      List<String> headers = new ArrayList<>();
      for (Cell cell : headerRow) {
        headers.add(cell.getStringCellValue());
      }

      // 从第三行开始读取数据
      for (int i = 2; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        Map<String, Object> rowData = new HashMap<>();
        for (int j = 0; j < headers.size(); j++) {
          Cell cell = row.getCell(j);
          rowData.put(headers.get(j), cell != null ? cell.toString() : "");  // 添加null检查
        }
        data.add(rowData);
      }

      ObjectMapper objectMapper = new ObjectMapper();
      String json = objectMapper.writeValueAsString(data);
      log.info("XlsxToJson : {}", json);
      workbook.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

