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
 * @date 2019-02-16 23:52
 * <p>
 * 性能统计错误
 */
public class ServiceTraceServerHandler extends ChannelInboundHandlerAdapter {
    AtomicInteger totalSendBytes = new AtomicInteger(0);
    static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        int sendBytes = ((ByteBuf) msg).readableBytes();
        ctx.writeAndFlush(msg);
        totalSendBytes.getAndSet(sendBytes);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        executorService.scheduleAtFixedRate(() -> {
            int qps = totalSendBytes.getAndSet(0);
            System.out.println("The sever write rate is " + qps + " bytes/s.");

        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
