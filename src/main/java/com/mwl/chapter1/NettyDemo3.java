package com.mwl.chapter1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyDemo3 {
    public static void main(String[] args) throws Exception{
       final EventLoopGroup bossGroup = new NioEventLoopGroup();
       final EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        }
                    });
          final ChannelFuture future= bootstrap.bind(8000).sync();
//         closeFuture 同步阻塞
            future.channel().closeFuture().addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    bossGroup.shutdownGracefully();
                    workGroup.shutdownGracefully();
                }
            });
        } finally {
//            bossGroup.shutdownGracefully();
//            workGroup.shutdownGracefully();
        }
    }
}
