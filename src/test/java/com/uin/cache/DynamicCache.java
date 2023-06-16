package com.uin.cache;

import cn.hutool.json.JSONUtil;
import com.google.common.cache.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor
@Slf4j
public class DynamicCache {

  private final CacheLoader<String, Animal> animalCacheLoader = new CacheLoader<>() {
    // 如果找不到元素，回调用这里
    @Override
    public Animal load(String key) throws Exception {
      return null;
    }
  };

  @Test
  public void testCache() {
    LoadingCache<String, Animal> cacheBuilder = CacheBuilder.newBuilder()
        .maximumSize(1000L)
        .expireAfterWrite(3, TimeUnit.SECONDS)
        .removalListener(new MycacheListener())
        .build(animalCacheLoader);

    cacheBuilder.put("1", new Animal("dog", 1));
    cacheBuilder.put("2", new Animal("cat", 2));
    cacheBuilder.put("3", new Animal("fox", 3));

    try {
      // 手动失效
      cacheBuilder.invalidate("2");
      log.info("Animal:{}", JSONUtil.parse(cacheBuilder.get("1")));
      Thread.sleep(4 * 1000);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 缓存监听器 -- 失效的监听器
   */
  public class MycacheListener implements RemovalListener<String, Animal> {

    @Override
    public void onRemoval(RemovalNotification<String, Animal> notification) {
      log.info("缓存到了过期时间，触发了onRemoval");
      String reason = String.format("key=%s,value=%s,reason=%s", notification.getKey(), notification.getValue(),
          notification.getCause());
      log.info(reason);
      log.info("key:{},value:{},cause:{}", notification.getKey(), notification.getValue(), notification.getCause());
    }
  }

  @Data
  class Animal {

    private String name;
    private Integer age;

    @Override
    public String toString() {
      return "Animal{" +
          "name='" + name + '\'' +
          ", age=" + age +
          '}';
    }

    public Animal(String name, Integer age) {
      this.name = name;
      this.age = age;
    }
  }
}
