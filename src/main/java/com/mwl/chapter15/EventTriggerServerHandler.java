package com.mwl.chapter15;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.channels.Channel;

/**
 * @author mawenlong
 * @date 2019-02-17 16:30
 */
public class EventTriggerServerHandler extends ChannelInboundHandlerAdapter {
    int count;
    int readCompleteTimes;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("This is " + (++count) + " times receive client :[" + body + "]");
        body += "_$";
        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(echo);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
        readCompleteTimes++;
        System.out.println("This is " + readCompleteTimes + " times receive ReadComplete event.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
