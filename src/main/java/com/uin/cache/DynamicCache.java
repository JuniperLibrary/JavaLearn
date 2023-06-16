package com.uin.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dingchuan
 */
@Slf4j
@RequiredArgsConstructor
public class DynamicCache {

  private final Cache<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
      .maximumSize(1000L)
      .expireAfterWrite(10, TimeUnit.MINUTES)
      .removalListener(notification -> log.info("DynamicCache,preCheckTaskCache,key<{}> was removed, because of {}",
          notification.getKey(), notification.getCause()))
      .build();

  public Object getCacheBuilder(String key) {
    return cacheBuilder.getIfPresent(key);
  }

  public void doInit() {
    for (int i = 0,j = 0; i < 10; i++) {
      cacheBuilder.put(i,j);
    }
  }

  /**
   * 缓存回收: 基于容量回收、定时回收和基于引用回收。
   *
   * 基于容量的回收
   */
  public void testCache(){

  }


}
