package com.mwl.chapter2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class ClientPoolError1 {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "18081"));

    public static void main(String[] args) throws Exception {
//        TimeUnit.SECONDS.sleep(30);
        initClientPool(100);
    }

    static void initClientPool(int poolSize) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new LoggingHandler());
                    }
                });
        for (int i = 0; i < poolSize; i++) {
            ChannelFuture f = b.connect(HOST, PORT).sync();
            f.channel().closeFuture().addListener((r) -> {
                group.shutdownGracefully();
            });
        }
    }
}
