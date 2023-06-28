package com.uin.socket.hutool.niotest;

import cn.hutool.core.io.BufferUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.socket.nio.NioServer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author dingchuan
 */
@RequiredArgsConstructor
@Slf4j
public class SocketNetworkService {

  private static final NioServer server = new NioServer(8080);

  public static void receiveMessage() {
    server.setChannelHandler((socketChannel -> {
      ByteBuffer readBuffer = ByteBuffer.allocate(1024 * 5);
      try {
        // 从channel读数据到缓冲区
        int read = socketChannel.read(readBuffer);
        if (read > 0) {
          // Flips this buffer.  The limit is set to the current position and then
          // the position is set to zero，就是表示要从起始位置开始读取数据
          readBuffer.flip();
          // 要读取的字节长度
          byte[] bytes = new byte[readBuffer.remaining()];
          // 将缓冲区的数据读到bytes数组
          readBuffer.get(bytes);
          String body = StrUtil.utf8Str(bytes);
          log.info("[{}]:{}", socketChannel.getRemoteAddress(), body);
          doWrite(socketChannel, body);
        } else if (read < 0) {
          socketChannel.close();
        }
      } catch (IOException e) {
        throw new IORuntimeException(e);
      }
    }));
    server.listen();
  }

  private static void doWrite(SocketChannel socketChannel, String body) {
    try {
      body = "收到消息：" + body;
      // 将缓冲数据写入渠道，返回给客户端
      socketChannel.write(BufferUtil.createUtf8(body));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {
    receiveMessage();
  }
}
