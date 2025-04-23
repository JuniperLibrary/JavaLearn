package com.uin.cache;

import cn.hutool.cache.*;
import cn.hutool.cache.impl.*;
import java.util.function.*;
import lombok.extern.slf4j.*;

@Slf4j
public class CacheManager {

  // 缓存容器（可根据需要设置多个缓存实例）
  private static final TimedCache<String, Object> timedCache = CacheUtil.newTimedCache(30 * 60 * 1000L); // 默认30分钟

  static {
    // 每5分钟清理一次过期缓存
    timedCache.schedulePrune(5 * 60 * 1000L);
  }

  /**
   * 手动设置缓存
   *
   * @param key       键
   * @param value     值
   * @param ttlMillis 过期时间（毫秒）
   */
  public static void put(String key, Object value, long ttlMillis) {
    timedCache.put(key, value, ttlMillis);
  }

  /**
   * 设置缓存，使用默认过期时间
   */
  public static void put(String key, Object value) {
    timedCache.put(key, value);
  }

  /**
   * 获取缓存
   *
   * @param key 键
   * @return 缓存中的值（如果存在且未过期）
   */
  public static Object get(String key) {
    return timedCache.get(key);
  }

  /**
   * 获取缓存（如果不存在则通过加载器加载并写入缓存）
   *
   * @param key       键
   * @param ttlMillis 缓存时间
   * @param loader    加载函数（可从DB或远程API中获取数据）
   */
  public static <T> T getOrLoad(String key, long ttlMillis, Function<String, T> loader) {
    Object value = timedCache.get(key, false);
    if (value == null) {
      T newVal = loader.apply(key);
      if (newVal != null) {
        timedCache.put(key, newVal, ttlMillis);
      }
      return newVal;
    }
    return (T) value;
  }

  /**
   * 清除某个key
   */
  public static void remove(String key) {
    timedCache.remove(key);
  }

  /**
   * 清空所有缓存
   */
  public static void clear() {
    timedCache.clear();
  }

  public static void main(String[] args) {
    String value = CacheManager.getOrLoad("market:Au99.99", 60 * 1000L, key -> {
      // 模拟加载逻辑，比如从数据库获取数据
      return "金价数据";
    });
    log.info(value);
  }
}
