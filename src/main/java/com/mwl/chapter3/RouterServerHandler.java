package com.mwl.chapter3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mawenlong
 * @date 2019/02/11
 */
public class RouterServerHandler extends ChannelInboundHandlerAdapter {
    static ExecutorService executorService = Executors.newSingleThreadExecutor();
    PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf reqMsg = (ByteBuf) msg;
        byte[] body = new byte[reqMsg.readableBytes()];
        // ReferenceCountUtil.release(reqMsg);
        executorService.execute(() -> {
            ByteBuf respMsg = allocator.heapBuffer(body.length);
            respMsg.writeBytes(body);
            ctx.writeAndFlush(respMsg);
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
