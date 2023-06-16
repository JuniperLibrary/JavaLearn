package com.uin.socket.bio.server;

import com.uin.socket.commons.ServerInfo;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class EchoServerHandle implements AutoCloseable {

  private final ServerSocket serverSocket;

  public EchoServerHandle() throws Exception {
    this.serverSocket = new ServerSocket(ServerInfo.PORT);   // 进行服务端的Socket启动
    log.info("ECHO服务器端已经启动了，该服务在{}端口上监听....", ServerInfo.PORT);
    this.clientConnect();
  }

  private void clientConnect() throws Exception {
    boolean serverFlag = true;
    while (serverFlag) {
      // 等待客户端连接
      Socket client = this.serverSocket.accept();
      Thread clientThread = new Thread(() -> {
        try {
          // 服务器端输入为客户端输出
          Scanner scan = new Scanner(client.getInputStream());
          // 服务器端的输出为客户端输入
          PrintStream out = new PrintStream(client.getOutputStream());
          // 设置分隔符
          scan.useDelimiter("\n");
          boolean clientFlag = true;
          while (clientFlag) {
            // 现在有内容
            if (scan.hasNext()) {
              // 获得输入数据
              String inputData = scan.next();
              // 信息结束
              if ("exit".equalsIgnoreCase(inputData)) {
                // 结束内部的循环
                clientFlag = false;
                // 一定需要提供有一个换行机制，否则Scanner不好读取
                out.println("【ECHO】Bye Bye ... kiss");
              } else {
                // 回应信息
                out.println("【ECHO】" + inputData);
              }
            }
          }
          client.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
      clientThread.start(); // 启动多线程
    }
  }

  @Override
  public void close() throws Exception {
    this.serverSocket.close();
  }
}

/**
 * 实现服务器端的编写开发，采用BIO（阻塞模式）实现开发的基础结构
 */
public class EchoServer {

  public static void main(String[] args) throws Exception {
    new EchoServerHandle();
  }
}
