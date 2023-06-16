package com.uin.socket.aiotest;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.socket.aio.AioClient;
import cn.hutool.socket.aio.AioSession;
import cn.hutool.socket.aio.SimpleIoAction;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author dingchuan
 */
@Slf4j
@Component
public class SocketNetworkClient {

  private static final AioClient aioClient = new AioClient(new InetSocketAddress("127.0.0.1", 8899),
      new SimpleIoAction() {
        @Override
        public void doAction(AioSession session, ByteBuffer data) {
          if (data.hasRemaining()) {
            Console.log(StrUtil.utf8Str(data));
            session.read();
          }
          Console.log("OK");
        }
      });

  public static void sendMessage() {
    aioClient.write(ByteBuffer.wrap("Hello".getBytes()));
    aioClient.read();
//    aioClient.close();
  }

  public static void main(String[] args) {
    sendMessage();
  }

}
