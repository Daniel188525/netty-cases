package com.mwl.chapter12;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.util.Arrays;

/**
 * @author mawenlong
 * @date 2019/02/15
 */
public class RestfulReq {

    private HttpMethod method;

    private HttpVersion version;

    private byte[] body;

    public RestfulReq(byte[] body) {
        this.body = body;
    }

    public byte[] body() {
        if (this.body != null) {
            return Arrays.copyOf(this.body, this.body.length);
        }
        return null;
    }

}
