package com.uin.spi;

import cn.hutool.json.*;

public class JSONSerializer implements Serializer{

  @Override
  public byte[] serialize(Object object) {
    return JSONUtil.toJsonStr(object).getBytes();
  }
}
