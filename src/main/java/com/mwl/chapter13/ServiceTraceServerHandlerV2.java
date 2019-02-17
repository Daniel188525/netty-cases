package com.mwl.chapter13;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.SingleThreadEventExecutor;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mawenlong
 * @date 2019-02-17 13:08
 */
public class ServiceTraceServerHandlerV2 extends ChannelInboundHandlerAdapter {
    //消息发送性能统计
    AtomicInteger totalSendBytes = new AtomicInteger(0);
    static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    //线程池性能统计
    static ScheduledExecutorService kpiExecutorsService = Executors.newSingleThreadScheduledExecutor();
    //发送队列消息积压数
    static ScheduledExecutorService writeQueKpiExectorsService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            int qps = totalSendBytes.getAndSet(0);
            System.out.println("The Server write rate is " + qps + " byte/s.");
        }, 0, 1000, TimeUnit.MILLISECONDS);
        kpiExecutorsService.scheduleAtFixedRate(() -> {
            Iterator<EventExecutor> executors = ctx.executor().parent().iterator();
            while (executors.hasNext()) {
                SingleThreadEventExecutor executor = (SingleThreadEventExecutor) executors.next();
                int size = executor.pendingTasks();
                if (executor == ctx.executor()) {
                    System.out.println(ctx.channel() + "-->" + executor + " pending size in queue is " + size);
                } else {
                    System.out.println(executor + " pending size in queue is " + size);
                }
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        writeQueKpiExectorsService.scheduleAtFixedRate(() -> {
            long pendingSize = ((NioSocketChannel) ctx.channel()).unsafe().outboundBuffer().totalPendingWriteBytes();
            System.out.println(ctx.channel() + "--> " + " ChannelOutboundBuffer's totalPendingWriteBytes is : "
                               + pendingSize + " bytes");
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        int sendBytes = ((ByteBuf) msg).readableBytes();
        ChannelFuture writeFuture = ctx.write(msg);
        writeFuture.addListener((f) -> {
            totalSendBytes.getAndAdd(sendBytes);
        });
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
