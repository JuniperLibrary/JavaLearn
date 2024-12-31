package com.uin.cache;

import java.util.*;
import java.util.function.*;
import lombok.extern.slf4j.*;

@Slf4j
public class CacheSystem<K, V> {

  // 内部缓存存储
  private final Map<K, V> cache = new HashMap<>();

  /**
   * 从缓存中获取值。如果缓存中不存在，则通过 Supplier 生成值并存入缓存。
   *
   * @param key      缓存的键
   * @param supplier 用于生成值的 Supplier
   * @return 缓存中的值
   */
  public V get(K key, Supplier<V> supplier) {
    return cache.computeIfAbsent(key, k -> {
      V value = supplier.get();
      log.info("Value generated for key: {}", key);
      return value;
    });
  }

  /**
   * 显示当前缓存内容
   */
  public void showCache() {
    log.info("Cache content: {}", cache);
  }

  public static void main(String[] args) {
    // 创建一个缓存系统
    CacheSystem<String, String> cacheSystem = new CacheSystem<>();

    // 使用 Supplier 延迟生成值
    Supplier<String> valueSupplier = () -> {
      log.info("Generating value...");
      return "GeneratedValue";
    };

    // 获取值（第一次时会通过 Supplier 生成值）
    String value1 = cacheSystem.get("key1", valueSupplier);
    log.info("Fetched value: {}", value1);

    // 再次获取相同的键，应该从缓存中获取值
    String value2 = cacheSystem.get("key1", valueSupplier);
    log.info("Fetched value: {}", value2);

    // 显示缓存内容
    cacheSystem.showCache();
  }
}
