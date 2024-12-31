package com.uin.cache;

import com.google.common.cache.*;
import java.util.concurrent.*;
import java.util.function.*;
import lombok.extern.slf4j.*;

@Slf4j
public class GuavaCacheWithSupplierExample {

  public static void main(String[] args) {
    // 创建 Guava Cache，配置缓存过期时间为 5 秒
    Cache<String, String> cache = CacheBuilder.newBuilder()
        .expireAfterWrite(5, TimeUnit.SECONDS)
        .build();

    // 使用 Supplier 实现延迟加载
    Supplier<String> valueSupplier = () -> {
      log.info("Generating value...");
      return "GeneratedValue";
    };

    // 获取缓存中的值，如果不存在则使用 Supplier 提供值
    String key = "key1";
    String value1 = cache.getIfPresent(key);
    if (value1 == null) {
      value1 = valueSupplier.get();
      cache.put(key, value1);
    }
    log.info("Fetched value: {}" , value1);

    // 再次获取相同的键，应该从缓存中直接返回值
    String value2 = cache.getIfPresent(key);
    log.info("Fetched value: {}" , value2);

    // 等待缓存过期后再次获取
    try {
      Thread.sleep(6000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    String value3 = cache.getIfPresent(key);
    if (value3 == null) {
      value3 = valueSupplier.get();
      cache.put(key, value3);
    }
    log.info("Fetched value after expiration: {}" , value3);
  }
}
