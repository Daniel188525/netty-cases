package com.mwl.chapter15;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author mawenlong
 * @date 2019-02-17 16:55
 */
public class EventTriggerClient {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8075"));

    public void connect() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
         .channel(NioSocketChannel.class)
         .option(ChannelOption.TCP_NODELAY, true)
         .handler(new ChannelInitializer<SocketChannel>() {
             @Override
             protected void initChannel(SocketChannel ch) throws Exception {
                 ByteBuf delimiter = Unpooled.copiedBuffer("_$".getBytes());
                 ch.pipeline()
                   .addLast(new DelimiterBasedFrameDecoder(2048, delimiter))
                   .addLast(new StringDecoder())
                   .addLast(new EventTriggerClientHandler());
             }
         });
        ChannelFuture f = b.connect(HOST, PORT).sync();
        f.channel().closeFuture().sync();

        group.shutdownGracefully();
    }

    public static void main(String[] args) throws InterruptedException {
        new EventTriggerClient().connect();
    }
}
