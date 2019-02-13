package com.mwl.chapter6;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author mawenlong
 * @date 2019-02-13 22:20
 */
public class ApiGatewayServerHandler extends ChannelInboundHandlerAdapter {
    ExecutorService executorService = Executors.newFixedThreadPool(8);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.write(msg);
        char[] req = new char[((ByteBuf) msg).readableBytes()];
        executorService.execute(() -> {
            char[] disptch = req;
            try {
                TimeUnit.MICROSECONDS.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ctx.write(msg);
//
//        char[] req = new char[64 * 1024];
//        executorService.execute(() -> {
//            char[] dispatch = req;
//            try {
//                TimeUnit.MICROSECONDS.sleep(5000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
