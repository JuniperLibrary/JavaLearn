package com.uin.file;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class DmpToJson {
  public static void main(String[] args) throws Exception {
    BufferedReader reader = new BufferedReader(new FileReader("data.dmp"));
    List<Map<String, String>> data = new ArrayList<>();
    String line;

    while ((line = reader.readLine()) != null) {
      String[] parts = line.split(","); // 假设以逗号分隔
      Map<String, String> row = new HashMap<>();
      for (int i = 0; i < parts.length; i++) {
        row.put("Column" + i, parts[i]);
      }
      data.add(row);
    }
    reader.close();

    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(data);
    System.out.println(json);
  }
}
