package com.mwl.chapter10;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author mawenlong
 * @date 2019/02/15
 */
public class ConcurrentPerformanceClient {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8070"));
    static final int MSG_SIZE = 256;

    public void connect() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
         .channel(NioSocketChannel.class)
         .option(ChannelOption.TCP_NODELAY, true)
         .handler(new ChannelInitializer<SocketChannel>() {
             @Override
             protected void initChannel(SocketChannel ch) throws Exception {
                 ch.pipeline()
                   .addLast(new ConcurrentPerformanceClientHandler());
             }
         });
        ChannelFuture future = b.connect(HOST, PORT).sync();
        future.channel().closeFuture().sync();

        group.shutdownGracefully();
    }

    public static void main(String[] args) throws InterruptedException {
        new ConcurrentPerformanceClient().connect();
    }

}
