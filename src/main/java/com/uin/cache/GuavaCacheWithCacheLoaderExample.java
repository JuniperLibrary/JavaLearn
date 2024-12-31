package com.uin.cache;

import com.google.common.cache.*;
import java.util.concurrent.*;
import lombok.extern.slf4j.*;

@Slf4j
public class GuavaCacheWithCacheLoaderExample {
  public static void main(String[] args) {
    // 创建 LoadingCache，指定 CacheLoader 提供缓存缺失时的加载逻辑
    LoadingCache<String, String> cache = CacheBuilder.newBuilder()
        .expireAfterWrite(5, TimeUnit.SECONDS)
        .build(new CacheLoader<>() {
          @Override
          public String load(String key) {
            log.info("Generating value for: {}" , key);
            return "GeneratedValue";
          }
        });

    // 获取值
    try {
      String value1 = cache.get("key1");
      log.info("Fetched value: {}" , value1);

      Thread.sleep(6000);

      String value2 = cache.get("key1");
      log.info("Fetched value after expiration: {}" , value2);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
