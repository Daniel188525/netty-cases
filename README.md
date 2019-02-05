《Netty进阶之路-跟着案例学Netty》对应的源码。

# 1.netty服务器意外退出

## 1.1 java Daemon线程
- Daemon线程(守护线程)：通常JVM创建(GC线程)
- 主线程不是守护线程
- 如果虚拟机中只有Daemon线程则程序退出

## 1.2 NioEvenLoop
- NioEvenLoop是非守护线程
- NioEvenLoop运行之后不会主动退出
- NioEvenLoop只有调用shutdown系列方法才会退出