package com.uin.redis.listener;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
//public class TestRedisListener implements MessageListener {
public class TestRedisListener {

//  @Override
//  public void onMessage(Message message, byte[] pattern) {
//    byte[] channel = message.getChannel();
//
//    log.info("on message->{}", Arrays.toString(channel));
//    try {
//      String title = new String(channel, "UTF-8");
//      String content = new String(message.getBody(), "UTF-8");
//      log.info("onMessage title:{},boddy:{}", title, content);
//    } catch (Exception e) {
//      throw new IORuntimeException("处理消息失败！原因" + e.getCause());
//    }
//  }

  public void loadData(String message) {
    if (CharSequenceUtil.isNotBlank(message)) {
      log.info("cnbd 接收到redis通道信号，开始更新中债估值数据信息：{}", message);
    }
  }
}
