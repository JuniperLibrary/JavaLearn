package com.uin.spi;

@SPI
public interface Serializer {

  byte[] serialize(Object object);
}
