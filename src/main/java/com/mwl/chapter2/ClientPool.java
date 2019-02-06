package com.mwl.chapter2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import javax.swing.*;

public class ClientPool {

    static final String HOST = "127.0.0.1";
    static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        initClintPool(10);
    }

    static void initClintPool(int poolSize) throws Exception {
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
