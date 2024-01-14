package com.uin.cache.asyncupdatecache;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 使用线程池异步刷新本地缓存
 *
 * @author dingchuan
 */
public class ThreadPoolUpdate {

  private ThreadPoolExecutor threadPoolExecutor;

  public ThreadPoolUpdate() {
    /*executorService = Executors.newFixedThreadPool(3);*/
    /**
     * 核心线程数
     */
    int corePoolSize = 5;
    /**
     * 最大线程数
     */
    int maximumPoolSize = 10;
    /**
     * 线程空闲时间（秒）
     */
    long keepAliveTime = 60;
    threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
        TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
  }

  public void refershCacheAsync() {
    /*executorService.submit(() -> {
      // 执行缓存刷新的操作
      System.out.println(Thread.currentThread().getName() + "异步缓存刷新任务完成执行");
    });*/
    threadPoolExecutor.execute(()->{
      // 执行缓存刷新的操作
      System.out.println(Thread.currentThread().getName() + "异步缓存刷新任务完成执行");
    });
  }

  public void shutdown() {
    System.out.println("异步任务执行完毕，关闭线程池");
    /*executorService.shutdown();*/
    threadPoolExecutor.shutdown();
  }

  public static void main(String[] args) {
    ThreadPoolUpdate poolUpdate = new ThreadPoolUpdate();
    poolUpdate.refershCacheAsync();
    try {
      // 等待一段时间，确保异步任务有足够的时间执行
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    poolUpdate.shutdown();
  }
}
