package com.mwl.chapter4;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;

/**
 * @author mawenlong
 * @date 2019-02-11 22:38
 */
public class HttpResponseV2 {
    private HttpHeaders header;
    private FullHttpResponse response;
    private byte[] body;

    public HttpResponseV2(FullHttpResponse response) {
        this.header = response.headers();
        this.response = response;
        if (response.content() != null) {
            body = new byte[response.content().readableBytes()];
            response.content().getBytes(0, body);
        }
    }

    public HttpHeaders header() {
        return header;
    }

    public byte[] body() {
        return body;
    }
}
