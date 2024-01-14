package com.uin.RateLimiter.slidingWindow.demo;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定窗口算法
 *
 * @author dingchuan
 */
public class RateLimiterSimpleWindow {

  /**
   * 阈值
   */
  private static Integer QPS = 2;
  /**
   * 时间窗口（毫秒）
   */
  private static long TIME_WINDOWS = 1000;
  /**
   * 计数器
   */
  private static final AtomicInteger REQ_COUNT = new AtomicInteger();

  private static long START_TIME = System.currentTimeMillis();

  public synchronized static boolean tryAcquire() {
    if ((System.currentTimeMillis() - START_TIME) > TIME_WINDOWS) {
      REQ_COUNT.set(0);
      START_TIME = System.currentTimeMillis();
    }
    return REQ_COUNT.incrementAndGet() <= QPS;
  }

  public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 10; i++) {
      Thread.sleep(400);
      LocalTime now = LocalTime.now();
      if (!tryAcquire()) {
        System.out.println(now + " 被限流");
      } else {
        System.out.println(now + " 做点什么");
      }
    }
  }
}
