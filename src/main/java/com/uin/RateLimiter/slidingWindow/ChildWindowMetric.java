package com.uin.RateLimiter.slidingWindow;

import java.util.Arrays;
import java.util.concurrent.atomic.LongAdder;

/**
 * 子窗口的长度
 */
public class ChildWindowMetric {

  /**
   * 子窗口的长度
   */
  private LongAdder[] longAdders;

  public ChildWindowMetric(int size, int locationInLongAdderArray) {
    this.longAdders = new LongAdder[size];

    for (int i = 0; i < size; ++i) {
      this.longAdders[i] = new LongAdder();
    }

    this.longAdders[locationInLongAdderArray].add(1L);
  }

  public LongAdder[] getLongAdders() {
    return this.longAdders;
  }

  public void setLongAdders(LongAdder[] longAdders) {
    this.longAdders = longAdders;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof ChildWindowMetric)) {
      return false;
    } else {
      ChildWindowMetric other = (ChildWindowMetric) o;
      if (!other.canEqual(this)) {
        return false;
      } else {
        return Arrays.deepEquals(this.getLongAdders(), other.getLongAdders());
      }
    }
  }

  protected boolean canEqual(Object other) {
    return other instanceof ChildWindowMetric;
  }

  public int hashCode() {
    int result = 1;
    result = result * 59 + Arrays.deepHashCode(this.getLongAdders());
    return result;
  }

  public String toString() {
    return "ChildWindowMetric(longAdders=" + Arrays.deepToString(this.getLongAdders()) + ")";
  }
}
