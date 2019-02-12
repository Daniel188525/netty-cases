package com.mwl.chapter4;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.concurrent.DefaultPromise;

import java.util.concurrent.ExecutionException;


/**
 * @author mawenlong
 * @date 2019-02-11 22:34
 */
public class HttpClient {
    private Channel channel;
    HttpClientHandler handler = new HttpClientHandler();

    private void connect(String host, int port) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
         .channel(NioSocketChannel.class)
         .handler(new ChannelInitializer<SocketChannel>() {
             @Override
             protected void initChannel(SocketChannel ch) throws Exception {
                 ch.pipeline()
                   .addLast(new HttpClientCodec())
                   .addLast(new HttpObjectAggregator(Short.MAX_VALUE))
                   .addLast(handler);
             }
         });
        ChannelFuture future = b.connect(host, port).sync();
        channel = future.channel();
    }

    private HttpResponseV2 blockSend(FullHttpRequest request) throws InterruptedException, ExecutionException {
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        DefaultPromise<HttpResponseV2> respPromise = new DefaultPromise<>(channel.eventLoop());
        handler.setRespPromise(respPromise);
        channel.writeAndFlush(request);
        HttpResponseV2 response = respPromise.get();
        if (response != null) {
            System.out.print("The client received http response, the body is :" + new String(response.body()));
        }
        return response;
    }

    public static void main(String[] args) throws Exception {
        HttpClient client = new HttpClient();
        client.connect("127.0.0.1", 7070);
        ByteBuf body = Unpooled.wrappedBuffer("Http message!".getBytes("UTF-8"));
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                                                                    "http://127.0.0.1/user?id=11&addr=ShangHai", body);
        HttpResponseV2 response = client.blockSend(request);
    }
}
