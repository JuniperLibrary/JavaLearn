package com.uin.spi;

import java.util.*;
import lombok.extern.slf4j.*;

@Slf4j
public class SpiMain {

  public static void main(String[] args) {
    ServiceLoader<Serializer> load = ServiceLoader.load(Serializer.class);
    Iterator<Serializer> iterator = load.iterator();
    while (iterator.hasNext()){
      Serializer next = iterator.next();
      log.info("SPI : {}",next.getClass().getName());
    }
  }
}
