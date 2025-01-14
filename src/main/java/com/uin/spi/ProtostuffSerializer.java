package com.uin.spi;

import com.dyuproject.protostuff.*;
import com.dyuproject.protostuff.runtime.*;

public class ProtostuffSerializer implements Serializer{

  private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

  @Override
  public byte[] serialize(Object object) {
    Schema schema = RuntimeSchema.getSchema(object.getClass());
    return ProtostuffIOUtil.toByteArray(object,schema,BUFFER);
  }
}
