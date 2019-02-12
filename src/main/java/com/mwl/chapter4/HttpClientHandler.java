package com.mwl.chapter4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.concurrent.DefaultPromise;

/**
 * @author mawenlong
 * @date 2019-02-11 22:35
 */
public class HttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    DefaultPromise<HttpResponseV2> respPromise;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        if (msg.decoderResult().isFailure()) {
            throw new Exception("Decode HttpResponse error : " + msg.decoderResult().cause());
        }
        HttpResponseV2 response = new HttpResponseV2(msg);
        respPromise.setSuccess(response);
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public DefaultPromise<HttpResponseV2> getRespPromise() {
        return respPromise;
    }

    public void setRespPromise(DefaultPromise<HttpResponseV2> respPromise) {
        this.respPromise = respPromise;
    }
}
