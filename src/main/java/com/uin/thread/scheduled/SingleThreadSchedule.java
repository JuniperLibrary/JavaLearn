package com.uin.thread.scheduled;

import cn.hutool.core.thread.ThreadUtil;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SingleThreadSchedule {

  public static void main(String[] args) {
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(
        ThreadUtil.newNamedThreadFactory("browser-hearbeat-pool", false));

    executor.scheduleAtFixedRate(() -> log.info("scheduleAtFixedRate"), 1000L, 10000L, TimeUnit.MICROSECONDS);
    executor.scheduleWithFixedDelay(() -> log.info("scheduleWithFixedDelay"), 2000, 2000, TimeUnit.MICROSECONDS);
  }

}
