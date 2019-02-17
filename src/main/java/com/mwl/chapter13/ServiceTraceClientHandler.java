package com.mwl.chapter13;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author mawenlong
 * @date 2019-02-17 12:56
 */
public class ServiceTraceClientHandler extends ChannelInboundHandlerAdapter {

    static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        executorService.scheduleAtFixedRate(() -> {
            for (int i = 0; i < 100; i++) {
                ByteBuf message = Unpooled.buffer(ServiceTraceClient.MSG_SIZE);
                for (int j = 0; j < message.capacity(); j++) {
                    message.writeByte((byte) j);
                }
                ctx.writeAndFlush(message);
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
