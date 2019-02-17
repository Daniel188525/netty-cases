package com.mwl.chapter13;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mawenlong
 * @date 2019-02-17 15:53
 * <p>
 * netty消息读取速度性能统计
 */
public class ServiceTraceProfileServerHandler extends ChannelInboundHandlerAdapter {
    AtomicInteger totalSendBytes = new AtomicInteger(0);
    static ScheduledExecutorService kpiExecutor = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        kpiExecutor.scheduleAtFixedRate(() -> {
            int readRates = totalSendBytes.getAndSet(0);
            System.out.println(ctx.channel() + "--->read bytes " + readRates + " bytes/s");
        }, 0, 1000, TimeUnit.MILLISECONDS);
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        int readBytes = ((ByteBuf) msg).readableBytes();
        totalSendBytes.getAndAdd(readBytes);
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
