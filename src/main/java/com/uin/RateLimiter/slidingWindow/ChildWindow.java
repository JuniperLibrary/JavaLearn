package com.uin.RateLimiter.slidingWindow;

public class ChildWindow {

  /**
   * 子窗口的时间跨度
   */
  private long childWindowTimeSpan;
  /**
   * 开始时间
   */
  private long startTime;
  /**
   * 子窗口的度量
   */
  private ChildWindowMetric childWindowMetric;

  public ChildWindow(Long childWindowTimeSpan, Long startTime, ChildWindowMetric childWindowMetric) {
    this.childWindowTimeSpan = childWindowTimeSpan;
    this.startTime = startTime;
    this.childWindowMetric = childWindowMetric;
  }

  public long getChildWindowTimeSpan() {
    return this.childWindowTimeSpan;
  }

  public long getStartTime() {
    return this.startTime;
  }

  public ChildWindowMetric getChildWindowMetric() {
    return this.childWindowMetric;
  }

  public void setChildWindowTimeSpan(long childWindowTimeSpan) {
    this.childWindowTimeSpan = childWindowTimeSpan;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public void setChildWindowMetric(ChildWindowMetric childWindowMetric) {
    this.childWindowMetric = childWindowMetric;
  }

  /**
   * 重写equals方法
   *
   * @param o
   * @return
   */
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof ChildWindow)) {
      return false;
    } else {
      ChildWindow other = (ChildWindow) o;
      if (!other.canEqual(this)) {
        return false;
      } else if (this.getChildWindowTimeSpan() != other.getChildWindowTimeSpan()) {
        return false;
      } else if (this.getStartTime() != other.getStartTime()) {
        return false;
      } else {
        Object this$childWindowMetric = this.getChildWindowMetric();
        Object other$childWindowMetric = other.getChildWindowMetric();
        if (this$childWindowMetric == null) {
          if (other$childWindowMetric == null) {
            return true;
          }
        } else if (this$childWindowMetric.equals(other$childWindowMetric)) {
          return true;
        }

        return false;
      }
    }
  }

  protected boolean canEqual(Object other) {
    return other instanceof ChildWindow;
  }

  public int hashCode() {
    int result = 1;
    long $childWindowTimeSpan = this.getChildWindowTimeSpan();
    result = result * 59 + (int) ($childWindowTimeSpan >>> 32 ^ $childWindowTimeSpan);
    long $startTime = this.getStartTime();
    result = result * 59 + (int) ($startTime >>> 32 ^ $startTime);
    Object $childWindowMetric = this.getChildWindowMetric();
    result = result * 59 + ($childWindowMetric == null ? 43 : $childWindowMetric.hashCode());
    return result;
  }

  public String toString() {
    long var10000 = this.getChildWindowTimeSpan();
    return "ChildWindow(childWindowTimeSpan=" + var10000 + ", startTime=" + this.getStartTime() + ", childWindowMetric="
        + this.getChildWindowMetric() + ")";
  }
}
