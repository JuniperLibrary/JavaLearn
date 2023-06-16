package com.uin.socket.bio.client;


import com.uin.socket.commons.ServerInfo;
import com.uin.socket.util.InputUtil;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class EchoClientHandle implements AutoCloseable {

  private Socket client;

  public EchoClientHandle() throws Exception {
    this.client = new Socket(ServerInfo.ECHO_SERVER_HOST, ServerInfo.PORT);
    log.info("已经成功的连接到了服务器端，可以进行消息的发送处理。");
    this.accessServer();
  }

  /**
   * 数据交互处理
   *
   * @throws Exception
   */
  private void accessServer() throws Exception {
    // 服务器端的输出为客户端的输入
    Scanner scan = new Scanner(this.client.getInputStream());
    // 向服务器端发送内容
    PrintStream out = new PrintStream(this.client.getOutputStream());
    scan.useDelimiter("\n");
    boolean flag = true;
    while (flag) {
      String data = InputUtil.getString("请输入要发送的数据信息：");
      // 先把内容发送到服务器端上
      out.println(data);
      if ("exit".equalsIgnoreCase(data)) {
        // 结束循环s
        flag = false;
      }
      if (scan.hasNext()) {
        log.info(scan.next());
      }
    }
  }

  @Override
  public void close() throws Exception {
    this.client.close();
  }
}

public class EchoClient {

  public static void main(String[] args) {
    try (EchoClientHandle echo = new EchoClientHandle()) {

    } catch (Exception e) {
    }
  }
}
