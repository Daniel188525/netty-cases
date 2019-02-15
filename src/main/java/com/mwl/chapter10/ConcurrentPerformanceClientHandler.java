package com.mwl.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author mawenlong
 * @date 2019/02/15
 */
public class ConcurrentPerformanceClientHandler extends ChannelInboundHandlerAdapter {

    static ScheduledExecutorService scheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            for (int i = 0; i < 100; i++) {
                ByteBuf firstMessage = Unpooled.buffer(ConcurrentPerformanceClient.MSG_SIZE);
                for (int j = 0; j < firstMessage.capacity(); j++) {
                    firstMessage.writeByte((byte) j);
                }
                ctx.writeAndFlush(firstMessage);
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
