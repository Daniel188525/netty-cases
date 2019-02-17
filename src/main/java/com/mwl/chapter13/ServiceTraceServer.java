package com.mwl.chapter13;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
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
 * @date 2019-02-17 00:30
 */
public class ServiceTraceServer {
    static final int PORT = Integer.parseInt(System.getProperty("port", "8073"));
    static final EventExecutorGroup executor = new DefaultEventExecutorGroup(100);

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
         .channel(NioServerSocketChannel.class)
         .option(ChannelOption.SO_BACKLOG, 100)
         .handler(new LoggingHandler(LogLevel.INFO))
         .childHandler(new ChannelInitializer<SocketChannel>() {
             @Override
             protected void initChannel(SocketChannel ch) throws Exception {
                 ch.config().setAllocator(UnpooledByteBufAllocator.DEFAULT);
                 ch.pipeline()
//                   .addLast(new ServiceTraceServerHandler()
                   .addLast(new ServiceTraceProfileServerHandler())
                   .addLast(new ServiceTraceServerHandlerV2());
             }
         }).childOption(ChannelOption.SO_RCVBUF, 8 * 1024)
         .childOption(ChannelOption.SO_SNDBUF, 8 * 1024);
        ChannelFuture f = b.bind(PORT).sync();
        f.channel().closeFuture().sync();

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
