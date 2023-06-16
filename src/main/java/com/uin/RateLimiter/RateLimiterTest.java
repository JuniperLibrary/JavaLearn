package com.uin.RateLimiter;

import com.google.common.util.concurrent.RateLimiter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author dingchuan
 */
public class RateLimiterTest {

  /**
   * 创建一个具有指定稳定吞吐量的RateLimit，给定为“每秒许可”（通常称为QPS，每秒查询）。
   * 返回的RateLimit确保在任何给定的一秒钟内平均发出不超过perSecond的permissions，持续的请求在每一秒钟内顺利传播。
   * 当传入请求速率超过permitsPerSecond时，速率限制器将每（1.0/permitsPerSecond）秒释放一个许可。当速率限制器未使用时，将允许高达permitsPerSecond许可的突发，
   * 随后的请求将以permitsPrSecond的稳定速率平稳地受到限制
   */
  /**
   * 速率是每秒2个
   */
  private static final RateLimiter rateLimiter = RateLimiter.create(2.0);
  private static final RateLimiter rateLimiterByByte = RateLimiter.create(5000.0);

  public static void main(String[] args) {

  }

  public static void submitTasks(List tasks, Executor executor) {
    for (Object task : tasks) {
      // QPS 2
      rateLimiter.acquire(); // 也许需要等待
      executor.execute((Runnable) task);
    }
  }

  /**
   * 希望以每秒5kb的速率处理它。可以通过要求每个字节代表一个许可，然后每秒5000个许可证 5kb=5*1000=5000byte
   */
  public static void submitByteTasks(byte[] bytes) throws IOException {
    rateLimiterByByte.acquire(bytes.length);

  }

  public void networkService() {

  }
}
