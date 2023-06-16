package com.uin.RateLimiter.config;

import com.google.common.util.concurrent.RateLimiter;
import com.uin.RateLimiter.ResponseEnum;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author dingchuan
 */
@Slf4j
@Component
public class RateLimitInterceptor extends AbstrtactInteceptor {
  // 定义了QPS为1的全局限流器
  private static final RateLimiter rateLimiter = RateLimiter.create(1);

  @Override
  protected ResponseEnum preFilter(HttpServletRequest request) {
    // 尝试获取令牌
    if (!rateLimiter.tryAcquire()) {
      log.info("限流中....");
      return ResponseEnum.RATE_LIMIT;
    }
    log.info("请求成功");
    return ResponseEnum.SUCCESS;
  }
}
