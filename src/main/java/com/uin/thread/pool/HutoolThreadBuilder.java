package com.uin.thread.pool;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.thread.ThreadUtil;
import java.util.concurrent.Future;
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
        future = ThreadUtil.execAsync(() -> {
          StopWatch watch = new StopWatch();
          watch.start(threadName + "sync");
          Thread.currentThread().setName(threadName);
          importSyncTaskForQueue();
          watch.stop();
          log.info("异步任务执行完成:{},耗时:{}ms", threadName, watch.getTotalTimeMillis());
        });
        msg.append("async 线程").append(threadName).append("开始执行...");
      } else {
        msg.append("async 线程").append(threadName).append("等待同步...");
      }
    }
    log.info("async status:{}", msg);
    return future;
  }

  private void importSyncTaskForQueue() {

  }

  private boolean checkThreadAlive(String threadName) {
    return false;
  }
}
