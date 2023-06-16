# AIO

## CompletionHandler<Integer, ByteBuffer>

## AsynchronousSocketChannel

面向流的连接套接字的异步通道。
异步套接字通道以两种方式之一创建。 通过调用此类定义的open方法之一创建新创建的AsynchronousSocketChannel 。 新创建的频道已打开但尚未连接。
A相连接AsynchronousSocketChannel当连接到的插座制成创建AsynchronousServerSocketChannel 。 无法为任意预先存在的socket创建异步套接字通道。

通过调用其connect方法连接新创建的通道; 一旦连接，通道保持连接直到它关闭。 是否连接套接字通道可以通过调用其getRemoteAddress方法来确定。
尝试在未连接的通道上调用I / O操作将导致抛出NotYetConnectedException 。

这种类型的通道可以安全地供多个并发线程使用。 它们支持并发读写，但最多只有一个读操作和一个写操作可以在任何时候都很出色。
如果线程在先前的读操作完成之前启动读操作，则将抛出ReadPendingException 。
类似地，尝试在上一次写入完成之前启动写操作将抛出WritePendingException 。
