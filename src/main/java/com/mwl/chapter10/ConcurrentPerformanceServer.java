package com.mwl.chapter10;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author mawenlong
 * @date 2019/02/15
 */
public class ConcurrentPerformanceServer {
    static final int PORT = Integer.parseInt(System.getProperty("port", "8070"));
    static final EventExecutorGroup EXECUTORS = new DefaultEventExecutorGroup(100);

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                 .channel(NioServerSocketChannel.class)
                 .childOption(ChannelOption.SO_BACKLOG, 100)
                 .handler(new LoggingHandler(LogLevel.INFO))
                 .childHandler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     protected void initChannel(SocketChannel ch) throws Exception {
                         ch.pipeline()
                           .addLast(EXECUTORS, new ConcurrentPerformanceServerHandler());
                         // .addLast(new ConcurrentPerformanceServerHandlerV2());
                     }
                 });
        ChannelFuture future = bootstrap.bind(PORT).sync();
        future.channel().closeFuture().sync();

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}
