# Netty

Netty是由JBOSS提供的一个java开源框架。
Netty是 一个异步事件驱动的网络应用程序框架，用于快速开发高性能、高可靠性的网络服务器和客户端程序。它提供了对TCP、UDP以及各种二进制和基于文本的传统协议的支持。它极大地简化并优化了
TCP 和 UDP 套接字服务器等网络编程，解决了断线重连、网络闪断、心跳处理、半包读写、网络拥塞和异常流的处理等等问题。
Netty 现在都在用的是4.x，5.x版本已经封存，Netty 4.x 需要JDK 6以上版本支持。

## Netty的使用场景

1. RPC 框架的网络通信工具：
   在分布式系统中，各个节点之间需要远程服务调用，高性能的 RPC 框架必不可少，Netty 作为异步高性能的通信框架，往往作为基础通信组件被这些
   RPC 框架使用。典型的应用有：阿里分布式服务框架 Dubbo 的RPC 框架使用 Netty
   作为基础通信组件，用于实现各进程节点之间的内部通信。Rocketmq底层也是用的Netty作为基础通信组件。
2. 实现HTTP 服务器：
   Netty 作为高性能的基础通信组件，它本身提供了 TCP/UDP 和 HTTP 协议栈，通过 Netty 可以实现一个简单的 HTTP 服务器。
3. 消息推送和通信系统：
   市面上有很多消息推送系统都是基于 Netty 来做的，另外使用 Netty 实现一个可以聊天类似微信的即时通讯系统。

### Netty为什么高性能

1. 多路复用通讯方式
   Netty 的 IO 线程 NioEventLoop 由于聚合了多路复用器 Selector，可以同时并发处理成百上千个客户端
   Channel，由于读写操作都是非阻塞的，这就可以充分提升 IO 线程的运行效率，避免由于频繁 IO 阻塞导致的线程挂起。
2. 异步通信 NIO
   由于 Netty 采用了异步通信模式，一个 IO 线程可以并发处理 N 个客户端连接和读写操作，这从根本上解决了传统同步阻塞 IO
   一连接一线程模型，架构的性能、弹性伸缩能力和可靠性都得到了极大的提升。
3. 零拷贝（ DIRECT BUFFERS 使用堆外内存）
   在发送数据的时候，传统的实现方式是：

```python
File.read(bytes)
Socket.send(bytes)
```

这种方式需要四次数据拷贝和四次上下文切换：

1. 数据从磁盘读取到内核的read buffer
2. 数据从内核缓冲区拷贝到用户缓冲区
3. 数据从用户缓冲区拷贝到内核的socket buffer
4. 数据从内核的socket buffer拷贝到网卡接口（硬件）的缓冲区

而在 netty 中，使用的是堆外内存进行拷贝：

1. Netty 的接收和发送 ByteBuffer 采用 DIRECT BUFFERS，使用堆外直接内存进行 Socket 读写，
   不需要进行字节缓冲区的二次拷贝。如果使用传统的堆内存(HEAP BUFFERS)进行 Socket
   读写， JVM 会将堆内存 Buffer 拷贝一份到直接内存中，然后才写入 Socket 中。相比于堆外直接内存， 消息在发送过程中多了一次缓冲区的内存拷贝。
2. Netty 提供了组合 Buffer 对象，可以聚合多个 ByteBuffer 对象，用户可以像操作一个 Buffer 那样 方便的对组合 Buffer
   进行操作，避免了传统通过内存拷贝的方式将几个小 Buffer 合并成一个大的
   Buffer。
3. Netty 的文件传输采用了 transferTo 方法，它可以直接将文件缓冲区的数据发送到目标 Channel， 避免了传统通过循环 write
   方式导致的内存拷贝问题。

## Netty模块组件

### Bootstrap

Bootstrap 意思是引导，一个 Netty 应用通常由一个 Bootstrap 开始，主要作用是配置整个 Netty 程序，串联各个组件，Netty 中
Bootstrap 类是客户端程序的启动引导类，ServerBootstrap 是服务端启动引导类。

### NioEventLoop

EventLoop 的主要作用是监听网络事件并调用事件处理器进行相关 I/O 操作的处理。**NioEventLoop
维护了一个线程和任务队列，支持异步提交执行任务，线程启动时会调用 NioEventLoop 的 run 方法，执行 I/O 任务和非 I/O 任务：
I/O 任务，即 selectionKey 中 ready 的事件，如 accept、connect、read、write 等，由 processSelectedKeys 方法触发。
非 IO 任务，添加到 taskQueue 中的任务，如 register0、bind0 等任务，由 runAllTasks 方法触发。

### NioEventLoopGroup

NioEventLoopGroup，主要管理 eventLoop 的生命周期，可以理解为一个线程池，内部维护了一组线程，每个线程(NioEventLoop)负责处理多个
Channel 上的事件，而一个 Channel 只对应于一个线程。

### ChannelPipline

保存 ChannelHandler 的 List，用于处理或拦截 Channel 的入站事件和出站操作。ChannelPipeline
实现了一种高级形式的拦截过滤器模式，使用户可以完全控制事件的处理方式，以及 Channel 中各个的 ChannelHandler 如何相互交互。
在 Netty 中每个 Channel 都有且仅有一个 ChannelPipeline 与之对应，它们的组成关系如下图：

### Channel

Netty 网络通信的组件，能够用于执行网络 I/O 操作。Channel 为用户提供：

1. 当前网络连接的通道的状态（例如是否打开？是否已连接？）
2. 网络连接的配置参数 （例如接收缓冲区大小）
3. 提供异步的网络 I/O 操作(如建立连接，读写，绑定端口)，异步调用意味着任何 I/O 调用都将立即返回，并且不保证在调用结束时所请求的
   I/O 操作已完成。
4. 调用立即返回一个 ChannelFuture 实例，通过注册监听器到 ChannelFuture 上，可以 I/O 操作成功、失败或取消时回调通知调用方。
5. 支持关联 I/O 操作与对应的处理程序。
   不同协议、不同的阻塞类型的连接都有不同的 Channel 类型与之对应。
   
下面是一些常用的 Channel 类型：

```text
- NioSocketChannel，异步的客户端 TCP Socket 连接。
- NioServerSocketChannel，异步的服务器端 TCP Socket 连接。
- NioDatagramChannel，异步的 UDP 连接。
- NioSctpChannel，异步的客户端 Sctp 连接。
- NioSctpServerChannel，异步的 Sctp 服务器端连接。
- 这些通道涵盖了 UDP 和 TCP 网络 IO 以及文件 IO。
```

### Selector

Netty 基于 Selector 对象实现 I/O 多路复用，通过 Selector 一个线程可以监听多个连接的 Channel 事件。当向一个 Selector 中注册
Channel 后，Selector 内部的机制就可以自动不断地查询(Select) 这些注册的 Channel 是否有已就绪的 I/O
事件（例如可读，可写，网络连接完成等），这样程序就可以很简单地使用一个线程高效地管理多个 Channel 。

### Future

正如前面介绍，在 Netty 中所有的 IO 操作都是异步的，不能立刻得知消息是否被正确处理。但是可以过一会等它执行完成或者直接注册一个监听，具体的实现就是通过
Future 和 ChannelFutures，他们可以注册一个监听，当操作执行成功或失败时监听会自动触发注册的监听事件。

### ChannelHandler

ChannelHandler 是消息的具体处理器。ChannelHandler 是一个接口，处理 I/O 事件或拦截 I/O 操作，并将其转发到其
ChannelPipeline(业务处理链)中的下一个处理程序。
ChannelHandler 本身并没有提供很多方法，因为这个接口有许多的方法需要实现，方便使用期间，可以继承它的子类：

```text
ChannelInboundHandler 用于处理入站 I/O 事件。
ChannelOutboundHandler 用于处理出站 I/O 操作。
```

或者使用以下适配器类：

```text
ChannelInboundHandlerAdapter 用于处理入站 I/O 事件。
ChannelOutboundHandlerAdapter 用于处理出站 I/O 操作。
```
