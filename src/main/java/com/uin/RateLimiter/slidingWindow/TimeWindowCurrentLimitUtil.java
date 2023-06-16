package com.uin.RateLimiter.slidingWindow;

import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 时间窗口限制工具
 * @author dingchuan
 */
public class TimeWindowCurrentLimitUtil {

  private static final Logger log = LoggerFactory.getLogger(TimeWindowCurrentLimitUtil.class);
  private static final long WINDOW_TIME_SPAN = 1000L;
  private static AtomicReferenceArray<Object> CHILD_WINDOWS;
  private static final ReentrantLock RESET_WINDOW_REENTRANT_LOCK = new ReentrantLock();

  public TimeWindowCurrentLimitUtil() {
  }

  /**
   * 获取子窗口
   *
   * @param maxQps 每秒通过的个数
   * @param precision
   * @param longAdderArraySize 子窗口的长度
   * @param locationInLongAdderArray 在子窗口的位置
   * @param reentrantLock
   * @return
   */
  public static boolean getchildWindows(int maxQps, int precision, int longAdderArraySize, int locationInLongAdderArray,
      ReentrantLock reentrantLock) {
    while (true) {
      if (CHILD_WINDOWS == null) {
        if (!RESET_WINDOW_REENTRANT_LOCK.tryLock()) {
          Thread.yield();
          continue;
        }

        try {
          if (CHILD_WINDOWS == null) {
            CHILD_WINDOWS = new AtomicReferenceArray(precision);
          }
        } finally {
          RESET_WINDOW_REENTRANT_LOCK.unlock();
        }
      }

      long currentTime = System.currentTimeMillis();
      int index = calculateIndex(currentTime, (long) precision);
      long startTime = calculateStartTime(currentTime, (long) precision);
      ChildWindow oldChildWindow = (ChildWindow) CHILD_WINDOWS.get(index);
      boolean var11;
      if (oldChildWindow == null) {
        if (CHILD_WINDOWS.compareAndSet(index, (Object) null, new ChildWindow(1000L / (long) precision, startTime,
            new ChildWindowMetric(longAdderArraySize, locationInLongAdderArray)))) {
          if (RESET_WINDOW_REENTRANT_LOCK.tryLock()) {
            try {
              var11 = isPass(currentTime, locationInLongAdderArray, maxQps);
            } finally {
              RESET_WINDOW_REENTRANT_LOCK.unlock();
            }

            return var11;
          }

          Thread.yield();
        } else {
          Thread.yield();
        }
      } else if (startTime == oldChildWindow.getStartTime()) {
        if (reentrantLock.tryLock()) {
          oldChildWindow.getChildWindowMetric().getLongAdders()[locationInLongAdderArray].add(1L);

          try {
            var11 = isPass(currentTime, locationInLongAdderArray, maxQps);
          } finally {
            reentrantLock.unlock();
          }

          return var11;
        }

        Thread.yield();
      } else if (startTime > oldChildWindow.getStartTime()) {
        if (RESET_WINDOW_REENTRANT_LOCK.tryLock()) {
          try {
            resetWindowTo(oldChildWindow, startTime, locationInLongAdderArray);
            var11 = isPass(currentTime, locationInLongAdderArray, maxQps);
          } finally {
            RESET_WINDOW_REENTRANT_LOCK.unlock();
          }

          return var11;
        }

        Thread.yield();
      } else if (startTime < oldChildWindow.getStartTime()) {
        log.error("please check server system time!");
      }
    }
  }

  /**
   * 是否通过
   *
   * @param currentTime
   * @param locationInLongAdderArray
   * @param maxQps
   * @return
   */
  private static boolean isPass(Long currentTime, int locationInLongAdderArray, int maxQps) {
    int currentPassTimes = 0;

    for (int i = 0; i < CHILD_WINDOWS.length(); ++i) {
      ChildWindow childWindow = (ChildWindow) CHILD_WINDOWS.get(i);
      if (childWindow != null && !childWindowIsExpire(currentTime, childWindow)) {
        ChildWindowMetric childWindowMetric = childWindow.getChildWindowMetric();
        LongAdder longAdder = childWindowMetric.getLongAdders()[locationInLongAdderArray];
        currentPassTimes += longAdder.intValue();
      }
    }

    return currentPassTimes <= maxQps;
  }

  /**
   * 子窗口是否时间是否过期
   *
   * @param currentTime
   * @param childWindow
   * @return
   */
  private static boolean childWindowIsExpire(long currentTime, ChildWindow childWindow) {
    return currentTime - childWindow.getStartTime() > 1000L;
  }

  /**
   * 窗口重置
   *
   * @param oldChildWindow
   * @param startTime
   * @param longAdderArraySize
   */
  private static void resetWindowTo(ChildWindow oldChildWindow, long startTime, int longAdderArraySize) {
    oldChildWindow.setStartTime(startTime);
    LongAdder[] longAdders = oldChildWindow.getChildWindowMetric().getLongAdders();
    LongAdder[] var5 = longAdders;
    int var6 = longAdders.length;

    for (int var7 = 0; var7 < var6; ++var7) {
      LongAdder longAdder = var5[var7];
      longAdder.reset();
    }

    LongAdder longAdder = oldChildWindow.getChildWindowMetric().getLongAdders()[longAdderArraySize];
    longAdder.add(1L);
  }

  /**
   * 计算开始时间
   *
   * @param currentTime
   * @param precision
   * @return
   */
  private static long calculateStartTime(long currentTime, long precision) {
    int l = (int) (1000L / precision);
    // 用当前时间 减去 (当前时间和目前所在的时间点 取余) 得到子窗口的开始时间
    return currentTime - currentTime % (long) l;
  }

  /**
   * 计算在子窗口的位置
   *
   * @param currentTime
   * @param precision
   * @return
   */
  private static int calculateIndex(long currentTime, long precision) {
    long timeId = currentTime / (1000L / precision);
    // 将当前时间和子窗口的长度 取余 得到当前时间点在子窗口的位置
    return (int) (timeId % (long) CHILD_WINDOWS.length());
  }
}
