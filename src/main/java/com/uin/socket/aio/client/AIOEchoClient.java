package com.uin.socket.aio.client;


import com.uin.socket.commons.ServerInfo;
import com.uin.socket.util.InputUtil;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ClientReadHandler implements CompletionHandler<Integer, ByteBuffer> {

  private final AsynchronousSocketChannel clientChannel;
  private final CountDownLatch latch;

  public ClientReadHandler(AsynchronousSocketChannel clientChannel, CountDownLatch latch) {
    this.clientChannel = clientChannel;
    this.latch = latch;
  }

  /**
   * 操作成功的回调
   *
   * @param result The result of the I/O operation.
   * @param buffer The object attached to the I/O operation when it was initiated.
   */
  @Override
  public void completed(Integer result, ByteBuffer buffer) {
    buffer.flip();
    String receiveMessage = new String(buffer.array(), 0, buffer.remaining());
    log.info("收到消息:{}", receiveMessage);
  }

  /**
   * 操作失败的回调
   *
   * @param exc        The exception to indicate why the I/O operation failed
   * @param attachment The object attached to the I/O operation when it was initiated.
   */
  @Override
  public void failed(Throwable exc, ByteBuffer attachment) {
    try {
      this.clientChannel.close();
    } catch (Exception e) {
      log.info("异常:{}", e.getMessage());
    }
    this.latch.countDown();
  }
}

class ClientWriteHandler implements CompletionHandler<Integer, ByteBuffer> {

  private final AsynchronousSocketChannel clientChannel;
  private final CountDownLatch latch;

  public ClientWriteHandler(AsynchronousSocketChannel clientChannel, CountDownLatch latch) {
    this.clientChannel = clientChannel;
    this.latch = latch;
  }

  /**
   * 操作完成的回调
   *
   * @param result The result of the I/O operation.
   * @param buffer The object attached to the I/O operation when it was initiated.
   */
  @Override
  public void completed(Integer result, ByteBuffer buffer) {
    if (buffer.hasRemaining()) {
      this.clientChannel.write(buffer, buffer, this);
    } else {
      ByteBuffer readBuffer = ByteBuffer.allocate(50);
      this.clientChannel.read(readBuffer, readBuffer, new ClientReadHandler(this.clientChannel, this.latch));
    }
  }

  /**
   * 操作失败的回调
   *
   * @param exc        The exception to indicate why the I/O operation failed
   * @param attachment The object attached to the I/O operation when it was initiated.
   */
  @Override
  public void failed(Throwable exc, ByteBuffer attachment) {
    try {
      this.clientChannel.close();
    } catch (Exception e) {
    }
    this.latch.countDown();
  }
}

@Slf4j
class AIOClientThread implements Runnable {

  /**
   * 面向流的连接套接字的异步通道
   */
  private final AsynchronousSocketChannel clientChannel;
  private final CountDownLatch latch;

  public AIOClientThread() {
    // 打开客户端的Channel
    try {
      this.clientChannel = AsynchronousSocketChannel.open();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
    this.clientChannel.connect(new InetSocketAddress(ServerInfo.ECHO_SERVER_HOST, ServerInfo.PORT));
    this.latch = new CountDownLatch(1);
  }

  @Override
  public void run() {
    try {
      this.latch.await();
      this.clientChannel.close();
    } catch (Exception e) {
      log.info(e.getMessage());
    }
  }

  /**
   * 进行消息的发送处理
   *
   * @param msg 输入的交互内容
   * @return 是否停止交互的标记
   */
  public boolean sendMessage(String msg) {
    ByteBuffer buffer = ByteBuffer.allocate(50);
    buffer.put(msg.getBytes());
    buffer.flip();
    this.clientChannel.write(buffer, buffer, new ClientWriteHandler(this.clientChannel, this.latch));
    if ("exit".equalsIgnoreCase(msg)) {
      return false;
    }
    return true;
  }
}


public class AIOEchoClient {

  public static void main(String[] args) throws Exception {
    AIOClientThread client = new AIOClientThread();
    new Thread(client).start();
    while (client.sendMessage(InputUtil.getString("请输入要发送的信息："))) {

    }
  }
}
