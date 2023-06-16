package com.uin.socket.nio.client;


import com.uin.socket.commons.ServerInfo;
import com.uin.socket.util.InputUtil;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * 进行NIO客户端的连接访问。
 */
public class NIOEchoClient {

  public static void main(String[] args) throws Exception {
    try (EchoClientHandle handle = new EchoClientHandle()) {

    } catch (Exception e) {
    }
  }
}

@Slf4j
class EchoClientHandle implements AutoCloseable {

  private final SocketChannel clientChannel;

  public EchoClientHandle() throws Exception {
    // 创建一个客户端的通道实例
    this.clientChannel = SocketChannel.open();
    // 设置要连接的主机信息，包括主机名称以及端口号
    this.clientChannel.connect(new InetSocketAddress(ServerInfo.ECHO_SERVER_HOST, ServerInfo.PORT));
    this.accessServer();
  }

  /**
   * 访问服务器端
   *
   * @throws Exception
   */
  public void accessServer() throws Exception {
    // 开辟一个缓冲区
    ByteBuffer buffer = ByteBuffer.allocate(50);
    boolean flag = true;
    while (flag) {
      // 清空缓冲区，因为该部分代码会重复执行
      buffer.clear();
      String msg = InputUtil.getString("请输入要发送的内容：");
      // 将此数据保存在缓冲区之中
      buffer.put(msg.getBytes());
      // 重置缓冲区
      buffer.flip();
      // 发送数据内容
      this.clientChannel.write(buffer);
      // 当消息发送过去之后还需要进行返回内容的接收处理 清空缓冲区，等待新的内容的输入
      buffer.clear();
      // 将内容读取到缓冲区之中，并且返回个数
      int readCount = this.clientChannel.read(buffer);
      // 得到前需要进行重置
      buffer.flip();
      // 输出信息
      log.info(new String(buffer.array(), 0, readCount));
      if ("exit".equalsIgnoreCase(msg)) {
        flag = false;
      }
    }
  }

  @Override
  public void close() throws Exception {
    this.clientChannel.close();
  }
}
