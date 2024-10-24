package com.uin.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvToJson {

  public static void main(String[] args) throws Exception {
    CSVReader reader = new CSVReader(new FileReader("src/main/java/com/uin/file/2021-10-21.csv"));
    String[] headers = reader.readNext();
    List<Map<String, String>> data = new ArrayList<>();

    String[] line;
    while ((line = reader.readNext()) != null) {
      Map<String, String> row = new HashMap<>();
      for (int i = 0; i < headers.length; i++) {
        row.put(headers[i], line[i]);
      }
      data.add(row);
    }
    reader.close();

    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(data);
    log.info("CsvToJson : {}", json);
  }
}

