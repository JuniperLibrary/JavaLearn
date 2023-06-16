package com.uin.socket.aio.server;

import com.uin.socket.commons.ServerInfo;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;

/**
 * 2、实现客户端的回应处理操作
 */
@Slf4j
class EchoHandler implements CompletionHandler<Integer, ByteBuffer> {

  private final AsynchronousSocketChannel clientChannel;
  private boolean exit = false; // 进行操作的结束标记判断

  public EchoHandler(AsynchronousSocketChannel clientChannel) {
    this.clientChannel = clientChannel;
  }

  @Override
  public void completed(Integer result, ByteBuffer buffer) {
    buffer.flip();
    String readMessage = new String(buffer.array(), 0, buffer.remaining()).trim();
    System.out.println("【服务器端接收到消息内容】" + readMessage);
    // 回应信息
    String resultMessage = "【ECHO】" + readMessage + "\n";
    if ("exit".equalsIgnoreCase(readMessage)) {
      resultMessage = "【EXIT】Bye Bye ... kiss + \n";
      // 结束
      this.exit = true;
    }
    this.echoWrite(resultMessage);
  }

  private void echoWrite(String result) {
    // 分配 50 byte缓冲区
    ByteBuffer buffer = ByteBuffer.allocate(50);
    buffer.put(result.getBytes());
    buffer.flip();
    this.clientChannel.write(buffer, buffer, new CompletionHandler<>() {
      @Override
      public void completed(Integer result, ByteBuffer buffer) {
        // 当前有数据
        if (buffer.hasRemaining()) {
          EchoHandler.this.clientChannel.write(buffer, buffer, this);
        } else {
          // 需要继续交互
          if (!EchoHandler.this.exit) {
            ByteBuffer readBuffer = ByteBuffer.allocate(50);
            EchoHandler.this.clientChannel.read(readBuffer, readBuffer,
                new EchoHandler(EchoHandler.this.clientChannel));
          }
        }
      }

      @Override
      public void failed(Throwable exc, ByteBuffer attachment) {
        try {
          EchoHandler.this.clientChannel.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  @Override
  public void failed(Throwable exc, ByteBuffer attachment) {
    try {
      // 关闭通道
      this.clientChannel.close();
    } catch (Exception e) {
      log.info(e.getMessage());
    }
  }
}

/**
 * 1、实现客户端连接回调的处理操作
 */
@Slf4j
class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AIOServerThread> {

  /**
   * 连接成功的回调
   *
   * @param result     The result of the I/O operation.
   * @param attachment The object attached to the I/O operation when it was initiated.
   */
  @Override
  public void completed(AsynchronousSocketChannel result, AIOServerThread attachment) {
    attachment.getServerChannel().accept(attachment, this); // 接收连接对象
    ByteBuffer buffer = ByteBuffer.allocate(50);
    result.read(buffer, buffer, new EchoHandler(result));
  }

  /**
   * 连接失败的回调
   *
   * @param exc        The exception to indicate why the I/O operation failed
   * @param attachment The object attached to the I/O operation when it was initiated.
   */
  @Override
  public void failed(Throwable exc, AIOServerThread attachment) {
    log.error("服务器端客户端连接失败!失败的原因:{}", exc.getCause());
    // 恢复执行
    attachment.getLatch().countDown();
  }
}

/**
 * 是进行AIO处理的线程类
 */
@Slf4j
class AIOServerThread implements Runnable {

  /**
   * 异步通道
   */
  private final AsynchronousServerSocketChannel serverChannel;
  /**
   * 进行线程等待操作 同步操作
   */
  private final CountDownLatch latch;

  public AIOServerThread() throws Exception {
    // 设置一个线程等待个数
    this.latch = new CountDownLatch(1);
    // 打开异步的通道
    this.serverChannel = AsynchronousServerSocketChannel.open();
    // 绑定服务端口
    this.serverChannel.bind(new InetSocketAddress(ServerInfo.PORT));
    log.info("服务器启动成功，在{}端口上监听服务...", ServerInfo.PORT);
  }

  /**
   * 获取异步通道
   *
   * @return
   */
  public AsynchronousServerSocketChannel getServerChannel() {
    return serverChannel;
  }

  /**
   * 获取计数器
   *
   * @return
   */
  public CountDownLatch getLatch() {
    return latch;
  }

  /**
   * 重写run
   */
  @Override
  public void run() {
    // 等待客户端连接
    this.serverChannel.accept(this, new AcceptHandler());
    try {
      this.latch.await(); // 进入等待时机
    } catch (InterruptedException e) {
      log.info("启动线程出现异常:{}", e.getCause());
    }
  }
}

public class AIOEchoServer {

  public static void main(String[] args) throws Exception {
    new Thread(new AIOServerThread()).start();
  }
}
