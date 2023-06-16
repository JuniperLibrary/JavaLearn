package com.uin.thread.pool;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.thread.ThreadUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author dingchuan
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HutoolThreadBuilder {

  public Future<?> asyncThread(Integer threadCount) {
    if (threadCount == null || threadCount <= 0) {
      threadCount = 1;
    }

    StringBuilder msg = new StringBuilder();

    Future<?> future = null;
    for (int i = 0; i < threadCount; i++) {
      String threadName = "async-thread-" + i;
      if (!checkThreadAlive(threadName)) {
        // 开启异步线程
        future = ThreadUtil.execAsync(() -> {
          StopWatch watch = new StopWatch();
          watch.start(threadName + "sync");
          Thread.currentThread().setName(threadName);
          // todo task
          watch.stop();
          log.info("异步任务执行完成:{},耗时:{}s", threadName, watch.getTotalTimeMillis() / 1000);
        });
        msg.append("async 线程").append(threadName).append("开始执行...");
      } else {
        msg.append("async 线程").append(threadName).append("等待同步...");
      }
    }
    log.info("async status:{}", msg);
    return future;
  }


  /**
   * 检查是否开启，防止重复执行
   *
   * @param threadName
   * @return
   */
  private boolean checkThreadAlive(String threadName) {
    Thread[] threads = ThreadUtil.getThreads();
    long count = Arrays.stream(threads).filter(item -> item.getName().equals(threadName)).count();
    return count > 0;
  }
}
