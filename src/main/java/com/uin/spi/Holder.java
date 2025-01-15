package com.uin.spi;

/**
 * 持有者 用于单个变量既可以做锁 又可以做值
 *
 * @param <T>
 */
public class Holder<T> {

  private T value;

  public T get() {
    return value;
  }

  public void set(T value) {
    this.value = value;
  }
}
