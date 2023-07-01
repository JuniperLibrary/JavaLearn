# redis 发布/订阅(Pub/Sub)

[//]: # (http://doc.redisfans.com/topic/pubsub.html)
Redis发布订阅是一种通信机制，将数据推到某个信道中，其他客户端可通过订阅这些管道来获取推送信息，以此用于消息的传输。

在Redis的发布订阅模型中，由三部分组成：Publisher、Channel、Subscriber

Publisher发布的消息分到不同的Channel中，不需要知道什么样的Subscriber订阅了。订阅者对一个或多个Channel感兴趣，只需要接受感兴趣的消息，不需要知道什么样的发布者。

主要的目的就是消除发布者和订阅者之间的耦合关系。在Redis中发布者和订阅者都是Redis客户端，channel就是服务器端。

## 原理

Redis通过 subscribe、psubscribe、unsubscribe和punsubscribe等命令实现发布和订阅功能。

在Redis底层结构中，客户端和频道的订阅关系是通过一个字典+链表的结构保存。




