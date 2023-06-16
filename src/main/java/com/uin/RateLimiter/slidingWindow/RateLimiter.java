package com.uin.RateLimiter.slidingWindow;

import java.time.Instant;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.LongPredicate;

/**
 * @author dingchuan
 */
public class RateLimiter {
  private final long timeIntervalInSeconds;
  private final long[] permits;
  private int head;
  private int size;

  public RateLimiter(int timeIntervalInSeconds, int permitsPerSec) {
    if (permitsPerSec > 0 && timeIntervalInSeconds > 0) {
      this.permits = new long[permitsPerSec];
      this.head = 0;
      this.size = 0;
      this.timeIntervalInSeconds = (long)timeIntervalInSeconds * 1000L;
    } else {
      throw new IllegalArgumentException("permitsPerSec must be greater than 0");
    }
  }

  public boolean tryAcquire(ReentrantLock reentrantLock, int count) {
    while(!reentrantLock.tryLock()) {
      Thread.yield();
    }

    boolean var11;
    try {
      long now = Instant.now().toEpochMilli();
      boolean result = false;

      for(int i = 0; i < count; ++i) {
        if (this.size == 0) {
          result = true;
        } else if (now > this.peek() + this.timeIntervalInSeconds) {
          result = true;
          this.removeWhile((time) -> {
            return time <= now - this.timeIntervalInSeconds;
          });
        } else {
          result = this.size < this.permits.length;
        }

        if (result) {
          System.out.println(now);
          this.offer(now);
        }

        if (!result) {
          boolean var7 = result;
          return var7;
        }
      }

      var11 = result;
    } finally {
      reentrantLock.unlock();
    }

    return var11;
  }

  private long peek() {
    this.ensureNotEmpty();
    return this.permits[this.head];
  }

  private void offer(long e) {
    int next = (this.head + this.size) % this.permits.length;
    this.permits[next] = e;
    if (this.size < this.permits.length) {
      ++this.size;
    } else {
      ++this.head;
    }

  }

  private void poll() {
    this.ensureNotEmpty();
    this.head = (this.head + 1) % this.permits.length;
    --this.size;
  }

  private void ensureNotEmpty() {
    if (this.size == 0) {
      throw new IllegalStateException("Permits queue is empty.");
    }
  }

  private void removeWhile(LongPredicate predicate) {
    while(this.size > 0 && predicate.test(this.peek())) {
      this.poll();
    }

  }


  public static void main(String[] args) {

  }
}
