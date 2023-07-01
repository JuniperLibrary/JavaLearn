package com.uin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;

public class main {

  public static void main(String[] args) {
    Map<String, String> old = new HashMap<>();
    Map<String, String> news = new HashMap<>();
    news.put("BidResidualZScore", "2.345");
    news.put("OfrResidualZScore", "4.345");
    news.put("MidResidualZScore", "1.345");

    old.put("BidResidualZScore", "2.345");
    old.put("OfrResidualZScore", "7.345");
    old.put("MidResidualZScore", "1.345");
//    List<Boolean> bools = new ArrayList<>();
//    int i=0;
//    for (Map.Entry<String, String> oldentry : old.entrySet()) {
//      for (Map.Entry<String, String> newEntry : news.entrySet()) {
//        if (oldentry.getKey().equals(newEntry.getKey())) {
//          bools.add(oldentry.getValue().equals(newEntry.getValue()));
//
//        }
//      }
//    }
//
//    System.out.println(old.equals(news));
//    try {
//      MessageDigest digest = MessageDigest.getInstance("SHA-256");
//      digest.digest();
//    } catch (NoSuchAlgorithmException e) {
//      throw new RuntimeException(e);
//    }
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
      objectOutputStream.writeObject(old);
      objectOutputStream.close();
      byte[] bytes = byteArrayOutputStream.toByteArray();

      objectOutputStream.writeObject(news);
      objectOutputStream.close();
      byte[] bytes1 = byteArrayOutputStream.toByteArray();

      String s = DigestUtils.sha256Hex(bytes);
      System.out.println(s);
      String s1 = DigestUtils.sha256Hex(bytes1);
      System.out.println(s1);

      System.out.println(s.equals(s1));
      System.out.println(old.equals(news));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }


  }

}
