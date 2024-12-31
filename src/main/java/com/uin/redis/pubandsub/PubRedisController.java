package com.uin.redis.pubandsub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PubRedisController {

  private final RedisTemplate redisTemplate;

  @GetMapping("/test")
  public void test() {
    redisTemplate.convertAndSend("test", "x");
  }
}
