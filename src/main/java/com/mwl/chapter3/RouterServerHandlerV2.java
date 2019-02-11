package com.mwl.chapter3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mawenlong
 * @date 2019/02/11
 */
public class RouterServerHandlerV2 extends SimpleChannelInboundHandler<ByteBuf> {
    static ExecutorService executorService = Executors.newSingleThreadExecutor();
    PooledByteBufAllocator allocator;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        byte[] body = new byte[msg.readableBytes()];
        executorService.execute(() -> {
            if (allocator == null) {
                allocator = new PooledByteBufAllocator(false);
            }
            ByteBuf respMsg = allocator.heapBuffer(body.length);
            respMsg.writeBytes(body);
            ctx.writeAndFlush(respMsg);
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
