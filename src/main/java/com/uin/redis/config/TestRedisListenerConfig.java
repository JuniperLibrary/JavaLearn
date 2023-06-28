package com.uin.redis.config;

import com.uin.redis.listener.TestRedisListener;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class TestRedisListenerConfig {

  /**
   * @param redisConnectionFactory
   * @param messageListenerAdapter
   * @return
   */
  @Bean
  public RedisMessageListenerContainer messageListenerContainer(RedisConnectionFactory redisConnectionFactory,
      MessageListenerAdapter messageListenerAdapter) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(redisConnectionFactory);
    container.addMessageListener(messageListenerAdapter, new PatternTopic("test*"));
    return container;
  }

  /**
   * 绑定消息监听者和接收监听的方法
   *
   * @param testRedisListener
   * @return
   */
  @Bean(name = "testMessageListenerAdapter")
  public MessageListenerAdapter testMessageListenerAdapter(TestRedisListener testRedisListener) {
    // redisReceiver 消息接收者
    // receiveMessage 消息接收后的方法
    return new MessageListenerAdapter(testRedisListener, "loadData");
  }
}
