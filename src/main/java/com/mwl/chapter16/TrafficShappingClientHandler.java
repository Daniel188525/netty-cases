package com.mwl.chapter16;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mawenlong
 * @date 2019-02-17 17:20
 */
public class TrafficShappingClientHandler extends ChannelInboundHandlerAdapter {
    private AtomicInteger SEQ = new AtomicInteger(0);
    static final byte[] ECHO_REQ = new byte[1024 * 1024];
    static final String DELIMITER = "$_";

    static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        executorService.scheduleAtFixedRate(() -> {
            ByteBuf byteBuf = null;
            for (int i = 0; i < 10; i++) {
                byteBuf = Unpooled.copiedBuffer(ECHO_REQ, DELIMITER.getBytes());
                SEQ.getAndAdd(byteBuf.readableBytes());
                if (ctx.channel().isWritable()) {
                    ctx.write(byteBuf);
                }
            }
            ctx.flush();
            int counter = SEQ.getAndSet(0);
            System.out.println("The client send rate is :" + counter + " bytes/s.");
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
