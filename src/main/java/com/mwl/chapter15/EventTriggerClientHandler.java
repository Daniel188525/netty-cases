package com.mwl.chapter15;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.omg.CORBA.TIMEOUT;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mawenlong
 * @date 2019-02-17 16:49
 */
public class EventTriggerClientHandler extends ChannelInboundHandlerAdapter {
    private static AtomicInteger SEQ = new AtomicInteger(0);
    static final String ECHO_REQ = "Hi,welcome to Netty";
    static final String DELIMITER = "_$";

    static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            int counter = SEQ.getAndIncrement();
            if (counter % 10 == 0) {
                ctx.writeAndFlush(Unpooled.copiedBuffer((ECHO_REQ + DELIMITER).getBytes()));
            } else {
                ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
