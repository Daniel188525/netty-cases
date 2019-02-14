package com.mwl.chapter8;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mawenlong
 * @date 2019-02-14 22:41
 */
public class IotCarsDiscardServerHandler extends ChannelInboundHandlerAdapter {
    static AtomicInteger sum = new AtomicInteger(0);
    static ExecutorService executorService = new ThreadPoolExecutor(1, 3, 30, TimeUnit.SECONDS,
                                                                    new ArrayBlockingQueue<Runnable>(1000),
                                                                    new ThreadPoolExecutor.DiscardPolicy());

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(new Date() + "--> Server receive client message : " + sum.incrementAndGet());
        executorService.execute(() -> {
            ByteBuf req = (ByteBuf) msg;
            //其它业务逻辑处理，访问数据库
            if (sum.get() % 100 == 0 || (Thread.currentThread() == ctx.channel().eventLoop())) {
                try {
                    //访问数据库，模拟偶现的数据库慢，同步阻塞15秒
                    TimeUnit.SECONDS.sleep(15);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //转发消息，此处代码省略，转发成功之后返回响应给终端
            ctx.writeAndFlush(req);
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
