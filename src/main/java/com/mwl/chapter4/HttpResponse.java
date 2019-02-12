package com.mwl.chapter4;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;

/**
 * @author mawenlong
 * @date 2019-02-11 23:01
 */
public class HttpResponse {
    private HttpHeaders header;
    private FullHttpResponse response;
    private byte[] body;

    public HttpResponse(FullHttpResponse response) {
        this.header = response.headers();
        this.response = response;
    }

//    public byte[] body() {
//        return body = response.content() != null?
//                response.content().array() : null;
//    }

    public byte[] body() {
        body = new byte[response.content().readableBytes()];
        response.content().getBytes(0, body);
        return body;
    }

}
