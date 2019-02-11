package com.mwl.chapter2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class ClientLeak {

    static final String HOST = "127.0.0.1";
    static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        initClintPool(150);
    }

    static void initClintPool(int poolSize) throws Exception {
        for (int i = 0; i < poolSize; i++) {
            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 protected void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(new LoggingHandler());
                 }
             });
            ChannelFuture future = b.connect(HOST, PORT).sync();
            future.channel().closeFuture().addListener((future1 -> {
                group.shutdownGracefully();
            }));
        }
    }

}
