package com.mwl.chapter16;

import com.sun.security.ntlm.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;

/**
 * @author mawenlong
 * @date 2019-02-17 17:51
 */
public class TrafficShappingServer {
    static final int PORT = Integer.parseInt(System.getProperty("port", "8076"));

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
                 ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                 ch.pipeline()
                   .addLast("Channel Traffic Shaping",
                            new ChannelTrafficShapingHandler(1024 * 1024, 1024 * 1024,
                                                             1000))
                   .addLast(new DelimiterBasedFrameDecoder(2048 * 1024, delimiter))
                   .addLast(new StringDecoder())
                   .addLast(new TrafficShapingServerHandler());
             }
         });
        ChannelFuture f = b.bind(PORT).sync();
        f.channel().closeFuture().sync();

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
