package com.uin.socket.hutool.niotest;

import cn.hutool.core.io.BufferUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.socket.nio.NioClient;
import java.nio.ByteBuffer;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author dingchuan
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SocketNetworkClient {

  private static final NioClient client = new NioClient("127.0.0.1", 8080);

  public static void sendMessage() {
    client.setChannelHandler(socketChannel -> {
      ByteBuffer readBuffer = ByteBuffer.allocate(1024 * 5);
      // 从channel读取数据
      int readBytes = socketChannel.read(readBuffer);
      if (readBytes > 0) {
        readBuffer.flip();
        byte[] bytes = new byte[readBuffer.remaining()];
        readBuffer.get(bytes);
        String body = StrUtil.utf8Str(bytes);
        log.info("[{}]:{}", socketChannel.getRemoteAddress(), body);
      } else if (readBytes < 0) {
        socketChannel.close();
      }
    });
    client.listen();
    client.write(BufferUtil.createUtf8("HelloWorld.\n"));
    client.write(BufferUtil.createUtf8("HelloWorld..\n"));

    // 在控制台向服务器端发送数据
    Console.log("请输入发送的消息：");
    Scanner scanner = new Scanner(System.in);
    while (scanner.hasNextLine()) {
      String request = scanner.nextLine();
      if (request != null && request.trim().length() > 0) {
        client.write(BufferUtil.createUtf8(request));
      }
    }
//    for (int i = 0; i < 10; i++) {
//      int finalI = i;
//      ThreadUtil.execute(() -> {
//        try {
//          Thread.sleep(2000);
//        } catch (InterruptedException e) {
//          throw new RuntimeException(e);
//        }
//        client.write(BufferUtil.createUtf8("消息:" + finalI));
//      });
//    }

  }

  public static void main(String[] args) {
    sendMessage();
  }
}
